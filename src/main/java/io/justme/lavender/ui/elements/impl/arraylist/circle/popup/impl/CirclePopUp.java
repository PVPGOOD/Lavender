package io.justme.lavender.ui.elements.impl.arraylist.circle.popup.impl;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.elements.impl.arraylist.circle.popup.AbstractPopUp;
import io.justme.lavender.utility.gl.ColorUtility;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/24
 **/
@Getter
public class CirclePopUp extends AbstractPopUp {

    public CirclePopUp(Module module) {
        super(module);
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        RenderUtility.drawRoundRect(
                getX(),
                getY(),
                getWidth() ,
                getHeight(),
                7,
                new Color(255, 200, 222, 255));

        String name = getModule().getName();
        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        fontDrawer.drawString(
                name, getX() , getY() ,
                ColorUtility.fadeBetween(getIndex(), 10, new Color(255, 65, 72).getRGB(), new Color(123, 113, 255).getRGB()));

        setHeight(fontDrawer.getHeight() /2f + 3);
        setWidth(fontDrawer.getStringWidth(name));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        return isHover(mouseX, mouseY);
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        return isHover(mouseX, mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
