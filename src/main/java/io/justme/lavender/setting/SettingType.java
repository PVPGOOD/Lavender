package io.justme.lavender.setting;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2025/4/4
 **/
@Getter
public enum SettingType {
    GLOBAL_SETTING("常用"),
    RENDERING("后期渲染");

    private final String name;

    SettingType(String name) {
        this.name = name;
    }
}
