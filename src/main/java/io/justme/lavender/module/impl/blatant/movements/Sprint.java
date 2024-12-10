package io.justme.lavender.module.impl.blatant.movements;

import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.player.PlayerUtility;
import io.justme.lavender.value.impl.BoolValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;

@Getter
@ModuleInfo(name = "Sprint", description = "Auto sprint.", category = Category.MOVEMENTS)
public class Sprint extends Module {

    private final BoolValue
            all = new BoolValue("all",false);

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (Minecraft.getMinecraft().thePlayer != null) Minecraft.getMinecraft().thePlayer.setSprinting(false);
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        var back = Minecraft.getMinecraft().gameSettings.keyBindBack.pressed;
        var forward = Minecraft.getMinecraft().gameSettings.keyBindForward.pressed;
        var left = Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed;
        var right = Minecraft.getMinecraft().gameSettings.keyBindRight.pressed;

        if (getAll().getValue() ? forward || back || left || right : !(left || right || back) && PlayerUtility.moving()) {
            if (!(Minecraft.getMinecraft().thePlayer.getFoodStats().getFoodLevel() <= 6)) {
                Minecraft.getMinecraft().thePlayer.setSprinting(true);
            }
        }
    }
}
