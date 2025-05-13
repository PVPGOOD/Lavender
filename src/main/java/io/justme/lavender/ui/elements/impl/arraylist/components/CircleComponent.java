package io.justme.lavender.ui.elements.impl.arraylist.components;

import io.justme.lavender.module.Module;
import io.justme.lavender.ui.elements.impl.arraylist.AbstractArraylist;
import io.justme.lavender.utility.gl.ColorUtility;
import io.justme.lavender.utility.gl.RenderUtility;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/13
 **/
public class CircleComponent extends AbstractArraylist {
    public CircleComponent(Module module) {
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
                new Color(16, 16, 16, 255));

        String name = module.getName();
        getFontDrawer().drawString(
                name, getX() + getWidth() /2f - getFontDrawer().getStringWidth(name)/2f , getY() + getHeight() / 2f - getFontDrawer().getHeight() / 2f + 3,
                ColorUtility.fadeBetween(getIndex(), 10, new Color(255, 65, 72).getRGB(), new Color(123, 113, 255).getRGB()));


    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        return isHover(mouseX, mouseY);

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 1 && isHover(mouseX, mouseY)) {
            getModule().setStatus(false);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
