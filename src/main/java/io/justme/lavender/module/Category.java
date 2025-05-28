package io.justme.lavender.module;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2023/12/23
 **/
@Getter
public enum Category {
    FIGHT("Fight"),
    VISUAL("Visual"),
    MOVEMENTS("Move"),
    PLAYER("Player"),
    MISC("Misc"),
    World("World"),
    Exploit("Exploit");

    private final String name;

    Category(String name) {
        this.name = name;
    }

}
