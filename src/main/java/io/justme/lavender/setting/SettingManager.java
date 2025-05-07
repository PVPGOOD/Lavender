package io.justme.lavender.setting;

import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author JustMe.
 * @since 2025/4/5
 **/
@Getter
@Setter
public class SettingManager {

    //设置
    private final BoolValue teamsCheck = new BoolValue("队伍检测", true);

    private final BoolValue notificationValue = new BoolValue("启用 通知", true);
    private final NumberValue notificationAliveValue = new NumberValue("通知存活时间", 500, 10, 1000, 10);

    //渲染
    private final BoolValue blurValue = new BoolValue("启用 模糊渲染", false);
    private final BoolValue shadowValue = new BoolValue("启用 阴影渲染", false);

    private final HashMap<SettingType, List<DefaultValue<?>>> settingTypeHashMap = new LinkedHashMap<>();

    public void onInitialization() {
        addSetting(getNotificationValue(), SettingType.GLOBAL_SETTING);
        addSetting(getNotificationAliveValue(), SettingType.GLOBAL_SETTING);

        addSetting(getBlurValue(), SettingType.RENDERING);
        addSetting(getShadowValue(), SettingType.RENDERING);
    }

    private void addSetting(DefaultValue<?> value, SettingType type) {
        getSettingTypeHashMap().computeIfAbsent(type, k -> new ArrayList<>()).add(value);
    }
}
