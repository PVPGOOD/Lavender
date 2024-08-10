package io.justme.lavender.module.impl.player;

import io.justme.lavender.La;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.player.PlayerUtility;
import io.justme.lavender.value.impl.ModeValue;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.util.BlockPos;

/**
 * @author JustMe.
 * @since 2024/5/2
 **/

@Getter
@Setter
@ModuleInfo(name = "NoFall", description = "", category = Category.PLAYER)
public class NoFall extends Module {

    private final ModeValue ModeValue = new ModeValue("Mode", new String[]{"jump", "spoofGround"}, "jump");

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private boolean jumped;
    private boolean shouldJumped;

    @EventTarget
    public void onMotion(EventMotionUpdate event) {

        switch (getModeValue().getValue()) {
            case "jump" -> {
                double blockY = distanceFromGround();
                if (mc.thePlayer.fallDistance > 5 && !jumped) {
                    //差值为1
                    if (mc.thePlayer.posY - blockY <= 1) {
                        setShouldJumped(true);
                    }
                }

                if (isShouldJumped()) {
                    mc.thePlayer.motionY = 0.42f;
                    setJumped(true);
                    setShouldJumped(false);
                }

                if (PlayerUtility.isOnGround()) {
                    setJumped(false);
                }
            }

            case "spoofGround" -> {
                if (mc.thePlayer.fallDistance > 3.0f) {
                    event.setOnGround(true);
                    mc.thePlayer.fallDistance = 0.0f;
                }
            }
        }
    }

    private double distanceFromGround() {
        for (double y = mc.thePlayer.posY; y >= 0; y--) {
            if (!mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ))) {
                return y;
            }
        }
        return 0;
    }
}
