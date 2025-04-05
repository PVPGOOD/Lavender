package io.justme.lavender.setting;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2025/4/4
 **/
@Getter
public enum SettingType {
    GLOBAL_SETTING("Global Setting"),
    RENDERING("Rendering");

    private final String name;

    SettingType(String name) {
        this.name = name;
    }
}
