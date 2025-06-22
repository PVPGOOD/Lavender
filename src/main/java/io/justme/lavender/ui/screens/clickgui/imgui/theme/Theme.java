package io.justme.lavender.ui.screens.clickgui.imgui.theme;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class Theme {
    private static ThemeType currentTheme = ThemeType.DARK;
    public ThemeColor themeColor;

    public void initialize() {
        themeColor = new ThemeColor();
        themeColor.initDarkColors();
    }

    public Color getColor(ThemeColorEnum colorEnum) {
        switch (currentTheme) {
            case DARK:
            default:
                return themeColor.getColor(currentTheme, colorEnum);
        }
    }
}
