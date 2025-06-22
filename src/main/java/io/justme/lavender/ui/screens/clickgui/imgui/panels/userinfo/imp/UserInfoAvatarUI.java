package io.justme.lavender.ui.screens.clickgui.imgui.panels.userinfo.imp;

import io.justme.lavender.ui.screens.clickgui.imgui.panels.userinfo.AbstractUserInfoUI;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.userinfo.UserInfoUIType;
import io.justme.lavender.utility.gl.RenderUtility;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/6/22
 **/
public class UserInfoAvatarUI extends AbstractUserInfoUI {

    public UserInfoAvatarUI(UserInfoUIType type) {
        super(type);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundedTexture(new ResourceLocation("la/png/shabi.png"),getX(),getY(),getWidth(),getHeight(),12,255);

        setHeight(22);
        setWidth(22);
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
