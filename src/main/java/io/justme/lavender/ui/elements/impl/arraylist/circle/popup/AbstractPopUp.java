package io.justme.lavender.ui.elements.impl.arraylist.circle.popup;

import io.justme.lavender.La;
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

//这里负责规范化每个Group的基本属性和方法
public abstract class AbstractPopUp {

    private float x,y,width,height;
    private boolean dragging;
    private float draggingX, draggingY;
    private float index;
    private FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium18();
    private Module module;

    public AbstractPopUp(Module module) {
        this.module = module;
    }

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void draw(int mouseX, int mouseY);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract boolean mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
}
