package io.justme.lavender.configs.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.justme.lavender.La;
import io.justme.lavender.configs.AbstractConfigs;
import io.justme.lavender.configs.ConfigsEnum;
import io.justme.lavender.module.Module;
import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author PVP_GOOD_
 * @since 2023/8/12 2:44
 */
@Getter
@Setter
public class SettingsConfigs extends AbstractConfigs {

    public SettingsConfigs() {
       setFileName(ConfigsEnum.CONFIG);
    }

    @SneakyThrows
    @Override
    public void load() {
        var jsonObject = new Gson().fromJson(Files.readString(getFilesPath()), JsonObject.class);

        try {
            for (Module module : La.getINSTANCE().getModuleManager().getElements()) {
                if (jsonObject.has(module.getName())) {
                    var moduleJsonObject = jsonObject.get(module.getName()).getAsJsonObject();

                    if (moduleJsonObject.has("enable")) {
                        module.setStatus(moduleJsonObject.get("enable").getAsBoolean());
                    }

                    if (moduleJsonObject.has("key")) {
                        module.setKey(moduleJsonObject.get("key").getAsInt());
                    }

                    if (moduleJsonObject.has("settings")) {
                        final JsonObject valueObject = moduleJsonObject.get("settings").getAsJsonObject();
                        for (final DefaultValue<?> value : module.getOptions()) {
                            if (valueObject.has(value.getName())) {
                                if (value instanceof BoolValue) {
                                    val boolValue = (BoolValue) value;
                                    boolValue.setValue(valueObject.get(value.getName()).getAsBoolean());
                                } else if (value instanceof NumberValue numberValue) {
                                    numberValue.setValue((float) valueObject.get(value.getName()).getAsDouble());
                                } else if (value instanceof ColorValue) {
                                    val colorValueObject = valueObject.get(value.getName()).getAsJsonObject();
                                    ((ColorValue) value)
                                            .setColor(new Color(colorValueObject.get("RGB").getAsInt()));
                                    if (((ColorValue) value).getValue()) {
                                        ((ColorValue) value).setAlpha(colorValueObject.get("Alpha").getAsFloat());
                                    }
                                } else if (value instanceof ModeValue modeValue) {
                                    modeValue.setValue(valueObject.get(value.getName()).getAsString());
                                } else if (value instanceof MultiBoolValue) {
                                    for (BoolValue boolValue : ((MultiBoolValue) value).getValue()) {
                                        boolValue.setValue(valueObject.get(value.getName()).getAsJsonObject().get(boolValue.getName()).getAsBoolean());
                                    }
                                } else if (value instanceof TextValue textValue) {
                                    textValue.setValue(valueObject.get(value.getName()).getAsString());
                                }
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException exception){
            exception.printStackTrace();
        }
    }

    @Override
    public void save() {

        var jsonObject = new JsonObject();
         for (Module module : La.getINSTANCE().getModuleManager().getElements()){
             var modulesJsonObject = new JsonObject();

             modulesJsonObject.addProperty("enable",module.isToggle());

             modulesJsonObject.addProperty("key",module.getKey());

             var elements = module.getOptions();
             if (!elements.isEmpty()) {
                 var valueObject = new JsonObject();
                 for (DefaultValue<?> value : module.getOptions()) {
                     if (value instanceof ModeValue modeValue) {
                         valueObject.addProperty(value.getName(), modeValue.getValue());
                     } else if (value instanceof BoolValue) {
                         valueObject.addProperty(value.getName(), ((BoolValue) value).getValue());
                     } else if (value instanceof ColorValue) {
                         val colorValueObject = new JsonObject();
                         colorValueObject.addProperty("RGB", ((ColorValue) value).getColor().getRGB());
                         if (((ColorValue) value).getValue())
                             colorValueObject.addProperty("Alpha", ((ColorValue) value).getAlpha());
                         valueObject.add(value.getName(), colorValueObject);

                     } else if (value instanceof MultiBoolValue) {
                         val multiBooleanElements = new JsonObject();

                         for (BoolValue boolValue : ((MultiBoolValue) value).getValue()) {
                             multiBooleanElements.addProperty(boolValue.getName(), boolValue.getValue());
                         }

                         valueObject.add(value.getName(), multiBooleanElements);
                     } else if (value instanceof NumberValue) {
                         valueObject.addProperty(value.getName(), ((NumberValue) value).getValue());
                     } else if (value instanceof TextValue) {
                         valueObject.addProperty(value.getName(), ((TextValue) value).getValue());
                     }
                 }
                 modulesJsonObject.add("settings", valueObject);
             }
             jsonObject.add(module.getName(),modulesJsonObject);
         }
        try {
            Files.writeString(getFilesPath(), new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
