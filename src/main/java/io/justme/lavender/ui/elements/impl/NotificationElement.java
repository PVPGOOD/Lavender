package io.justme.lavender.ui.elements.impl;

import io.justme.lavender.La;
import io.justme.lavender.ui.elements.AbstractElement;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/4/6
 **/

@Getter
@Setter
public class NotificationElement extends AbstractElement {

    private float x,y,width,height;
    private boolean dragging;

    public NotificationElement() {
        super("Notification Element");
    }

    private final TimerUtility timerUtility = new TimerUtility();

    private float interval;
    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
            RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 0, new Color(0,0,0,64));
        }
        var scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        boolean bottom = getPosY() < scaledResolution.getScaledHeight() / 2f;

        var interval = 0f;

        for (int i = 0; i < La.getINSTANCE().getNotificationsManager().getNotifications().size(); i++) {
            var abstractNotifications = La.getINSTANCE().getNotificationsManager().getNotifications().get(i);

            if (abstractNotifications.getTimer().hasTimeElapsed(abstractNotifications.getDuration() + 500) &&
                    abstractNotifications.getAnimation().isDone()) {
                La.getINSTANCE().getNotificationsManager().getNotifications().remove(i);
            }

            OGLUtility.scale(
                        getPosX() + abstractNotifications.getWidth() / 2f,
                        getPosY() + (abstractNotifications.getHeight() / 2f) + interval,
                        abstractNotifications.getAnimation().getValue(),
                        abstractNotifications::draw);

            abstractNotifications.setX(getPosX());
            abstractNotifications.setY(abstractNotifications.getPosYAnimation().getValue());

            abstractNotifications.getPosYAnimation().animate(getPosY() + interval,0.1f);
            abstractNotifications.getPosYAnimation().update();

            var isFirst = (i == 1) &&  abstractNotifications.getPosYAnimation().isDone(); // 判断是否是第一位
            abstractNotifications.getAnimation().animate(
                    isFirst || !abstractNotifications.getTimer().hasTimeElapsed(abstractNotifications.getDuration()) ? 1 : 0,
                    .1F,
                    Easings.LINEAR
            );
            abstractNotifications.getAnimation().update();

            var intervalHeight = 15;
            interval += (bottom ? abstractNotifications.getHeight() + intervalHeight : -abstractNotifications.getHeight() - intervalHeight);
            setInterval(interval);

            setWidth(abstractNotifications.getWidth());
        }

        setX(getPosX());
        setY(getPosY() + (bottom ? 0 : getInterval() + 30));
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
