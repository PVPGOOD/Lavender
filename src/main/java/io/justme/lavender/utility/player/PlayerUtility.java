package io.justme.lavender.utility.player;

import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

@UtilityClass
public class PlayerUtility  {

    public static final double WALK_SPEED = 0.221;
    public static final double BUNNY_SLOPE = 0.66;
    public static final double MOD_SPRINTING = 1.3F;
    public static final double MOD_SNEAK = 0.3F;
    public static final double MOD_ICE = 2.5F;
    public static final double MOD_WEB = 0.105 / WALK_SPEED;
    public static final double JUMP_HEIGHT = 0.42F;
    public static final double BUNNY_FRICTION = 159.9F;
    public static final double Y_ON_GROUND_MIN = 0.00001;
    public static final double Y_ON_GROUND_MAX = 0.0626;

    public static final double AIR_FRICTION = 0.9800000190734863D;
    public static final double WATER_FRICTION = 0.800000011920929D;
    public static final double LAVA_FRICTION = 0.5D;
    public static final double MOD_SWIM = 0.115F / WALK_SPEED;
    public static final double[] MOD_DEPTH_STRIDER = {
            1.0F,
            0.1645F / MOD_SWIM / WALK_SPEED,
            0.1995F / MOD_SWIM / WALK_SPEED,
            1.0F / MOD_SWIM,
    };

    public static final double UNLOADED_CHUNK_MOTION = -0.09800000190735147;
    public static final double HEAD_HITTER_MOTION = -0.0784000015258789;


    private final Minecraft mc = Minecraft.getMinecraft();

    public boolean moving() {
        return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
    }

    public void jump() {
        Minecraft.getMinecraft().thePlayer.jump();
    }

    public double baseSpeed() {
        var baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            double amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1 + .2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public boolean isInLiquid() {
        return mc.thePlayer.isInWater() || mc.thePlayer.isInLava();
    }

    public double getJumpBoostModifier(double height) {
        return getJumpBoostModifier(height, true);
    }

    public int getSpeedEffect() {
        return mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;
    }

    public double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            double amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1 + .2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public double getJumpBoostModifier(double height, boolean active) {
        if (mc.thePlayer.isPotionActive(Potion.jump) && active) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            height += (amplifier + 1) * .1;
        }

        return height;
    }

    public boolean isHoldingSword() {
        return mc.thePlayer != null && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    public void setMotion(double speed) {
        var forward = mc.thePlayer.moveForward;
        var strafe = mc.thePlayer.moveStrafing;
        var yaw = mc.thePlayer.rotationYaw;
        if (forward == 0 && strafe == 0) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (float) (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (float) (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            var sin = MathHelper.sin((float) Math.toRadians(yaw + 90));
            var cos = MathHelper.cos((float) Math.toRadians(yaw + 90));
            mc.thePlayer.motionX = forward * speed * cos + strafe * speed * sin;
            mc.thePlayer.motionZ = forward * speed * sin - strafe * speed * cos;
        }
    }

    public boolean holdingSword() {
        return mc.thePlayer != null && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    public boolean isOnGround(double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
    }

    public boolean isOnGround(){
        if (Minecraft.getMinecraft().thePlayer == null) return false;
        return mc.thePlayer.onGround;
    }

    public boolean isAirUnder(Entity ent) {
        return mc.theWorld.getBlockState(new BlockPos(ent.posX, ent.posY - 1, ent.posZ)).getBlock() == Blocks.air;
    }

    public void setSpeed(int speed) {
        float yaw = mc.thePlayer.rotationYaw;
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        if (forward == 0 && strafe == 0) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else {
                    forward = -1;
                }
            }
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90));
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90));
        }

    }

    public static double getAllowedHorizontalDistance() {
        return getAllowedHorizontalDistance(true);
    }

    /**
     * Basically calculates allowed horizontal distance just like NCP does
     *
     * @return allowed horizontal distance in one tick
     */
    public static double getAllowedHorizontalDistance(boolean allowSprint) {
        double horizontalDistance;
        boolean useBaseModifiers = false;

        if (mc.thePlayer.isInWeb) {
            horizontalDistance = MOD_WEB * WALK_SPEED;
        } else if (mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) {
            horizontalDistance = MOD_SWIM * WALK_SPEED;

            final int depthStriderLevel = depthStriderLevel();
            if (depthStriderLevel > 0) {
                horizontalDistance *= MOD_DEPTH_STRIDER[depthStriderLevel];
                useBaseModifiers = true;
            }

        } else if (mc.thePlayer.isSneaking()) {
            horizontalDistance = MOD_SNEAK * WALK_SPEED;
        } else {
            horizontalDistance = WALK_SPEED;
            useBaseModifiers = true;
        }

        if (useBaseModifiers) {
            if (canSprint(false) && allowSprint) {
                horizontalDistance *= MOD_SPRINTING;
            }

            if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getDuration() > 0) {
                horizontalDistance *= 1 + (0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1));
            }

            if (mc.thePlayer.isPotionActive(Potion.moveSlowdown)) {
                horizontalDistance = 0.29;
            }
        }

        return horizontalDistance;
    }

    public static int depthStriderLevel() {
        return EnchantmentHelper.getDepthStriderModifier(mc.thePlayer);
    }

    public static boolean canSprint(final boolean legit) {
        return (legit ? mc.thePlayer.moveForward >= 0.8F
                && !mc.thePlayer.isCollidedHorizontally
                && (mc.thePlayer.getFoodStats().getFoodLevel() > 6 || mc.thePlayer.capabilities.allowFlying)
                && !mc.thePlayer.isPotionActive(Potion.blindness)
                && !mc.thePlayer.isUsingItem()
                && !mc.thePlayer.isSneaking()
                : enoughMovementForSprinting());
    }

    public static boolean enoughMovementForSprinting() {
        return Math.abs(mc.thePlayer.moveForward) >= 0.8F || Math.abs(mc.thePlayer.moveStrafing) >= 0.8F;
    }

    public Block getBlock(final double x, final double y, final double z) {
        return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return getBlock(mc.thePlayer.posX + offsetX, mc.thePlayer.posY + offsetY, mc.thePlayer.posZ + offsetZ);
    }
}
