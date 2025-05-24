package io.justme.lavender.handler.impl.hack;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2025/5/23
 **/
@Getter
public abstract class AbstractHackDetection {


    private HackType hackType;

    public AbstractHackDetection(HackType hackType) {
        this.hackType = hackType;
    }

    public abstract void onUpdate(EntityPlayer player);

    public void flag(EntityPlayer player, String reason) {
        boolean exists = La.getINSTANCE().getHandlerManager().getHackDetectionHandler().getUsingModuleArraylist()
                .values()
                .stream()
                .anyMatch(list -> list.contains(hackType));

        if (!exists) {
            add(player, hackType);
            La.getINSTANCE().getNotificationsManager().push("HackDetection",
                    player.getName() + " 检测出 " + getHackType().name() + " - 原因: " + reason, NotificationsEnum.WARNING, 5000);
        }
    }

    public void testFlag(EntityPlayer player, String reason) {
        La.getINSTANCE().getNotificationsManager().push("HackDetection",
                player.getName() + " 检测出 " + getHackType().name() + " - 原因: " + reason, NotificationsEnum.WARNING, 5000);
    }


    public void add(EntityPlayer player, HackType hackType) {
        La.getINSTANCE().getHandlerManager().getHackDetectionHandler().getUsingModuleArraylist()
                .computeIfAbsent(player, k -> new ArrayList<>())
                .add(hackType);
    }

}
