package io.justme.lavender.module.impl.blatant.movements;

import io.justme.lavender.La;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
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
        var scaffold = ((Scaffold) La.getINSTANCE().getModuleManager().getModuleByName("Scaffold"));

        if (scaffold.isToggle()) {
            mc.gameSettings.keyBindSprint.pressed = scaffold.getSprintValue().getValue();
        } else {
            mc.gameSettings.keyBindSprint.pressed = true;
        }

    }
}
