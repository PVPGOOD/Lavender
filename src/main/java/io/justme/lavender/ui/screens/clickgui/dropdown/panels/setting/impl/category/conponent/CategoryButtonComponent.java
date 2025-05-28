package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.impl.category.conponent;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.impl.category.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.impl.category.CategoryType;
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
public class CategoryButtonComponent extends AbstractComponent {

    public CategoryType category;
    private final boolean neededIcon;

    public CategoryButtonComponent(CategoryType category) {
        super(category);
        this.neededIcon = true;
        this.category = category;
    }

    public CategoryButtonComponent(CategoryType category, boolean neededIcon) {
        super(category);

        this.neededIcon = neededIcon;
        this.category = category;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int alpha = ((int)(getCategory() == La.getINSTANCE().getDropScreen().getCurrentCategory() ? 255 : getCategoryTypeBackgroundAlpha().getValue()));
//        var alpha = 255;

        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),12,new Color(232, 222, 248,alpha));


        FontDrawer fontManager = La.getINSTANCE().getFontManager().getPingFang_Medium20();
        fontManager.drawString(getCategory().getName(),
                getX() + (getWidth() /2f) - (fontManager.getStringWidth(getCategory().getName()) /2f),
                getY() + (getHeight() / 2f) - (fontManager.getHeight() / 2f) + 4,
                new Color(129, 57, 80,255).getRGB());

        if (isNeededIcon()) {
            RenderUtility.drawImage(
                    new ResourceLocation("la/clickgui/category/" + getCategory().getName() + ".png"),
                    getX() + 8,
                    getY() + (getHeight() / 4),
                    12,
                    12,
                    new Color(129, 57, 80,255));
        }

        getCategoryTypeBackgroundAlpha().animate(isHover(mouseX, mouseY) ?
                128 : 16, 0.1f);

        getCategoryTypeBackgroundAlpha().update();
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
