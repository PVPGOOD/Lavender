package io.justme.lavender.ui.screens.clickgui.panel.settings;

/**
 * @author JustMe.
 * @since 2025/4/4
 **/
public enum SettingType {
    GLOBAL_SETTING("Global Setting"),
    RENDERING("Rendering");

    private final String name;

    SettingType(String name) {
        this.name = name;
    }
}
