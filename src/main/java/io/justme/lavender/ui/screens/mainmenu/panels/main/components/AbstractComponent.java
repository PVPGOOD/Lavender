package io.justme.lavender.ui.screens.mainmenu.panels.main.components;

import io.justme.lavender.ui.screens.mainmenu.panels.main.components.impl.button.MainMenuButtonType;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/17
 **/
@Getter
@Setter
public abstract class AbstractComponent {
    private MainMenuButtonType mainMenuButtonType;

    public AbstractComponent(MainMenuButtonType mainMenuButtonType) {
        this.mainMenuButtonType = mainMenuButtonType;
    }

    public float x,y,width,height;

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        return isHover(mouseX, mouseY);
    }
    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        return isHover(mouseX, mouseY);
    }
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
    public abstract void handleMouseInput() throws IOException;



}
