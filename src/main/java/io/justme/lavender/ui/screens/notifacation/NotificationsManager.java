package io.justme.lavender.ui.screens.notifacation;

import io.justme.lavender.ui.screens.notifacation.impl.RectangleNotifications;
import io.justme.lavender.utility.system.SoundUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2024/4/6
 **/

@Getter
@Setter
public class NotificationsManager {

    private ArrayList<AbstractNotifications> notifications = new ArrayList<>();

    public void push(String title,String subTitle,NotificationsEnum notificationsEnum,long duration) {
        var roundedNotifications = new RectangleNotifications();
        roundedNotifications.setTitle(title);
        roundedNotifications.setSubTitle(subTitle);
        roundedNotifications.setNotificationsEnum(notificationsEnum);
        roundedNotifications.setDuration(duration);

        getNotifications().add(roundedNotifications);
        if (Minecraft.getMinecraft().thePlayer != null) {
            SoundUtility.playSound("notification.wav",-20);
        }
    }

}
