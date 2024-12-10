package io.justme.lavender.module.impl.blatant.movements;

import io.justme.lavender.events.player.EventSafeWalk;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import net.lenni0451.asmevents.event.EventTarget;

/**
 * @author JustMe.
 * @since 2024/4/29
 **/
@ModuleInfo(name = "SafeWalk", description = "SafeWalk.", category = Category.MOVEMENTS)
public class SafeWalk extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
    }
    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onSafeWalk(EventSafeWalk walk) {
        walk.setCancel(true);
    }

}
