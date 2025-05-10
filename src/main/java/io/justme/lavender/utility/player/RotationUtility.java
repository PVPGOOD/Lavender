package io.justme.lavender.utility.player;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.*;
import org.lwjglx.input.Keyboard;
import org.lwjglx.util.vector.Vector2f;

@UtilityClass
public class RotationUtility {

    private final Minecraft mc = Minecraft.getMinecraft();

    public float[] getRotationToEntity(Entity entity) {
        double pX = mc.thePlayer.posX;
        double pY = mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight();
        double pZ = mc.thePlayer.posZ;
        double eX = entity.posX;
        double eY = entity.posY + (double) (entity.height * 0.75);
        double eZ = entity.posZ;
        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(dX * dX + dZ * dZ);
        float yaw = (float) (Math.toDegrees(Math.atan2(dZ, dX)) + 90.0);
        float pitch = (float) Math.toDegrees(Math.atan2(dH, dY));
        return new float[]{yaw, (float) (90.0 - pitch)};
    }

    public float[] getRotationsToPosition(double x, double y, double z) {
        var deltaX = x - mc.thePlayer.posX;
        var deltaY = y - mc.thePlayer.posY - mc.thePlayer.getEyeHeight();
        var deltaZ = z - mc.thePlayer.posZ;

        var horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        var yaw = (float) Math.toDegrees(-Math.atan2(deltaX, deltaZ));
        var pitch = (float) Math.toDegrees(-Math.atan2(deltaY, horizontalDistance));

        return new float[] {yaw, pitch};
    }

    public float[] getRotationsBlock(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - mc.thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - mc.thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double y = (double)block.getY() + 0.5;
        double d1 = mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }


    public float getMoveYaw(float yaw) {
        Vector2f from = new Vector2f((float) mc.thePlayer.lastTickPosX, (float) mc.thePlayer.lastTickPosZ),
                to = new Vector2f((float) mc.thePlayer.posX, (float) mc.thePlayer.posZ),
                diff = new Vector2f(to.x - from.x, to.y - from.y);

        double x = diff.x, z = diff.y;
        if (x != 0 && z != 0) {
            yaw = (float) Math.toDegrees((Math.atan2(-x, z) + MathHelper.PI2) % MathHelper.PI2);
        }
        return yaw;
    }


    public Vector2f calculate(final Vector3d from, final Vector3d to) {

        final Vector3d diff = to.subtract(from);
        final double distance = Math.hypot(diff.getX(), diff.getZ());
        final float yaw = (float) (MathHelper.atan2(diff.getZ(), diff.getX()) * MathHelper.TO_DEGREES) - 90.0F;
        final float pitch = (float) (-(MathHelper.atan2(diff.getY(), distance) * MathHelper.TO_DEGREES));
        return new Vector2f(yaw, pitch);
    }

    public Vector2f calculate(final Vec3 to) {
        return calculate(mc.thePlayer.getCustomPositionVector().add(0, mc.thePlayer.getEyeHeight(), 0), new Vector3d(to.xCoord, to.yCoord, to.zCoord));
    }

    public Vector2f calculate(final Vector3d to) {
        return calculate(mc.thePlayer.getCustomPositionVector().add(0, mc.thePlayer.getEyeHeight(), 0), to);
    }

    public Vector2f calculate(final Vector3d position, final EnumFacing enumFacing) {
        double x = position.getX() + 0.5D;
        double y = position.getY() + 0.5D;
        double z = position.getZ() + 0.5D;

        x += (double) enumFacing.getDirectionVec().getX() * 0.5D;
        y += (double) enumFacing.getDirectionVec().getY() * 0.5D;
        z += (double) enumFacing.getDirectionVec().getZ() * 0.5D;
        return calculate(new Vector3d(x, y, z));
    }

    public Vector2f calculate(final Entity entity) {
        return calculate(entity.getCustomPositionVector().add(0, Math.max(0, Math.min(mc.thePlayer.posY - entity.posY +
                mc.thePlayer.getEyeHeight(), (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 0.9)), 0));
    }

    public Vector2f calculate(final Entity entity, final boolean adaptive, final double range) {
        Vector2f normalRotations = calculate(entity);
        if (!adaptive || RaycastUtility.rayCast(normalRotations, range).typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            return normalRotations;
        }

        for (double yPercent = 1; yPercent >= 0; yPercent -= 0.25) {
            for (double xPercent = 1; xPercent >= -0.5; xPercent -= 0.5) {
                for (double zPercent = 1; zPercent >= -0.5; zPercent -= 0.5) {
                    Vector2f adaptiveRotations = calculate(entity.getCustomPositionVector().add(
                            (entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX) * xPercent,
                            (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * yPercent,
                            (entity.getEntityBoundingBox().maxZ - entity.getEntityBoundingBox().minZ) * zPercent));

                    if (RaycastUtility.rayCast(adaptiveRotations, range).typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                        return adaptiveRotations;
                    }
                }
            }
        }

        return normalRotations;
    }

    public static float getYawDifference(float currentYaw, double targetX, double targetY, double targetZ) {
        double deltaX = targetX - mc.thePlayer.posX;
        double deltaY = targetY - mc.thePlayer.posY;
        double deltaZ = targetZ - mc.thePlayer.posZ;
        double yawToEntity = 0;
        double degrees = Math.toDegrees(Math.atan(deltaZ / deltaX));
        if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
            if (deltaX != 0) yawToEntity = 90.0D + degrees;
        } else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
            if (deltaX != 0) yawToEntity = -90.0D + degrees;
        } else {
            if (deltaZ != 0) yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(currentYaw - (float) yawToEntity));
    }

    @Getter
    public class Vector3d {

        private final double x;
        private final double y;
        private final double z;

        public Vector3d(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector3d add(final double x, final double y, final double z) {
            return new Vector3d(this.x + x, this.y + y, this.z + z);
        }

        public Vector3d add(final Vector3d vector) {
            return add(vector.x, vector.y, vector.z);
        }

        public Vector3d subtract(final double x, final double y, final double z) {
            return add(-x, -y, -z);
        }

        public Vector3d subtract(final Vector3d vector) {
            return add(-vector.x, -vector.y, -vector.z);
        }

        public double length() {
            return Math.sqrt(x * x + y * y + z * z);
        }

        public Vector3d multiply(final double v) {
            return new Vector3d(x * v, y * v, z * v);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Vector3d)) return false;

            Vector3d vector = (Vector3d) obj;
            return ((Math.floor(x) == Math.floor(vector.x)) && Math.floor(y) == Math.floor(vector.y) && Math.floor(z) == Math.floor(vector.z));
        }
    }
}
