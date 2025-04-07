package io.justme.lavender.module.impl.legit.fight;

import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import net.minecraft.client.Minecraft;

/**
 * @author JustMe.
 * @since 2025/4/7
 **/

@Getter
@ModuleInfo(name = "Reach", description = "Reach.", category = Category.FIGHT)
public class Reach extends Module {

    private final NumberValue range = new NumberValue("Range", 3.0, 3.0, 6.0, 0.1);





}
