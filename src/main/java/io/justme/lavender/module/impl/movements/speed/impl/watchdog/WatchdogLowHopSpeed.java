package io.justme.lavender.module.impl.movements.speed.impl.watchdog;

import io.justme.lavender.La;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventMove;
import io.justme.lavender.events.player.EventStrafe;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.impl.movements.speed.AbstractSpeed;
import io.justme.lavender.module.impl.movements.speed.Speed;
import io.justme.lavender.utility.player.MovementUtility;
import io.justme.lavender.utility.player.PlayerUtility;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

/**
 * @author JustMe.
 * @since 2024/5/10
 **/
public class WatchdogLowHopSpeed extends AbstractSpeed {
    public WatchdogLowHopSpeed() {
        super("WatchdogLowHop");
    }
    
    private boolean collided;
    private int stage;
    private int stair;
    private double less;
    private boolean lessSlow;
    private double speed;

    private boolean a = false;


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
        if (mc.thePlayer.onGround) {
            offGroundTicks = 0;
        } else {
            offGroundTicks++;
        }
    }

    @Override
    public void onMove(EventMove event) {

    }
    public int offGroundTicks = 0;
    @Override
    public void onStrafe(EventStrafe event) {


        switch (offGroundTicks) {
            case 0: {
                //auto jump
                if (PlayerUtility.moving()) {
                    La.getINSTANCE().print("Jump!");
                    mc.thePlayer.motionY = 0.42;
                }
                break;
            }
            case 8:
                var speed = predictedMotion(mc.thePlayer.motionY, 3);
                La.getINSTANCE().print("Filled! " +  speed);
                mc.thePlayer.motionY = speed;
                break;
        }
    }

    private double getHypixelSpeed(int stage) {
        double value = 0.26 / 15;
        double firstValue = 0.4145 / 12.5;
        double decr = (((double) stage / 500) * 2);

        if (stage == 0) {
            value = 0.64 * 0.134;
        } else if (stage == 1) {
            value = firstValue;
        } else if (stage >= 2) {
            value = firstValue - decr;
        }
        if (collided) {
            value = 0.2;
            if (stage == 0)
                value = 0;
        }

        return Math.max(value, 0.26);
    }

    public static double predictedMotion(final double motion, final int ticks) {
        return MovementUtility.predictedMotion(motion, ticks);
    }

    private boolean noAction() {
        return ((mc.thePlayer.isInWater()
                || mc.thePlayer.isInLava()))
                || (mc.thePlayer.isSneaking());
    }
}
