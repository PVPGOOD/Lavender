package io.justme.lavender.handler.impl.hack;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2025/5/23
 **/
@Getter
public enum HackType {

    OMNI_SPRINT("全方向疾跑"),
    AUTO_BLOCK("自动格挡"),
    NO_SLOWDOWN("无格挡减速");

    private String name;

    HackType(String name) {
        this.name = name;
    }
}

