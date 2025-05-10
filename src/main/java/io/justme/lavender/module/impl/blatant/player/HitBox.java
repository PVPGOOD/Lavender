package io.justme.lavender.module.impl.blatant.player;

import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JustMe.
 * @since 2025/5/10
 **/

@Getter
@Setter
@ModuleInfo(name = "HitBox", description = "hitBox.", category = Category.PLAYER)
public class HitBox extends Module {

    private final NumberValue numberValue = new NumberValue("size",0.1, 0.1, 1.0, 0.1);


}
