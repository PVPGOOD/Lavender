package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.category;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/
@Getter
public enum CategoryType {
    //设置
    SETTING("偏好设置"),
    //元素
    ELEMENT("元素设置"),
    THEME("主题设置"),
    //皮肤设置
    SKIN("皮肤/披风"),
    //关于我们
    ABOUT("关于我们");

    private final String name;

    CategoryType(String name) {
        this.name = name;
    }
}
