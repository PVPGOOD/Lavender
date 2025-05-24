package io.justme.lavender.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.justme.lavender.La;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author PVP_GOOD_
 * @since 2023/8/12 2:43
 */
@Getter
@Setter
public abstract class AbstractConfigs {

    private ConfigsEnum fileName;

    //.Minecraft/lavender/Config/pageName/fileName.json
    public Path getFilesPath(){
        return Paths.get(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), La.getINSTANCE().getLa(), La.getINSTANCE().getConfigsManager().getPageName(),fileName + ".json");
    }

    //.Minecraft/lavender/Config/pageName
    public Path getFilesPageName(){
        return Paths.get(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), La.getINSTANCE().getLa(),La.getINSTANCE().getConfigsManager().getPageName());
    }

    public abstract void load();

    public abstract void save();


    public boolean has(String elementName) {
        try {
            var jsonObject = new Gson().fromJson(Files.readString(getFilesPath()), JsonObject.class);
            return jsonObject != null && jsonObject.has(elementName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void remove(String elementName) {
        try {
            var jsonObject = new Gson().fromJson(Files.readString(getFilesPath()), JsonObject.class);
            if (jsonObject != null && jsonObject.has(elementName)) {
                jsonObject.remove(elementName);
                Files.writeString(getFilesPath(), new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
