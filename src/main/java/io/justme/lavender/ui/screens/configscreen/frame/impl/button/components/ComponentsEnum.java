package io.justme.lavender.ui.screens.configscreen.frame.impl.button.components;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/
@Getter
public enum ComponentsEnum {

    //类别
    CHECKBOX("CheckBox"),
    List("List");

    private final String name;

    ComponentsEnum(String name) {
        this.name = name;
    }
}
