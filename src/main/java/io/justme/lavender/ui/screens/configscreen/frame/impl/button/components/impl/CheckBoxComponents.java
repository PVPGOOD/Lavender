package io.justme.lavender.ui.screens.configscreen.frame.impl.button.components.impl;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.configscreen.frame.impl.AbstractComponents;
import io.justme.lavender.ui.screens.configscreen.frame.impl.button.components.ComponentsEnum;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

@Getter
@Setter
public class CheckBoxComponents extends AbstractComponents {
    private FontDrawer fontRenderer = La.getINSTANCE().getFontManager().getPingFang_Bold18();


    public CheckBoxComponents(String name) {
        super(name);

        setType(ComponentsEnum.CHECKBOX);
    }

    @Override
    public void initGui() {

    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        var hovering = MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY);

        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),6,new Color(253, 147, 189, hovering ? 255 : 200));
        getFontRenderer().drawString(getName(),getX() + (getWidth() /2f) - (getFontRenderer().getStringWidth(getName()) /2f), getY() + (getFontRenderer().getHeight() / 2f - 4),new Color(255,255,255).getRGB());

        setWidth(55);
        setHeight(20);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        return MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
