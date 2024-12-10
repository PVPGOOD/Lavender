package io.justme.lavender.module.impl.blatant.movements.noslowdown;

import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.game.EventWorldReload;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventSlowDown;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.ModeValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;

/**
 * @author JustMe.
 * @since 2024/12/8
 **/

@Getter
@ModuleInfo(name = "NoSlowDown", description = "", category = Category.MOVEMENTS)
public class NoSlowDown extends Module {

    private final ModeValue mode = new ModeValue("Mode", new String[]{"WatchDog","Vanilla"}, "WatchDog");

    public NoSlowDown() {
        AbstractNoSlowDown.onInitialization();
    }

    @Override
    public void onEnable() {
        var abstractNoSlowDown = AbstractNoSlowDown.find(getMode().getValue());
        abstractNoSlowDown.onEnable();
        super.onEnable();
    }


    @Override
    public void onDisable() {
        var abstractNoSlowDown = AbstractNoSlowDown.find(getMode().getValue());
        abstractNoSlowDown.onDisable();
        super.onDisable();
    }

    @EventTarget
    public void onSlowDown(EventSlowDown event) {

        var abstractNoSlowDown = AbstractNoSlowDown.find(getMode().getValue());
        abstractNoSlowDown.onNoSlowDown(event);
    }


    @EventTarget
    public void onTicks(EventTick eventTick) {
        var abstractNoSlowDown = AbstractNoSlowDown.find(getMode().getValue());
        abstractNoSlowDown.onTick(eventTick);
    }

    @EventTarget
    public void onMotionEvent(EventMotionUpdate event) {
        var abstractNoSlowDown = AbstractNoSlowDown.find(getMode().getValue());
        abstractNoSlowDown.onMotionUpdate(event);
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        var abstractNoSlowDown = AbstractNoSlowDown.find(getMode().getValue());
        abstractNoSlowDown.onPacket(eventPacket);
    }

    @EventTarget
    public void onReload(EventWorldReload eventWorldReload){
        var abstractNoSlowDown = AbstractNoSlowDown.find(getMode().getValue());
        abstractNoSlowDown.onWorldReload(eventWorldReload);
    }


}
