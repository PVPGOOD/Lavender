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
        RenderUtility.drawRoundRect(getX(), getY(), getWidth(), 20, 8, new Color(0xE8DEF8));

        var iconWidth = 16;
        var iconHeight = 16;
        var iconX = getX() + getWidth() /2f - iconWidth /2f;
        var iconY = getY() + getHeight() / 2 - iconHeight /2f - 4;

        var font = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        var strX = getX();
        var srtY = getY() + getHeight() / 2 + 7;
        var strColor = new Color(0x8C000000, true);
        switch (getCategoryType()) {
            case FIGHT -> {
                font.drawString(getCategoryType().getName(),strX + getWidth() /2f - font.getStringWidth(getCategoryType().getName()) /2f,srtY,strColor.getRGB());
                RenderUtility.drawImage(new ResourceLocation("la/clickgui/category/Fight.png"), iconX, iconY, iconWidth, iconHeight, new Color(0x4A4459));
            }
            case VISUAL -> {
                font.drawString(getCategoryType().getName(),strX + getWidth() /2f - font.getStringWidth(getCategoryType().getName()) /2f,srtY,strColor.getRGB());
                RenderUtility.drawImage(new ResourceLocation("la/clickgui/category/Visual.png"), iconX, iconY, iconWidth, iconHeight, new Color(0x4A4459));
            }
            case MOVEMENTS -> {
                font.drawString("Move",strX + getWidth() /2f - font.getStringWidth("Move") /2f,srtY,strColor.getRGB());
                RenderUtility.drawImage(new ResourceLocation("la/clickgui/category/Movements.png"), iconX, iconY, iconWidth, iconHeight, new Color(0x4A4459));
            }
            case PLAYER -> {

                font.drawString(getCategoryType().getName(),strX + getWidth() /2f - font.getStringWidth(getCategoryType().getName()) /2f,srtY,strColor.getRGB());
                RenderUtility.drawImage(new ResourceLocation("la/clickgui/category/player.png"), iconX, iconY, iconWidth, iconHeight, new Color(0x4A4459));
            }

            case MISC -> {
                font.drawString(getCategoryType().getName(),strX + getWidth() /2f - font.getStringWidth(getCategoryType().getName()) /2f,srtY,strColor.getRGB());
                RenderUtility.drawImage(new ResourceLocation("la/clickgui/category/Misc.png"), iconX, iconY, iconWidth, iconHeight, new Color(0x4A4459));
            }
            case World -> {
                font.drawString(getCategoryType().getName(),strX + getWidth() /2f - font.getStringWidth(getCategoryType().getName()) /2f,srtY,strColor.getRGB());
                RenderUtility.drawImage(new ResourceLocation("la/clickgui/category/world.png"), iconX, iconY, iconWidth, iconHeight, new Color(0x4A4459));
            }
            case Exploit -> {
                font.drawString(getCategoryType().getName(),strX + getWidth() /2f - font.getStringWidth(getCategoryType().getName()) /2f,srtY,strColor.getRGB());
                RenderUtility.drawImage(new ResourceLocation("la/clickgui/category/Exploit.png"), iconX, iconY, iconWidth, iconHeight, new Color(0x4A4459));
            }
        }

        if (isHover(mouseX, mouseY)) {
            RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, new Color(0x8777676, true));
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
