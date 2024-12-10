package io.justme.lavender.module.impl.blatant.visual;

import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;

/**
 * @author JustMe.
 * @since 2024/4/30
 **/

//ItemRenderer scale , xyz , style
//EntityLivingBase getArmSwingAnimationEnd swingSpeed
@Getter
@ModuleInfo(name = "BlockStyle", description = "BlockStyle.", category = Category.VISUAL)
public class BlockStyle extends Module {

    private final ModeValue
            style = new ModeValue("Style", new String[]{"1.7", "Type1","Type2","Type3","None"}, "None");

    private final NumberValue
            posX = new NumberValue("X", 0, -1, 1.0, 1),
            posY = new NumberValue("Y", 0, -1, 1.0, 1),
            posZ = new NumberValue("Z", 0, -1, 1.0, 1),
            swingSpeed = new NumberValue("SwingSpeed", 1.0, 0.1, 1.0, 0.1),
            scale = new NumberValue("scale", 1, 0.1, 1.0, 1.0);

    @Override
    public void onEnable() {
        super.onEnable();
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }
}
