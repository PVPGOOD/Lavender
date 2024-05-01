package io.justme.lavender.module.impl.movements;

import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventMove;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.player.PlayerUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.ModeValue;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.potion.Potion;

@ModuleInfo(name = "Speed", description = "speed.", category = Category.MOVEMENTS)
public class Speed extends Module {

    private final ModeValue mode = new ModeValue("Mode", new String[]{"Watchdog"}, "Watchdog");

    private final BoolValue fastStop = new BoolValue("Fast Stop", true);

    @EventTarget
    public void onEvent(EventMotionUpdate event) {
        if (PlayerUtility.moving()) {
            if (mc.thePlayer.isCollidedVertically && !PlayerUtility.isInLiquid()) {
                if (mc.thePlayer.hurtTime > 4) {
                    mc.thePlayer.motionX *= 1.007;
                    mc.thePlayer.motionZ *= 1.007;
                }

                if (mc.thePlayer.motionY < 0.1 && mc.thePlayer.motionY > 0.01) {
                    mc.thePlayer.motionX *= 1.005;
                    mc.thePlayer.motionZ *= 1.005;
                }

                if (mc.thePlayer.motionY < 0.005 && mc.thePlayer.motionY > 0) {
                    mc.thePlayer.motionX *= 1.005;
                    mc.thePlayer.motionZ *= 1.005;
                }

                if (mc.thePlayer.motionY < 0.001 && mc.thePlayer.motionY > -0.03) {
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        mc.thePlayer.motionX *= 1.005;
                        mc.thePlayer.motionZ *= 1.005;
                    } else {
                        mc.thePlayer.motionX *= 1.002;
                        mc.thePlayer.motionZ *= 1.002;
                    }
                }
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = PlayerUtility.getJumpBoostModifier(.42F);
                    PlayerUtility.setMotion((float) Math.max(PlayerUtility.getBaseMoveSpeed(), .4756F + .04F * PlayerUtility.getSpeedEffect()));
                }
            }
        } else {
            if (fastStop.getValue()) mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (mc.thePlayer.isCollidedHorizontally && PlayerUtility.moving() && mc.thePlayer.onGround && mode.getValue().equalsIgnoreCase("hypixel")) {
            PlayerUtility.setMotion(PlayerUtility.getBaseMoveSpeed());
        }
    }


}
