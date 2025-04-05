package io.justme.lavender.module.impl.legit.fight;

import io.justme.lavender.La;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.math.MathUtility;
import io.justme.lavender.utility.player.MovementUtility;
import io.justme.lavender.utility.player.RotationUtility;
import io.justme.lavender.value.impl.NumberValue;
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

@ModuleInfo(name = "AimAssist", description = "Helps you aim at targets.", category = Category.FIGHT)
public class AimAssist extends Module {

    private final NumberValue yaw = new NumberValue("Yaw", 1.0, 0.1, 5.0, 0.1);
    private final NumberValue pitch = new NumberValue("Pitch", 1.0, 0.1, 5.0, 0.1);

    private final NumberValue range = new NumberValue("Range", 4.5, 1.0, 6.0, 0.1);
    private final NumberValue speed = new NumberValue("Speed", 1.0, 0.1, 5.0, 0.1);

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {
        if (Mouse.isButtonDown(0)) {
            EntityLivingBase target = getClosestTarget();
            if (target != null && !isTargetInCrossHair(target)) {
                float[] rotations = RotationUtility.getRotationToEntity(target);
                float wrappedYaw = MathHelper.wrapAngleTo180_float(rotations[0] - event.getYaw());
                float wrappedPitch = MathHelper.wrapAngleTo180_float(rotations[1] - event.getPitch());

                // 调整yaw
                float smoothYaw = MathUtility.lerp(event.getYaw(), event.getYaw() + wrappedYaw, (speed.getValue() * 0.1f) + MathUtility.getRandomFloat(-0.05f, 0.05f));
                event.setYaw(smoothYaw);

                // 调整pitch
                if (Math.abs(wrappedPitch) > 20) {
                    float smoothPitch = MathUtility.lerp(event.getPitch(), event.getPitch() + wrappedPitch, (speed.getValue() * 0.1f) + MathUtility.getRandomFloat(-0.05f, 0.05f));
                    event.setPitch(smoothPitch);
                }

                // 更新玩家的yaw和pitch
                Minecraft.getMinecraft().thePlayer.rotationYaw = event.getYaw();
                Minecraft.getMinecraft().thePlayer.rotationPitch = event.getPitch();
            }
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
                double distance = mc.thePlayer.getDistanceToEntity(entity);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestTarget = (EntityLivingBase) entity;
                }
            }
        }

        return closestTarget;
    }
}