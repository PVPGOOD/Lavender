package io.justme.lavender.setting;

import lombok.Getter;

/**
 * @author JustMe.
 * @since 2025/5/30
 **/
@Getter
public enum SettingPreferenceType {
    //只需要描述对应的就行不需要描述multi
    DEBUG("信息调试"),
    DISCORD_RPC("Discord RPC"),
    NOTIFICATION("通知"),
    GAME_END("游戏结束时自动关闭模块"),
    TEAMS_CHECK("队伍检测"),
    ANTI_BOT_CHECK("假人检测"),
    POST_RENDERING("后期渲染");

    private final String name;

    SettingPreferenceType(String name) {
        this.name = name;
    }
}
