package io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.impl.setting;

import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.AbstractNavBar;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.NavBarType;
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
public class NavSettingButton extends AbstractNavBar {

    private NavSettingButtonType navSettingButtonType;

    public NavSettingButton(NavSettingButtonType navSettingButtonType) {
        super(NavBarType.SETTING);

        this.navSettingButtonType = navSettingButtonType;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, new Color(0xFFD8E4));

        var iconWidth = 14;
        var iconHeight = 14;
        switch (getNavSettingButtonType()) {
            case SETTING_BUTTON -> RenderUtility.drawImage(new ResourceLocation("la/clickgui/imgui/settings.png"),getX() + getWidth() /2f - iconWidth /2f ,getY() + getHeight() / 2 - iconHeight /2f,iconWidth,iconHeight,new Color(0x633B48));
            case DESIGN_BUTTON -> RenderUtility.drawImage(new ResourceLocation("la/clickgui/imgui/blocks-hover.png"),getX() + getWidth() /2f - (iconWidth - 2) /2f,getY() + getHeight() / 2 - (iconHeight -2) /2f,(iconWidth - 2),(iconHeight -2),new Color(0x633B48));
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
