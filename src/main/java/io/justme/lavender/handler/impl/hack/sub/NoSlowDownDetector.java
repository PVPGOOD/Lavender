package io.justme.lavender.handler.impl.hack.sub;

import io.justme.lavender.handler.impl.hack.AbstractHackDetection;
import io.justme.lavender.handler.impl.hack.HackType;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author JustMe.
 * @since 2025/5/24
 **/
public class NoSlowDownDetector extends AbstractHackDetection {

    private int sprintBuffer = 0, motionBuffer = 0;


    public NoSlowDownDetector() {
        super(HackType.NO_SLOWDOWN);
    }

    @Override
    public void onUpdate(EntityPlayer player) {
        if (player.isUsingItem() || player.isBlocking()) {
            if (player.isSprinting()) {
                if (++sprintBuffer > 5) {
                    flag(player, "Sprinting when using item or blocking");
                }
                return;
            }
            // a motion check
            double dx = player.prevPosX - player.posX, dz = player.prevPosZ - player.posZ;
            if (dx * dx + dz * dz > 0.07) { // sq: 0.25
                if (++motionBuffer > 10 && player.hurtTime == 0) {
                    flag(player, "Not sprinting but keep in sprint motion when blocking");
                    motionBuffer = 7;
                    return;
                }
            }
            motionBuffer -= (motionBuffer > 0 ? 1 : 0);
            sprintBuffer -= (sprintBuffer > 0 ? 1 : 0);
        }
    }
}
