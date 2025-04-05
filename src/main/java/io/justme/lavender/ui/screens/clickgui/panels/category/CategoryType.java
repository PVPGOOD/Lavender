package io.justme.lavender.ui.screens.clickgui.panels.category;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/
@Getter
public enum CategoryType {
    CLIENT_SETTINGS("Settings"),
    MANAGER_POPPING("Pop-ups"),

    FIGHT("Fight"),
    VISUAL("Visual"),
    MOVEMENTS("Movements"),
    PLAYER("Player"),
    MISC("Misc"),
    World("World"),
    Exploit("Exploit");

    private final String name;

    CategoryType(String name) {
        this.name = name;
    }
}
