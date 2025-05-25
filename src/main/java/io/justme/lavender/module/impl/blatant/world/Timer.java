package io.justme.lavender.module.impl.blatant.world;

import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;

/**
 * @author JustMe.
 * @since 2025/5/25
 **/

@Getter
@ModuleInfo(name = "Timer", description = "Timer.", category = Category.World)
public class Timer extends Module {

    private final NumberValue numberValue = new NumberValue("Timer Speed", 0.2, 0.1, 10, 0.1, () -> true);


    @Override
    public void onEnable() {
        super.onEnable();

    }

    @EventTarget
    public void onTick(EventTick event) {
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        mc.timer.timerSpeed = getNumberValue().getValue();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        mc.timer.timerSpeed = 1f;
    }

}
