package io.justme.lavender.ui.screens.clickgui.imgui.panels.userinfo;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/4/4
 **/
@Getter
@Setter
public abstract class AbstractUserInfoUI {

    private UserInfoUIType type;

    public AbstractUserInfoUI(UserInfoUIType type) {
        this.type = type;
    }

    public float x,y,width,height;

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
    public abstract void handleMouseInput() throws IOException;

}
