package io.justme.lavender.ui.screens.clickgui.panel.category.chill;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.module.Category;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponent;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/5
 **/
@Getter
public class CategoryModuleButton extends AbstractControlsComponent {


    public Category category;

    public CategoryModuleButton(Category category) {
        abstractCategory = category;

        this.category = category;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int alpha = ((int)(getCategory() == La.getINSTANCE().getClickScreen().getCurrentCategory() ? 255 : getCategoryTypeBackgroundAlpha().getValue()));

        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),14,new Color(243, 218, 224,alpha));

        FontDrawer fontManager = La.getINSTANCE().getFontManager().getPingFang_Medium22();
        fontManager.drawString(getCategory().getName(),
                getX() + (getWidth() /2f) - (fontManager.getStringWidth(getCategory().getName()) /2f),
                getY() + (getHeight() / 4),
                new Color(129, 57, 80,255).getRGB());

        RenderUtility.drawImage(
                new ResourceLocation("la/clickgui/category/" + getCategory().getName() + ".png"),
                getX() + 8,
                getY() + (getHeight() / 4),
                12,
                12,
                new Color(129, 57, 80,255));

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
}
