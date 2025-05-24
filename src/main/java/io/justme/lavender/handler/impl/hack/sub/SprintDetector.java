package io.justme.lavender.handler.impl.hack.sub;

import io.justme.lavender.handler.impl.hack.AbstractHackDetection;
import io.justme.lavender.handler.impl.hack.HackType;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author JustMe.
 * @since 2025/5/23
 **/
public class SprintDetector extends AbstractHackDetection {

    public SprintDetector() {
        super(HackType.OMNI_SPRINT);
    }

    @Override
    public void onUpdate(EntityPlayer player) {
        if (player.isSprinting() && (player.moveForward < 0.0f || player.moveForward == 0.0f && player.moveStrafing != 0.0f)) {
            flag(player,"疑似 全疾跑" );
        }
    }
}
