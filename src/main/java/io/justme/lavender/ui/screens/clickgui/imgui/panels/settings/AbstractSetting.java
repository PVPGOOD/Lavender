package io.justme.lavender.ui.screens.clickgui.imgui.panels.settings;

import io.justme.lavender.setting.SettingType;
import io.justme.lavender.value.DefaultValue;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JustMe.
 * @since 2025/4/4
 **/
@Getter
@Setter
public abstract class AbstractSetting {

    private SettingType settingType;
    private List<DefaultValue<?>> valueList = new LinkedList<>();
    public String name;
    public float x,y,width,height;
    public float requestInterval;

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
    public abstract void handleMouseInput() throws IOException;

    public AbstractSetting(SettingType settingType) {
        this.settingType = settingType;
    }

}
