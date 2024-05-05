package io.justme.lavender.utility.world;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import io.justme.lavender.utility.interfaces.IMinecraft;
import lombok.experimental.UtilityClass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;
import net.optifine.reflect.Reflector;

import java.util.List;

/**
 * @author JustMe.
 * @since 2024/4/29
 **/
@UtilityClass
public class WorldUtility implements IMinecraft {
    public MovingObjectPosition raytrace(float yaw, float pitch) {
        var blockReachDistance = mc.playerController.getBlockReachDistance();
        var hitVec =
                mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks).addVector(
                        mc.thePlayer.getVectorForRotation(pitch, yaw).xCoord * blockReachDistance,
                        mc.thePlayer.getVectorForRotation(pitch, yaw).yCoord * blockReachDistance,
                        mc.thePlayer.getVectorForRotation(pitch, yaw).zCoord * blockReachDistance);
        return mc.theWorld.rayTraceBlocks(mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks), hitVec, false, false, true);
    }

    public boolean ray_castEntity(EntityLivingBase target, float yaw, float pitch, float lastYaw, float lastPitch, double reach) {
        Entity entity = mc.getRenderViewEntity();

        Entity pointedEntity = null;

        if (entity != null && mc.theWorld != null) {
            float partialTicks = mc.timer.renderPartialTicks;

            double d0 = mc.playerController.getBlockReachDistance();
            mc.objectMouseOver = raytraceLegit(yaw, pitch, lastYaw, lastPitch);
            double d1 = d0;
            Vec3 vec3 = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            int i = 3;

            if (mc.playerController.extendedReach())
            {
                d0 = 6.0D;
                d1 = 6.0D;
            }
            else if (d0 > reach)
            {
                flag = true;
            }

            if (mc.objectMouseOver != null)
            {
                d1 = mc.objectMouseOver.hitVec.distanceTo(vec3);
            }

            float aaaa = lastPitch + (pitch - lastPitch) * partialTicks;
            float bbbb = lastYaw + (yaw - lastYaw) * partialTicks;
            Vec3 vec31 = mc.thePlayer.getVectorForRotation(aaaa, bbbb);

            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            Vec3 vec33 = null;
            float f = 1.0F;
            List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
            {
                public boolean apply(Entity p_apply_1_)
                {
                    return p_apply_1_.canBeCollidedWith();
                }
            }));
            double d2 = d1;

            for (int j = 0; j < list.size(); ++j)
            {
                Entity entity1 = (Entity)list.get(j);
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3))
                {
                    if (d2 >= 0.0D)
                    {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                }
                else if (movingobjectposition != null)
                {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D)
                    {
                        boolean flag1 = false;

                        if (Reflector.ForgeEntity_canRiderInteract.exists())
                        {
                            flag1 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }

                        if (!flag1 && entity1 == entity.ridingEntity)
                        {
                            if (d2 == 0.0D)
                            {
                                pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        }
                        else
                        {
                            pointedEntity = entity1;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > reach)
            {
                pointedEntity = null;
                mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing)null, new BlockPos(vec33));
            }

            if (pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null))
            {
                mc.objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
            }
        }

        return pointedEntity != null && pointedEntity == target;
    }

    public static MovingObjectPosition raytraceLegit(float yaw, float pitch, float lastYaw, float lastPitch) {
        float partialTicks = mc.timer.renderPartialTicks;
        float blockReachDistance = mc.playerController.getBlockReachDistance();

        Vec3 vec3 = mc.thePlayer.getPositionEyes(partialTicks);

        float f = lastPitch + (pitch - lastPitch) * partialTicks;
        float f1 = lastYaw + (yaw - lastYaw) * partialTicks;
        Vec3 vec31 = mc.thePlayer.getVectorForRotation(f, f1);

        Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);

        return mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
    }
}
