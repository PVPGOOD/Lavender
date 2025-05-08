package io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/7
 **/

@Getter
@Setter

//管理三个类型的按钮
//hamburger setting category
public abstract class AbstractNavBar {

    public float x,y,width,height;
    public NavBarType type;
    public int requestHeight = 0;

    public AbstractNavBar(NavBarType type) {
        this.type = type;
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
