package io.justme.lavender.ui.elements.impl.arraylist.circle.components.impl;

import io.justme.lavender.module.Module;
import io.justme.lavender.ui.elements.impl.arraylist.circle.AbstractGroup;
import io.justme.lavender.ui.elements.impl.arraylist.circle.components.AbstractComponent;
import io.justme.lavender.utility.gl.ColorUtility;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.gl.RenderUtility;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/13
 **/
public class CircleComponent extends AbstractComponent {
    public CircleComponent(AbstractGroup abstractGroup, Module module) {
        super(abstractGroup,module);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        OGLUtility.scale(getX(),getY(),getPopUpAnimation().getValue(),() -> {
            RenderUtility.drawRoundRect(
                    getX(),
                    getY(),
                    getWidth() ,
                    getHeight(),
                    7,
                    new Color(16, 16, 16, 255));

            var name = getModule().getName();
            getFontDrawer().drawString(
                    name, getX() , getY() ,
                    ColorUtility.fadeBetween(getIndex(), 10, new Color(255, 65, 72).getRGB(), new Color(123, 113, 255).getRGB()));

            setHeight(getFontDrawer().getHeight() /2f + 3);
            setWidth(getFontDrawer().getStringWidth(name));
        });

        if (isDragging()) {
            getPopUpAnimation().animate(0.8f, 0.1f);
        }

        getPopUpAnimation().update();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if (isHover(mouseX, mouseY)) {
            return true;
        }

        return false;

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
