package io.justme.lavender.module.impl.blatant.fight;

import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;

/**
 * @author JustMe.
 * @since 2024/5/1
 **/

@Getter
@ModuleInfo(name = "Velocity", description = "", category = Category.FIGHT)
public class Velocity extends Module {

    public final NumberValue
            x_position = new NumberValue("X Position", 0, 0, 100, 1),
            y_position = new NumberValue("Y Position", 0, 0, 100, 1),
            z_position = new NumberValue("Z Position, ", 0, 0, 100, 1);
}
