package io.justme.lavender.module.impl.blatant.movements.noslowdown.impl;

import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.game.EventWorldReload;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventSlowDown;
import io.justme.lavender.module.impl.blatant.movements.noslowdown.AbstractNoSlowDown;

/**
 * @author JustMe.
 * @since 2024/12/8
 **/
public class VanillaNoSlowDown extends AbstractNoSlowDown {
    public VanillaNoSlowDown() {
        super("Vanilla");
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
    public void onWorldReload(EventWorldReload event) {

    }

    @Override
    public void onTick(EventTick event) {

    }

    @Override
    public void onNoSlowDown(EventSlowDown eventSlowDown) {
        eventSlowDown.setCancelled(true);
    }
}
