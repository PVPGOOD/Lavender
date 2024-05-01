package io.justme.lavender.module.impl.player;

import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;

import java.util.Objects;

/**
 * @author JustMe.
 * @since 2024/4/14
 **/

@Getter
@Setter
@ModuleInfo(name = "FastPlace", description = "fast place.", category = Category.PLAYER)
public class FastPlace extends Module {

    private final ModeValue
            clickEnumModeValue = new ModeValue("Mode", new String[]{"Normal", "delay"}, "delay");

    private final NumberValue
            delayMs = new NumberValue("Click Delay", 0, 0, 500, 5, () -> Objects.equals(getClickEnumModeValue().getValue(), "Delay"));

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Minecraft.getMinecraft().setRightClickDelayTimer(4);
        super.onDisable();
    }

    private TimerUtility timerUtility = new TimerUtility();
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {

        switch (getClickEnumModeValue().getValue()) {
            case "Normal" -> Minecraft.getMinecraft().setRightClickDelayTimer(0);

            case "delay" -> {
                // no things here...
            }
        }
    }
}
