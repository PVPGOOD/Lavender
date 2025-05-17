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
        float cornerRadius = getHeight() * 0.5f;
        Color baseColor = new Color(0xE9DEF8);
        Color hoverColor = new Color(0xE1D1FF);

        RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), cornerRadius,
                isHover(mouseX, mouseY) ? hoverColor : baseColor);

        var fontManager = La.getINSTANCE().getFontManager();
        var font = getHeight() < 21 ? fontManager.getPingFang_Bold18() : fontManager.getPingFang_Bold22();

        var text = getMainMenuButtonType().getName();
        float textWidth = font.getStringWidth(text);
        float textHeight = font.getHeight();

        float textX = getX() + getWidth() / 2f - textWidth / 2f;
        float textY = getY() + getHeight() / 2f - textHeight / 2f + 4;

        font.drawString(text, textX, textY, new Color(0xD9220F46, true).getRGB());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
