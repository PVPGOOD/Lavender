package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value;

import io.justme.lavender.module.Module;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/8
 **/
@Getter
@Setter
//有点多余 但是顺手了
public abstract class AbstractModuleValue {

    public Module module;
    public float x,y,width,height;
    public float requestHeight = 0;

    public AbstractModuleValue(Module module) {
        this.module = module;
    }

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract boolean mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
    public abstract void handleMouseInput() throws IOException;


}
