package io.justme.lavender.configs;

import io.justme.lavender.configs.impl.SettingsConfigs;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author PVP_GOOD_
 * @since 2023/8/12 2:43
 */

@Getter
@Setter
public class ConfigsManager extends AbstractConfigs {

    private final ArrayList<AbstractConfigs> configs = new ArrayList<>();
    private final SettingsConfigs settingsConfigs = new SettingsConfigs();

    public ConfigsManager(){
        getConfigs().addAll(Arrays.asList(
                settingsConfigs
        ));
    }

    @Override
    public void load() {
        for (AbstractConfigs abstractConfigs : getConfigs()) {
            if (abstractConfigs.getFilesPath().toFile().exists()) {
                abstractConfigs.load();
            }
        }
    }

    @Override
    public void save() {
        for (AbstractConfigs abstractConfigs : getConfigs()) {

            if (!abstractConfigs.getFilesPath().toFile().exists()) {
                try {

                    Files.createDirectories(abstractConfigs.getFilesPath().getParent());
                    abstractConfigs.getFilesPath().toFile().createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            abstractConfigs.save();
        }
    }
}
