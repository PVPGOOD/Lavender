package io.justme.lavender.module.impl.blatant.movements.speed.impl;

import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventMove;
import io.justme.lavender.events.player.EventStrafe;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.impl.blatant.movements.speed.AbstractSpeed;
import io.justme.lavender.utility.player.PlayerUtility;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

public class NCPHopSpeed extends AbstractSpeed {

    private boolean reset;
    private static double speed;
    private final double WALK_SPEED = 0.221;
    private final double MOD_SWIM = 0.115F / WALK_SPEED;
    private final double[] MOD_DEPTH_STRIDER = {
            1.0F,
            0.1645F / MOD_SWIM / WALK_SPEED,
            0.1995F / MOD_SWIM / WALK_SPEED,
            1.0F / MOD_SWIM,
    };


    public NCPHopSpeed() {
        super("NCPHop");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onPacket(EventPacket event) {

    }

    private int offGroundTicks;
    @Override
    public void onMotionUpdate(EventMotionUpdate event) {
        if (!PlayerUtility.moving()) {
            event.setX(event.getX() + (Math.random() - 0.5) / 100);
            event.setZ(event.getZ() + (Math.random() - 0.5) / 100);
        }

    }

    @Override
    public void onUpdate(EventUpdate event) {
        if (Minecraft.getMinecraft().thePlayer.onGround) {
            offGroundTicks = 0;
        } else {
            offGroundTicks ++;
        }
    }

    @Override
    public void onMove(EventMove event) {

    }

    @Override
    public void onStrafe(EventStrafe event) {
        final double base = getAllowedHorizontalDistance();
        final boolean potionActive = mc.thePlayer.isPotionActive(Potion.moveSpeed);

        if (PlayerUtility.moving()) {
            double BUNNY_FRICTION = 159.9F;
            switch (offGroundTicks) {
                case 0:
                    mc.thePlayer.motionY = jumpBoostMotion(0.42f);
                    speed = base * (potionActive ? 2 : 2.15);
                    break;

                case 1:
                    speed -= 0.8 * (speed - base);
                    break;

                default:
                    speed -= speed / BUNNY_FRICTION;
                    break;
            }

            reset = false;
        } else if (!reset) {
            speed = 0;

            reset = true;
            speed = getAllowedHorizontalDistance();
        }

        if (mc.thePlayer.isCollidedHorizontally) {
            speed = getAllowedHorizontalDistance();
        }

        event.setSpeed(Math.max(speed, base), Math.random() / 2000);
    }

    public double getAllowedHorizontalDistance() {
        double horizontalDistance;
        boolean useBaseModifiers = false;

        if (mc.thePlayer.isInWeb) {
            double MOD_WEB = 0.105 / WALK_SPEED;
            horizontalDistance = MOD_WEB * WALK_SPEED;
        } else if (PlayerUtility.isInLiquid()) {
            horizontalDistance = MOD_SWIM * WALK_SPEED;

            final int depthStriderLevel = depthStriderLevel();
            if (depthStriderLevel > 0) {
                horizontalDistance *= MOD_DEPTH_STRIDER[depthStriderLevel];
                useBaseModifiers = true;
            }

        } else if (mc.thePlayer.isSneaking()) {
            double MOD_SNEAK = 0.3F;
            horizontalDistance = MOD_SNEAK * WALK_SPEED;
        } else {
            horizontalDistance = WALK_SPEED;
            useBaseModifiers = true;
        }

        if (useBaseModifiers) {
            if (canSprint(false)) {
                double MOD_SPRINTING = 1.3F;
                horizontalDistance *= MOD_SPRINTING;
            }



            if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getDuration() > 0) {
                horizontalDistance *= 1 + (0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1));
            }

            if (mc.thePlayer.isPotionActive(Potion.moveSlowdown)) {
                horizontalDistance = 0.29;
            }
        }

        final Block below = blockRelativeToPlayer(0, -1, 0);
        if (below == Blocks.ice || below == Blocks.packed_ice) {
            horizontalDistance *= 1.2;
        }

        return horizontalDistance;
    }

    public double jumpBoostMotion(final double motionY) {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return motionY + (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
        }

        return motionY;
    }

    public Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).add(offsetX, offsetY, offsetZ)).getBlock();
    }
    public boolean canSprint(final boolean legit) {
        return (legit ? mc.thePlayer.moveForward >= 0.8F
                && !mc.thePlayer.isCollidedHorizontally
                && (mc.thePlayer.getFoodStats().getFoodLevel() > 6 || mc.thePlayer.capabilities.allowFlying)
                && !mc.thePlayer.isPotionActive(Potion.blindness)
                && !mc.thePlayer.isUsingItem()
                && !mc.thePlayer.isSneaking()
                : enoughMovementForSprinting());
    }

    public boolean enoughMovementForSprinting() {
        return Math.abs(mc.thePlayer.moveForward) >= 0.8F || Math.abs(mc.thePlayer.moveStrafing) >= 0.8F;
    }

    public int depthStriderLevel() {
        return EnchantmentHelper.getDepthStriderModifier(mc.thePlayer);
    }

}
