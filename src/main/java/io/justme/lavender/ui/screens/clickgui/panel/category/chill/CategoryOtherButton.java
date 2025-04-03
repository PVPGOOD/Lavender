package io.justme.lavender.ui.screens.clickgui.panel.category.chill;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.panel.category.CategoryTypes;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/
@Getter
@Setter
public class CategoryOtherButton extends AbstractControlsComponent {

    private CategoryTypes categoryTypes;

    public CategoryOtherButton(CategoryTypes types) {
        this.categoryTypes = types;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int alpha = ((int)(getCategoryTypeBackgroundAlpha().getValue()));

        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),14,new Color(243, 218, 224,alpha));

        FontDrawer fontManager = La.getINSTANCE().getFontManager().getPingFang_Medium22();
        fontManager.drawString(getCategoryTypes().getName(),
                getX() + (getWidth() /2f) - (fontManager.getStringWidth(getCategoryTypes().getName()) /2f),
                getY() + (getHeight() / 4),
                new Color(129, 57, 80).getRGB());

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
