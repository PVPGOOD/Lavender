package io.justme.lavender.ui.elements.impl;

import io.justme.lavender.La;
import io.justme.lavender.ui.elements.AbstractElement;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/24
 **/
@Getter
@Setter
public class HackDetectionElement extends AbstractElement {

    private float x,y,width,height;


    public HackDetectionElement() {
        super("HackDetection Element");
    }

    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {

        RenderUtility.drawRoundRect(getPosX(), getPosY(), getWidth(), getHeight(), 5, new Color(0xFF000000));

        int intervalY = 10;
        int maxWidth = 0;
        for (var entry : La.getINSTANCE().getHandlerManager().getHackDetectionHandler().getUsingModuleArraylist().entrySet()) {
            var player = entry.getKey();
            var hackTypes = entry.getValue();
            String playerText = player.getName();
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                    playerText,
                    getX(), getY() + intervalY, 0xFFFFFF
            );
            maxWidth = Math.max(maxWidth, Minecraft.getMinecraft().fontRendererObj.getStringWidth(playerText));
            intervalY += 10;

            // 绘制对应的 HackType
            for (var hackType : hackTypes) {
                String hackTypeText = " - HackType: " + hackType.name() + " (" + hackType.getName() + ")";
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                        hackTypeText,
                        getX(), getY() + intervalY, 0xFF5555
                );
                maxWidth = Math.max(maxWidth, Minecraft.getMinecraft().fontRendererObj.getStringWidth(hackTypeText));
                intervalY += 10;
            }
        }

        setX(getPosX());
        setY(getPosY());
        setHeight(intervalY + 10);
        setWidth(maxWidth + 10);
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
