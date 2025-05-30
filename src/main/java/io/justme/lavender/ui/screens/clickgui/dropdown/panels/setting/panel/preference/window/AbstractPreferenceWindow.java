package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.window;

import io.justme.lavender.setting.SettingPreferenceType;
import io.justme.lavender.utility.math.animation.Animation;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/30
 **/
//有时间再减少冗余重复代码 hhh
//2025/5/30
@Getter
@Setter
public abstract class AbstractPreferenceWindow {

    private Animation positionYAnimation;
    public float x,y,width,height;
    private SettingPreferenceType type;

    public AbstractPreferenceWindow(SettingPreferenceType type) {
        this.type = type;
    }

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract boolean mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
    public abstract void handleMouseInput() throws IOException;

}
