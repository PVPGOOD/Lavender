package io.justme.lavender.configs.impl;

import com.google.gson.*;
import io.justme.lavender.La;
import io.justme.lavender.configs.AbstractConfigs;
import io.justme.lavender.configs.ConfigsEnum;
import io.justme.lavender.setting.SettingPreferenceType;
import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.*;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author JustMe.
 * @since 2025/5/31
 **/
public class PreferenceSetting extends AbstractConfigs {

    public PreferenceSetting() {
        setFileName(ConfigsEnum.PREFERENCE);
    }

    @SneakyThrows
    @Override
    public void save() {
        var settingManager = La.getINSTANCE().getSettingManager();
        JsonObject root = new JsonObject();

        for (Map.Entry<SettingPreferenceType, ArrayList<DefaultValue<?>>> entry : settingManager.getSettingList().entrySet()) {
            JsonObject typeObj = new JsonObject();
            for (DefaultValue<?> value : entry.getValue()) {
                typeObj.add(value.getName(), new Gson().toJsonTree(value.getValue()));
            }
            root.add(entry.getKey().name(), typeObj);
        }

        Files.writeString(getFilesPath(), new GsonBuilder().setPrettyPrinting().create().toJson(root));
    }

    @SneakyThrows
    @Override
    public void load() {
        var settingManager = La.getINSTANCE().getSettingManager();
        var path = getFilesPath();
        if (!Files.exists(path)) return;


        var root = JsonParser.parseString(Files.readString(path)).getAsJsonObject();

        for (var entry : settingManager.getSettingList().entrySet()) {
            var typeName = entry.getKey().name();
            if (!root.has(typeName)) continue;

            var typeJson = root.get(typeName);

            for (var value : entry.getValue()) {
                JsonElement elem = loadValueFor(value, typeJson);
                if (elem != null) {
                    load(value, elem);
                }
            }
        }
    }

    private JsonElement loadValueFor(DefaultValue<?> value, JsonElement typeJson) {
        if (typeJson.isJsonObject()) {
            return typeJson.getAsJsonObject().get(value.getName());
        } else if (typeJson.isJsonArray()) {
            for (JsonElement el : typeJson.getAsJsonArray()) {
                if (el.isJsonObject()) {
                    var obj = el.getAsJsonObject();
                    if (value.getName().equals(obj.get("name").getAsString())) {
                        return obj.get("value");
                    }
                }
            }
        }
        return null;
    }

    private void load(DefaultValue<?> value, JsonElement elem) {
        switch (value) {
            case BoolValue boolVal -> boolVal.setValue(elem.getAsBoolean());
            case NumberValue numVal -> numVal.setValue((float) elem.getAsDouble());
            case ModeValue modeVal -> modeVal.setValue(elem.getAsString());
            case TextValue textVal -> textVal.setValue(elem.getAsString());
            case MultiBoolValue multiVal -> loadMultiBoolValue(multiVal, elem);
            default -> {
            }
        }
    }

    private void loadMultiBoolValue(MultiBoolValue multiVal, JsonElement elem) {
        if (elem.isJsonObject()) {
            var obj = elem.getAsJsonObject();
            for (var boolVal : multiVal.getValue()) {
                if (obj.has(boolVal.getName())) {
                    boolVal.setValue(obj.get(boolVal.getName()).getAsBoolean());
                }
            }
        } else if (elem.isJsonArray()) {
            for (var el : elem.getAsJsonArray()) {
                if (el.isJsonObject()) {
                    var obj = el.getAsJsonObject();
                    var name = obj.get("name").getAsString();
                    var value = obj.get("value").getAsBoolean();
                    for (var boolVal : multiVal.getValue()) {
                        if (boolVal.getName().equals(name)) {
                            boolVal.setValue(value);
                        }
                    }
                }
            }
        }
    }

}

