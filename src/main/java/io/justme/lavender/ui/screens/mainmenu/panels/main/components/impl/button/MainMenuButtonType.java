package io.justme.lavender.ui.screens.mainmenu.panels.main.components.impl.button;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2025/5/17
 **/
@Getter
public enum MainMenuButtonType {
    SINGLE_PLAY("Single Play"),
    MULTIPLE_PLAY("Multiple Play"),
    ALTS_LOGIN("Alts Login"),
    OPTIONS("Options"),
    EXIT("Exit");

    private final String name;

    MainMenuButtonType(String name){
        this.name = name;
    }
}
