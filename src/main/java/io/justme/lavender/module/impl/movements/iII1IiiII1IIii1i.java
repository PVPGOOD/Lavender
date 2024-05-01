package io.justme.lavender.module.impl.movements;

import io.justme.lavender.events.player.EventSlowDown;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.ModeValue;
import net.lenni0451.asmevents.event.EventTarget;

@ModuleInfo(name = "NoSlow", description = "", category = Category.MOVEMENTS)
public class iII1IiiII1IIii1i extends Module {

    private final ModeValue mode = new ModeValue("Mode", new String[]{"Watchdog", "Normal"}, "Normal");

    @EventTarget
    public void onSlowDown(EventSlowDown event) {
        switch (mode.getValue()) {
            case "Normal":
                event.setCancelled(true);
                break;
        }
    }
}
