package io.justme.lavender.module;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import io.justme.lavender.utility.interfaces.IMinecraft;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.EnumChatFormatting;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2023/12/23
 **/
@Getter
@Setter
public class Module implements IMinecraft {

    public String name;
    public String description;
    public Category category;
    public int key;
    private boolean toggle;
    private final Animation animationInterval = new Animation();
    private final Animation animation = new Animation();
    private final ArrayList<DefaultValue<?>> options = new ArrayList<>();

    public Module(){
        final ModuleInfo info = this.getClass().getAnnotation(ModuleInfo.class);
        this.name = info.name();
        this.description = info.description();
        this.category = info.category();
        this.key = info.key();
        this.toggle = true;
    }

    public void setStatus(boolean enable, boolean pushNotification) {
        this.toggle = enable;

        boolean notify = La.getINSTANCE().getSettingManager().getNotificationValue().getValue()
                && pushNotification
                && La.getINSTANCE().getSettingManager().getNotificationMultiValue().find("开启模块时 推送通知").getValue();

        boolean playSound = La.getINSTANCE().getSettingManager().getNotificationValue().getValue()
                && La.getINSTANCE().getSettingManager().getNotificationMultiValue().find("启用通知 声音").getValue();

        if (notify) {
            String status = enable ? "was enabled" : "was disabled";
            EnumChatFormatting color = enable ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
            La.getINSTANCE().getNotificationsManager().push(
                    "Module Manager",
                    String.format("%s %s %s %s", color, name, EnumChatFormatting.RESET, status),
                    NotificationsEnum.SUCCESS,
                    1000,
                    playSound
            );
        }

        if (enable) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void setStatus(boolean enable){
        this.toggle = enable;

        if (enable){
            onEnable();
        } else {
            onDisable();
        }
    }

    public void reflectValues(){
        try {
            for (Field field : getClass().getDeclaredFields()) {
                if (field.getType().isAssignableFrom(DefaultValue.class) ||
                        field.getType().isAssignableFrom(BoolValue.class) ||
                        field.getType().isAssignableFrom(ColorValue.class) ||
                        field.getType().isAssignableFrom(NumberValue.class) ||
                        field.getType().isAssignableFrom(ModeValue.class) ||
                        field.getType().isAssignableFrom(MultiBoolValue.class) ||
                        field.getType().isAssignableFrom(TextValue.class) ||
                        field.getType().isAssignableFrom(NumberRangeValue.class)) {
                    if (!field.isAccessible())
                        field.setAccessible(true);
                    options.add((DefaultValue<?>) field.get(this));
                }
            }
        } catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException(illegalAccessException);
        }
    }


    public void onEnable(){
        La.getINSTANCE().getEventManager().register(this);
    }

    public void onDisable(){
        La.getINSTANCE().getEventManager().unregister(this);
    }

}
