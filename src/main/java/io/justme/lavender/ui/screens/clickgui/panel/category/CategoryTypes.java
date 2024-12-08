package io.justme.lavender.ui.screens.clickgui.panel.category;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/
@Getter
public enum CategoryTypes {
    CLIENT_SETTINGS("Settings"),
    MANAGER_POPPING("Pop-ups");

    private final String name;

    CategoryTypes(String name) {
        this.name = name;
    }
}
