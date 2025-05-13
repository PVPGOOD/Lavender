package io.justme.lavender.ui.elements.impl.arraylist;

import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.module.Module;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/13
 **/
@Getter
@Setter
public abstract class AbstractArraylist {
    private FontDrawer fontDrawer;
    private float x,y,width,height;
    private boolean dragging;
    private float draggingX, draggingY;
    private float index;
    public Module module;

    public AbstractArraylist(Module module) {
        this.module = module;
    }

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void draw(int mouseX, int mouseY);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
}
