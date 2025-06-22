package io.justme.lavender.ui.screens.clickgui.imgui.panels.userinfo.imp;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.userinfo.AbstractUserInfoUI;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.userinfo.UserInfoUIType;
import io.justme.lavender.ui.screens.clickgui.imgui.theme.ThemeColorEnum;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/6/22
 **/
public class UserInformationUI extends AbstractUserInfoUI {
    public UserInformationUI(UserInfoUIType type) {
        super(type);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        var fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium14();
        String[] infos = {
            "UID: 0",
            "Username: JustMe",
            "License: Forever"
        };
        var startY = getY() + 2;
        var lineHeight = 10;
        var color = La.getINSTANCE().getTheme().getColor(ThemeColorEnum.PANEL_USERINFO_TITLE_FONT).getRGB();

        for (int i = 0; i < infos.length; i++) {
            fontDrawer.drawCenteredString(infos[i], getX() + 10, startY + i * lineHeight, color);
        }
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
