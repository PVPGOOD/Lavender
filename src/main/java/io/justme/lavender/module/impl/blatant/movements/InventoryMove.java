package io.justme.lavender.module.impl.blatant.movements;

import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

/**
 * @author JustMe.
 * @since 2024/8/10
 **/

@ModuleInfo(name = "InventoryMove", description = " ", category = Category.MOVEMENTS)
public class InventoryMove extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }

    private final KeyBinding[] affectedBindings = new KeyBinding[]{
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindJump
    };

    @EventTarget
    public void onMotion(EventMotionUpdate event) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
            for (final KeyBinding a : affectedBindings) {
                a.pressed = (GameSettings.isKeyDown(a));
            }
        }
    }

}
