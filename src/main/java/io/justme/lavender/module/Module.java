package io.justme.lavender.module;

import io.justme.lavender.La;
import io.justme.lavender.utility.interfaces.IMinecraft;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

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
                        field.getType().isAssignableFrom(TextValue.class)) {
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
