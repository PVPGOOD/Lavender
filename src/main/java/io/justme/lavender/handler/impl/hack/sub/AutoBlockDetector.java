package io.justme.lavender.handler.impl.hack.sub;

import io.justme.lavender.handler.impl.hack.AbstractHackDetection;
import io.justme.lavender.handler.impl.hack.HackType;
import net.minecraft.entity.player.EntityPlayer;

public class AutoBlockDetector extends AbstractHackDetection {

    public AutoBlockDetector() {
        super(HackType.AUTO_BLOCK);
    }

    private int blockingTime;

    @Override
    public void onUpdate(EntityPlayer player) {
        if (player.isBlocking()) ++blockingTime;
        else blockingTime = 0;
        if (blockingTime > 5 && player.isSwingInProgress) {
            flag(player, "autoblock");
        }
    }
}