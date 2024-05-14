package io.justme.lavender.module.impl.movements.speed.impl.watchdog;

import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventMove;
import io.justme.lavender.events.player.EventStrafe;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.impl.movements.speed.AbstractSpeed;
import io.justme.lavender.utility.player.PlayerUtility;

/**
 * @author JustMe.
 * @since 2024/5/10
 **/
public class WatchdogLowHopSpeed extends AbstractSpeed {
    public WatchdogLowHopSpeed() {
        super("WatchdogLowHop");
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

    }

    @Override
    public void onStrafe(EventStrafe event) {
        if (PlayerUtility.isOnGround()) {

        }
    }
}
