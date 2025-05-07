package io.justme.lavender.ui.screens.clickgui.dropdown.element.navbar.impl.hamburger;

import io.justme.lavender.ui.screens.clickgui.dropdown.element.navbar.AbstractNavBar;
import io.justme.lavender.ui.screens.clickgui.dropdown.element.navbar.NavBarType;
import io.justme.lavender.utility.gl.RenderUtility;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/7
 **/
public class NavHamburgerButton extends AbstractNavBar {

    public NavHamburgerButton() {
        super(NavBarType.HAMBURGER);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        var iconWidth = 12;
        var iconHeight = 12;
        RenderUtility.drawImage(new ResourceLocation("la/clickgui/imgui/hamburger.png"), getX() + getWidth() / 2f - iconWidth /2f, getY() + getHeight() / 2 - iconHeight /2f, iconWidth, iconHeight,new Color(0x777676));

        if (isHover(mouseX, mouseY)) {
            RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),13, new Color(0x59777676, true));
        }

        setHeight(26);
        setWidth(26);

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
