package io.justme.lavender.ui.screens.mainmenu.panels.main.components.impl.button;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.mainmenu.panels.main.components.AbstractComponent;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/17
 **/
@Getter
public class MainMenuButton extends AbstractComponent {


    public MainMenuButton(MainMenuButtonType mainMenuButtonType) {
        super(mainMenuButtonType);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),15,new Color(0xE9DEF8));

        if (isHover(mouseX, mouseY)) {
            RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),15,new Color(0xE1D1FF));
        }

        var font = La.getINSTANCE().getFontManager().getPingFang_Bold22();
        font.drawString(getMainMenuButtonType().getName(),
                        getX() + getWidth() / 2 - font.getStringWidth(getMainMenuButtonType().getName()) / 2f,
                        getY() + getHeight() / 2 - font.getHeight() / 3f,
                        new Color(0xD9220F46, true).getRGB());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
