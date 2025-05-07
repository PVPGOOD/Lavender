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
import net.minecraft.potion.Potion;

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

        if ( mc.currentScreen != null) return;

        var player = Minecraft.getMinecraft().thePlayer;

        float f = 0.8F;

        boolean flag3 = (float)player.getFoodStats().getFoodLevel() > 6.0F || player.capabilities.allowFlying;
        if (!player.isSprinting() && player.movementInput.moveForward >= f && flag3 && !player.isUsingItem() && !player.isPotionActive(Potion.blindness))
        {
            player.setSprinting(true);
        }
    }
}
