package io.justme.lavender.ui.elements.impl;

import io.justme.lavender.La;
import io.justme.lavender.ui.elements.AbstractElements;
import io.justme.lavender.ui.elements.ElementsEnum;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/4/6
 **/

@Getter
@Setter
public class Notifications extends AbstractElements {

    private float x,y,width,height;
    private boolean dragging;

    public Notifications() {
        super(ElementsEnum.Notification);
    }

    private final TimerUtility timerUtility = new TimerUtility();

    private float interval;
    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {

        var scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        boolean bottom = getPosY() < scaledResolution.getScaledHeight() / 2f;

        var interval = 0;

        for (int i = 0; i < La.getINSTANCE().getNotificationsManager().getNotifications().size(); i++) {
            var abstractNotifications = La.getINSTANCE().getNotificationsManager().getNotifications().get(i);

            if (abstractNotifications.getTimer().hasTimeElapsed(abstractNotifications.getDuration()) && abstractNotifications.getAnimation().isDone())
                La.getINSTANCE().getNotificationsManager().getNotifications().remove(i);

            OGLUtility.scale(
                        getPosX() + abstractNotifications.getWidth() / 2f,
                        getPosY() + (abstractNotifications.getHeight() / 2f) + interval,
                        abstractNotifications.getAnimation().getValue(),
                        abstractNotifications::draw);

                interval += (bottom ? 40 : -40);
                setInterval(interval);

            abstractNotifications.setX(getPosX());
            abstractNotifications.setY(getPosY() + interval);
            abstractNotifications.getAnimation().animate(abstractNotifications.getTimer().hasTimeElapsed(abstractNotifications.getDuration()) ? 0 : 1,.1F, Easings.LINEAR);
            abstractNotifications.getAnimation().update();
        }

        setX(getPosX());
        setY(getPosY() + (bottom ? 0 : getInterval() + 30));
        setWidth(100);
        setHeight(bottom ? getInterval() : -getInterval());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            if (MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY)) {
                setDragging(true);
            }

        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) setDragging(false);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
