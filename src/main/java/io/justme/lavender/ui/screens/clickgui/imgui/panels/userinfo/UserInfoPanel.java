package io.justme.lavender.ui.screens.clickgui.imgui.panels.userinfo;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.AbstractPanel;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.userinfo.imp.UserInfoAvatarUI;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.userinfo.imp.UserInformationUI;
import io.justme.lavender.ui.screens.clickgui.imgui.theme.ThemeColorEnum;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2025/6/22
 **/
@Getter
@Setter
public class UserInfoPanel extends AbstractPanel {

    private final ArrayList<AbstractUserInfoUI> abstractUserInfoUI = new ArrayList<>();

    public UserInfoPanel() {
        setName("UserInfoPanel");
    }


    @Override
    public void initGui() {
        // Initialize user info UIs
        getAbstractUserInfoUI().add(new UserInfoAvatarUI(UserInfoUIType.USERINFO_AVATAR_UI));
        getAbstractUserInfoUI().add(new UserInformationUI(UserInfoUIType.USERINFO_INFORMATION_UI));

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(), getY(),getWidth(),getHeight(),15, La.getINSTANCE().getTheme().getColor(ThemeColorEnum.PANEL_USERINFO_BACKGROUND));

        for (AbstractUserInfoUI userInfoUI : getAbstractUserInfoUI()) {
            switch (userInfoUI.getType()) {
                case USERINFO_AVATAR_UI -> {
                    var initialOffsetX = userInfoUI.getWidth() /4f;
                    var initialOffsetY = userInfoUI.getHeight() /3f;
                    userInfoUI.setX(getX() + initialOffsetX);
                    userInfoUI.setY(getY() + initialOffsetY);
                    userInfoUI.drawScreen(mouseX, mouseY, partialTicks);
                }

                case USERINFO_INFORMATION_UI -> {
                    var initialOffsetX = 55;
                    var initialOffsetY = userInfoUI.getHeight() /3f;
                    userInfoUI.setX(getX() + initialOffsetX );
                    userInfoUI.setY(getY() + initialOffsetY);
                    userInfoUI.drawScreen(mouseX, mouseY, partialTicks);
                }
            }

        }

        setHeight(35);
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
