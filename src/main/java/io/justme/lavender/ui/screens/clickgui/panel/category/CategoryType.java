package io.justme.lavender.ui.screens.clickgui.panel.category;

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
    MOVEMENTS("Movements"),
    VISUAL("Visual"),
    MISC("Miscellaneous"),
    EXPLOIT("Exploit"),
    PLAYER("Player"),
    World("World"),
    Exploit("Exploit");

    private final String name;

    CategoryType(String name) {
        this.name = name;
    }
}
