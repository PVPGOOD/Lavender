package io.justme.lavender.configs;

import io.justme.lavender.La;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

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

}
