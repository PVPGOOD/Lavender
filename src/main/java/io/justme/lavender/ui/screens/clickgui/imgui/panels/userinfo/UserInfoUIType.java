package io.justme.lavender.ui.screens.clickgui.imgui.panels.userinfo;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2025/6/22
 **/
@Getter
public enum UserInfoUIType {
    USERINFO_AVATAR_UI("AvatarUI"),
    USERINFO_INFORMATION_UI("InformationUI");

    private final String name;

    UserInfoUIType(String name) {
        this.name = name;
    }
}
