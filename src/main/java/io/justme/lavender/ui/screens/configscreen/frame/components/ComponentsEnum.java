package io.justme.lavender.ui.screens.configscreen.frame.components;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/
@Getter
public enum ComponentsEnum {
    LOAD("Load"),
    RELOAD("Reload"),
    ADD("Add"),
    REFRESH("Refresh");

    private final String name;

    ComponentsEnum(String name) {
        this.name = name;
    }
}
