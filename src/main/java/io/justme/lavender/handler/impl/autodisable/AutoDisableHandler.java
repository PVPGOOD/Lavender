package io.justme.lavender.handler.impl.autodisable;

import io.justme.lavender.La;
import io.justme.lavender.events.game.EventGameEnd;
import io.justme.lavender.events.game.EventWorldReload;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.handler.AbstractHandler;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import net.lenni0451.asmevents.event.EventTarget;

/**
 * @author JustMe.
 * @since 2025/5/31
 **/
public class AutoDisableHandler extends AbstractHandler {

    @EventTarget
    public void onGameEnd(EventGameEnd eventPacket) {
        disable("游戏结束");
    }

    @EventTarget
    public void onMotion(EventMotionUpdate eventMotionUpdate) {

    }

    @EventTarget
    public void onWorldReload(EventWorldReload eventWorldReload) {
        disable("重载世界");
    }

    public void disable(String s) {
        if (!La.getINSTANCE().getSettingManager().getGameEndValue().getValue()) return;

        String[] modules = {"KillAura", "Speed", "Scaffold", "InventoryCleaner", "ChestStealer","SafeWalk"};

        for (String moduleName : modules) {
            if (La.getINSTANCE().getSettingManager().getGameEndMultiValue().find(moduleName).getValue()) {
                var module = La.getINSTANCE().getModuleManager().getModuleByName(moduleName);

                if (module != null && module.isToggle()) {
                    module.setStatus(false);
                    La.getINSTANCE().getNotificationsManager().push(
                            "Auto Disable",
                            String.format(moduleName + " 已关闭 因为 %s" ,s),
                            NotificationsEnum.SUCCESS,
                            2000
                    );
                }
            }
        }
    }
}
