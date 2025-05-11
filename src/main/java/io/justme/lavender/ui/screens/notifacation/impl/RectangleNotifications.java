package io.justme.lavender.ui.screens.notifacation.impl;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.notifacation.AbstractNotifications;
import io.justme.lavender.utility.gl.RenderUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

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
        var titleFontRenderer = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        var subTitleFontRenderer = La.getINSTANCE().getFontManager().getPingFang_Medium22();

        var scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        Color originalColor = new Color(253, 247, 255, 225); // 原始颜色
        float brightnessFactor = 1.1f; // 亮度因子，大于1增加亮度，小于1降低亮度
        int red = Math.min((int) (originalColor.getRed() * brightnessFactor), 255);
        int green = Math.min((int) (originalColor.getGreen() * brightnessFactor), 255);
        int blue = Math.min((int) (originalColor.getBlue() * brightnessFactor), 255);
        Color brightenedColor = new Color(red, green, blue, originalColor.getAlpha());
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 16, brightenedColor);
        GL11.glDisable(GL11.GL_BLEND);

        var location = 24;
        var lavender28 = La.getINSTANCE().getFontManager().getLavender28();
        float titleY = getY() + getHeight() / 4 - titleFontRenderer.getHeight() / 2f + 5;
        //icon
        lavender28.drawString(
                "z",
                getX() + location - 16,
                titleY - lavender28.getHeight()/8f, new Color(0xD949454F, true).getRGB());

        String title = getTitle();
        titleFontRenderer.drawString(
                title,
                getX() + location,
                titleY,
                new Color(0xF249454F, true).getRGB()
        );

        var subTitleY = getY() + getHeight()/2f + subTitleFontRenderer.getHeight()/2f - 8;
        var s = getSubTitle();
        subTitleFontRenderer.drawString(s,  getX() + 34 , subTitleY,new Color(73, 69, 79, 166).getRGB());

        setWidth(titleFontRenderer.getStringWidth(title) + subTitleFontRenderer.getStringWidth(s) + 16);
        setHeight(45);
    }
}
