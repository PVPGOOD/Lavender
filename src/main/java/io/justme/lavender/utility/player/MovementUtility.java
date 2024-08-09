package io.justme.lavender.utility.player;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

/**
 * @author JustMe.
 * @since 2024/8/8
 **/
@UtilityClass
public class MovementUtility {

    public double predictedMotion(final double motion, final int ticks) {
        if (ticks == 0) return motion;
        double predicted = motion;

        for (int i = 0; i < ticks; i++) {
            predicted = (predicted - 0.04) * 0.98F;
        }

        return predicted;
    }

    public void strafe() {
        strafe(speed());
    }

         
    public static double speed() {
        return Math.hypot(Minecraft.getMinecraft().thePlayer.motionX, Minecraft.getMinecraft().thePlayer.motionZ);
    }

    /**
     * Makes the player strafe at the specified speed
     */
    public void strafe(final double speed) {
        final double yaw = direction();
        Minecraft.getMinecraft().thePlayer.motionX = -MathHelper.sin((float) yaw) * speed;
        Minecraft.getMinecraft().thePlayer.motionZ = MathHelper.cos((float) yaw) * speed;
    }



    public void strafe(final double speed, float yaw) {
        yaw = (float) Math.toRadians(yaw);
        Minecraft.getMinecraft().thePlayer.motionX = -MathHelper.sin(yaw) * speed;
        Minecraft.getMinecraft().thePlayer.motionZ = MathHelper.cos(yaw) * speed;
    }

    public static double direction() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;

        if (Minecraft.getMinecraft().thePlayer.moveForward < 0) {
            rotationYaw += 180;
        }

        float forward = 1;

        if (Minecraft.getMinecraft().thePlayer.moveForward < 0) {
            forward = -0.5F;
        } else if (Minecraft.getMinecraft().thePlayer.moveForward > 0) {
            forward = 0.5F;
        }

        if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0) {
            rotationYaw -= 70 * forward;
        }

        if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0) {
            rotationYaw += 70 * forward;
        }

        return Math.toRadians(rotationYaw);
    }
}
