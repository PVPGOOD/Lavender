package io.justme.lavender.module.impl.player;

import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import net.lenni0451.asmevents.event.EventTarget;

/**
 * @author JustMe.
 * @since 2024/5/2
 **/

@ModuleInfo(name = "NoFall", description = "", category = Category.PLAYER)
public class NoFall extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onMotion(EventMotionUpdate event) {
        if (mc.thePlayer.fallDistance > 3.0f) {
            event.setOnGround(true);
            mc.thePlayer.fallDistance = 0.0f;
        }
    }

}
