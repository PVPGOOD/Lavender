package io.justme.lavender.setting;

import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.MultiBoolValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2025/4/5
 **/
@Getter
@Setter
public class SettingManager {

    //设置
    private final BoolValue debugValue = new BoolValue("信息调试", false);
    //discord rpc
    private final BoolValue enableDiscordRPCValue = new BoolValue("Discord RPC", true);

    private final BoolValue notificationValue = new BoolValue("启用 通知", true);
    private final MultiBoolValue notificationMultiValue = new MultiBoolValue("通知设置",
            new BoolValue("启用通知声音", true, () -> getNotificationValue().getValue()),
            new BoolValue("第一次链接服务器时提醒当前账户ID", true, () -> getNotificationValue().getValue())
    );
    private final NumberValue notificationAliveValue = new NumberValue("通知 存活时间(毫秒)", 500, 10, 1000, 10 , () -> getNotificationValue().getValue());

    private final BoolValue gameEndValue = new BoolValue("当 游戏结束时自动关闭模块", true);
    private final MultiBoolValue gameEndMultiValue = new MultiBoolValue("对应的模块" ,
            new BoolValue("KillAura", true, () -> getGameEndValue().getValue()),
            new BoolValue("Speed", true, () -> getGameEndValue().getValue()),
            new BoolValue("Scaffold", true, () -> getGameEndValue().getValue()),
            new BoolValue("InvManager", true, () -> getGameEndValue().getValue()));

    //teams
    private final BoolValue teamsCheck = new BoolValue("启用 队伍检测", true);
    private final MultiBoolValue teamsCheckMultiValue = new MultiBoolValue("对应的模块" ,
            new BoolValue("KillAura ", true, () -> getTeamsCheck().getValue()),
            new BoolValue("AimAssist", true, () -> getTeamsCheck().getValue())
    );
    //antibot
    private final BoolValue antiBotCheck = new BoolValue("启用 假人检测", true);
    private final ModeValue antibotCheckModeValue = new ModeValue("假人检测模式", new String[]{"Hypixel"}, "Hypixel", () -> getAntiBotCheck().getValue());
    private final MultiBoolValue antiBotCheckMultiValue = new MultiBoolValue("对应的模块" ,
            new BoolValue("KillAura ", true, () -> getAntiBotCheck().getValue()),
            new BoolValue("AimAssist", true, () -> getAntiBotCheck().getValue())
    );

    //渲染
    //后期渲染
    private final BoolValue postRenderingValue = new BoolValue("启用 后期渲染", true);
    private final MultiBoolValue postRenderingMultiValue = new MultiBoolValue("启用渲染" ,
            new BoolValue("模糊 ", true, () -> getPostRenderingValue().getValue()),
            new BoolValue("字体辉光 ", true, () -> getPostRenderingValue().getValue()),
            new BoolValue("阴影", true, () -> getPostRenderingValue().getValue())
    );
    private final NumberValue blurStrange =
            new NumberValue("模糊强度", 5, 1, 10, 1 , () -> getNotificationValue().getValue());
    private final NumberValue shadow =
            new NumberValue("阴影强度", 5, 1, 10, 1 , () -> getNotificationValue().getValue());

    //暂时用不到
//    private final HashMap<SettingType, List<DefaultValue<?>>> settingTypeHashMap = new LinkedHashMap<>();

    private final ArrayList<DefaultValue<?>> settingList = new ArrayList<>();

    public void onInitialization() {
        getSettingList().add(getDebugValue());
        getSettingList().add(getEnableDiscordRPCValue());

        getSettingList().add(getNotificationValue());
        getSettingList().add(getNotificationMultiValue());
        getSettingList().add(getNotificationAliveValue());

        getSettingList().add(getGameEndValue());
        getSettingList().add(getGameEndMultiValue());

        getSettingList().add(getTeamsCheck());
        getSettingList().add(getTeamsCheckMultiValue());

        getSettingList().add(getAntiBotCheck());
        getSettingList().add(getAntibotCheckModeValue());
        getSettingList().add(getAntiBotCheckMultiValue());

        getSettingList().add(getPostRenderingValue());
        getSettingList().add(getPostRenderingMultiValue());
        getSettingList().add(getBlurStrange());
        getSettingList().add(getShadow());
        //设置类型
    }
}
