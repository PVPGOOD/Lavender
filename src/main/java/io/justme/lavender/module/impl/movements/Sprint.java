package io.justme.lavender.module.impl.movements;

import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import net.minecraft.client.Minecraft;

/**
 * @author JustMe.
 * @since 2024/4/28
 **/

@ModuleInfo(name = "Speed", description = "IDK.", category = Category.MOVEMENTS)
public class Sprint extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
        Minecraft.getMinecraft().thePlayer.setSprinting(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.getMinecraft().thePlayer.setSprinting(false);
    }

}
