package io.justme.lavender.utility.world;

import io.justme.lavender.utility.interfaces.IMinecraft;
import lombok.experimental.UtilityClass;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

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
}
