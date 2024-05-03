package io.justme.lavender.ui.screens.notifacation.impl;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.notifacation.AbstractNotifications;
import io.justme.lavender.utility.gl.RenderUtility;
import net.minecraft.client.Minecraft;

import java.awt.*;

/**
 * @author JustMe.
 * @since 2024/4/6
 **/
public class RectangleNotifications extends AbstractNotifications {

    public RectangleNotifications(){

    }

    @Override
    public void draw() {

        RenderUtility.drawRect(getX(),getY(),getWidth(),getHeight(),new Color(0));

        var titleFontRenderer = Minecraft.getMinecraft().fontRendererObj;
        var subTitleFontRenderer = Minecraft.getMinecraft().fontRendererObj;

        Color color;

        switch (getNotificationsEnum()) {
            case SUCCESS -> color = new Color(0, 255, 0);
            case INFO -> color = new Color(0, 75, 250);
            case FAIL ->  color = new Color(255,0,0);
            case WARNING -> color = new Color(255, 255, 0);
            case NONE ->  color = new Color(255,255,255);
            default -> color = new Color(255,255,255);
        }

        titleFontRenderer.drawString(getTitle(), (int) (getX() + 5), (int) (getY() + 5),color.getRGB());
        subTitleFontRenderer.drawString(getSubTitle(), (int) (getX() + 34), (int) (getY() + 15),new Color(255,255,255,64).getRGB());

        setWidth(titleFontRenderer.getStringWidth(getTitle()) + subTitleFontRenderer.getStringWidth(getSubTitle()) + 50);
        setHeight(30);
    }
}
