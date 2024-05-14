package io.justme.lavender.utility.player;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

@UtilityClass
public class PlayerUtility  {

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
}
