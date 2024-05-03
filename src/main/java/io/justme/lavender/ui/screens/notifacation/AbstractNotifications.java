package io.justme.lavender.ui.screens.notifacation;

import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.math.animation.Animation;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JustMe.
 * @since 2024/4/6
 **/

@Getter
@Setter
public abstract class AbstractNotifications {

    private String title,subTitle;
    private NotificationsEnum notificationsEnum;
    private long duration;
    private float x,y,width,height;

    private final TimerUtility timer = new TimerUtility();
    private final Animation animation = new Animation();

    public abstract void draw();
}
