package io.justme.lavender.module.impl.blatant.player;

import io.justme.lavender.La;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import io.justme.lavender.value.impl.BoolValue;
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
    private final BoolValue forceEnable = new BoolValue("ForceEnable", false);

    @Override
    public void onEnable(){
        super.onEnable();

        if (!forceEnable.getValue()) {

            La.getINSTANCE().getNotificationsManager().push(
                    "Hitbox",
                    "由于该功能会造成慢狗 请勾上 forceEnable",
                    NotificationsEnum.FAIL,3000);
            setStatus(false);
        }
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }
}
