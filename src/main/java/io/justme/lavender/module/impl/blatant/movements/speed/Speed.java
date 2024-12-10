package io.justme.lavender.module.impl.blatant.movements.speed;

import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventMove;
import io.justme.lavender.events.player.EventStrafe;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.ModeValue;
import net.lenni0451.asmevents.event.EventTarget;

@ModuleInfo(name = "Speed", description = "speed.", category = Category.MOVEMENTS)
public class Speed extends Module {

    public final ModeValue mode = new ModeValue("Mode", new String[]{"Watchdog","NCPHop","WatchdogLowHop"}, "Watchdog");

    public final BoolValue fastStop = new BoolValue("Fast Stop", true);

    public Speed() {
        AbstractSpeed.onInitialization();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        var abstractSpeed = AbstractSpeed.find(mode.getValue());
        abstractSpeed.onEnable();
    }

    @Override
    public void onDisable(){
        super.onDisable();
        var abstractSpeed = AbstractSpeed.find(mode.getValue());
        abstractSpeed.onDisable();
    }

    @EventTarget
    public void onEvent(EventMotionUpdate event) {
        var abstractSpeed = AbstractSpeed.find(mode.getValue());
        abstractSpeed.onMotionUpdate(event);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        var abstractSpeed = AbstractSpeed.find(mode.getValue());
        abstractSpeed.onUpdate(event);
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        var abstractSpeed = AbstractSpeed.find(mode.getValue());
        abstractSpeed.onPacket(event);
    }

    @EventTarget
    public void onMove(EventMove event) {
        var abstractSpeed = AbstractSpeed.find(mode.getValue());
        abstractSpeed.onMove(event);
    }

    @EventTarget
    public void onStrafe(EventStrafe event) {
        var abstractSpeed = AbstractSpeed.find(mode.getValue());
        abstractSpeed.onStrafe(event);
    }
}
