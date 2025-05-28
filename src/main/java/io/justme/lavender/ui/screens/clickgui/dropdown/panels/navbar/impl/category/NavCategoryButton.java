package io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.impl.category;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.AbstractNavBar;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.NavBarType;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/7
 **/
@Getter
@Setter
public class NavCategoryButton extends AbstractNavBar {

    public CategoryType categoryType;

    public NavCategoryButton(CategoryType categoryType) {
        super(NavBarType.CATEGORY);

        this.categoryType = categoryType;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (isSelecting()) {
            RenderUtility.drawRoundRect(getX(), getY(), getWidth(), 16, 8, new Color(0xE8DEF8));

        }

        var iconWidth = 12;
        var iconHeight = 12;
        var iconX = getX() + getWidth() /2f - iconWidth /2f;
        var iconY = getY() + getHeight() / 2 - iconHeight /2f - 6;

        var font = La.getINSTANCE().getFontManager().getPingFang_Bold16();
        var strX = getX();
        var srtY = getY() + getHeight() / 2 + 4;
        var strColor = new Color(0xA34A4459);

        RenderUtility.drawImage(
                new ResourceLocation("la/clickgui/category/md/"+ getCategoryType().getName() + ".png"),
                iconX, iconY, iconWidth, iconHeight, new Color(0x4A4459));

        font.drawString(getCategoryType().getName(),strX + getWidth() /2f - font.getStringWidth(getCategoryType().getName()) /2f,srtY,strColor.getRGB());

        if (isHover(mouseX, mouseY)) {
            RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, new Color(0,0,0,24));
        }

        setHeight(28);
        setWidth(28);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        return isHover(mouseX, mouseY);
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        return isHover(mouseX, mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
