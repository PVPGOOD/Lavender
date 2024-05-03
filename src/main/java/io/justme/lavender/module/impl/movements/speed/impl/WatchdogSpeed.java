package io.justme.lavender.module.impl.movements.speed.impl;

import io.justme.lavender.La;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventMove;
import io.justme.lavender.events.player.EventStrafe;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.impl.movements.speed.AbstractSpeed;
import io.justme.lavender.module.impl.movements.speed.Speed;
import io.justme.lavender.utility.player.PlayerUtility;
import net.minecraft.potion.Potion;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/
public class WatchdogSpeed extends AbstractSpeed {
    public WatchdogSpeed() {
        super("Watchdog");
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

    @Override
    public void onMotionUpdate(EventMotionUpdate event) {

    }

    @Override
    public void onUpdate(EventUpdate event) {
        
    }

    @Override
    public void onMove(EventMove event) {

        var speed = ((Speed) La.getINSTANCE().getModuleManager().getModuleByName("Speed"));

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
            if (speed.fastStop.getValue()) mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        }

        if (mc.thePlayer.isCollidedHorizontally && PlayerUtility.moving() && mc.thePlayer.onGround) {
            PlayerUtility.setMotion(PlayerUtility.getBaseMoveSpeed());
        }
    }

    @Override
    public void onStrafe(EventStrafe event) {

    }
}
