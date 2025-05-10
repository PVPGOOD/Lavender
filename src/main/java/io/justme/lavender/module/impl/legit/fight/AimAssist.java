package io.justme.lavender.module.impl.legit.fight;

import io.justme.lavender.La;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.math.MathUtility;
import io.justme.lavender.utility.player.MovementUtility;
import io.justme.lavender.utility.player.RotationUtility;
import io.justme.lavender.utility.player.ValidEntityUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjglx.input.Mouse;

import java.util.List;

@Getter
@ModuleInfo(name = "AimAssist", description = "Helps you aim at targets.", category = Category.FIGHT)
public class AimAssist extends Module {


    private final BoolValue edge_stop = new BoolValue("Edge Stop", false);
    private final NumberValue edgeReduction = new NumberValue("Edge Reduction", 90f, 1.0f, 360,0.1f);

    private final NumberValue range = new NumberValue("Range", 4.5, 1.0, 6.0, 0.1);
    private final NumberValue pitch = new NumberValue("Pitch", 1.0, -80, 80, 5);
    private final NumberValue fov = new NumberValue("Fov", 180, 5, 360, 5);

    private final NumberValue yaw_speed = new NumberValue("Yaw Speed", 10.0, 0.1, 20, 0.1);
    private final NumberValue pitch_speed = new NumberValue("Pitch Speed", 10.0, 0.1, 20, 0.1);

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {
        if (mc.currentScreen != null || !Mouse.isButtonDown(0)) return;

        EntityLivingBase target = getClosestTarget();
        if (target == null) return;

        if (getEdge_stop().getValue() && isTargetInCrossHair(target)) {
            return;
        }

        float[] rot = RotationUtility.getRotationToEntity(target);
        float wrappedYaw = MathHelper.wrapAngleTo180_float(rot[0] - event.getYaw());
        float wrappedPitch = MathHelper.wrapAngleTo180_float(rot[1] - event.getPitch());
        float absYaw = Math.abs(wrappedYaw);
        float absPitch = Math.abs(wrappedPitch);

        float maxYawSpeed = getYaw_speed().getValue()   * 0.05f;
        float maxPitchSpeed = getPitch_speed().getValue() * 0.05f;

        var EDGE_ANGLE = getEdgeReduction().getValue();
        float finalYawSpeed, finalPitchSpeed;

        if (getEdge_stop().getValue() && absYaw < EDGE_ANGLE) {
            finalYawSpeed = maxYawSpeed * (absYaw / EDGE_ANGLE);
        } else {
            finalYawSpeed = maxYawSpeed;
        }

        if (getEdge_stop().getValue() && absPitch < EDGE_ANGLE) {
            finalPitchSpeed = maxPitchSpeed * (absPitch / EDGE_ANGLE);
        } else {
            finalPitchSpeed = maxPitchSpeed;
        }

        if (absYaw > 0.1f) {
            mc.thePlayer.rotationYaw = MathUtility.lerp(
                    mc.thePlayer.rotationYaw,
                    mc.thePlayer.rotationYaw + wrappedYaw,
                    finalYawSpeed
            );
        }
        if (absPitch > 0.1f) {
            mc.thePlayer.rotationPitch = MathUtility.lerp(
                    mc.thePlayer.rotationPitch,
                    mc.thePlayer.rotationPitch + wrappedPitch,
                    finalPitchSpeed
            );
        }
    }

    private boolean isTargetInCrossHair(Entity target) {
        Minecraft mc = Minecraft.getMinecraft();
        Vec3 playerPos = mc.thePlayer.getPositionEyes(1.0f);
        Vec3 lookVec = mc.thePlayer.getLook(1.0f);
        Vec3 reachVec = playerPos.addVector(lookVec.xCoord * range.getValue(), lookVec.yCoord * range.getValue(), lookVec.zCoord * range.getValue());
        MovingObjectPosition rayTraceResult = mc.theWorld.rayTraceBlocks(playerPos, reachVec, false, false, false);

        if (rayTraceResult != null && rayTraceResult.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            return rayTraceResult.entityHit == target;
        }

        // 检查射线是否与实体相交
        List<Entity> entitiesInPath = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(lookVec.xCoord * range.getValue(), lookVec.yCoord * range.getValue(), lookVec.zCoord * range.getValue()).expand(1.0, 1.0, 1.0));
        for (Entity entity : entitiesInPath) {
            if (entity.canBeCollidedWith() && entity == target) {
                AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox().expand(0.1, 0.1, 0.1);
                MovingObjectPosition intercept = entityBoundingBox.calculateIntercept(playerPos, reachVec);
                if (intercept != null) {
                    return true;
                }
            }
        }

        return false;
    }

    private EntityLivingBase getClosestTarget() {
        EntityLivingBase closestTarget = null;
        double closestDistance = range.getValue();

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer && entity != mc.thePlayer && entity.isEntityAlive()) {
                if (ValidEntityUtility.isOnSameTeam((EntityLivingBase) entity)) continue;
                if (notInFov(entity)) continue;
                double distance = mc.thePlayer.getDistanceToEntity(entity);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestTarget = (EntityLivingBase) entity;
                }
            }
        }

        return closestTarget;
    }

    private boolean notInFov(Entity entity) {
        return !(Math.abs(RotationUtility.getYawDifference(mc.thePlayer.rotationYaw, entity.posX, entity.posY, entity.posZ)) <= getFov().getValue().floatValue());
    }
}