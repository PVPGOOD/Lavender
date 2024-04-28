package io.justme.lavender;

import io.justme.lavender.configs.ConfigsManager;
import io.justme.lavender.fonts.FontManager;
import io.justme.lavender.module.ModuleManager;
import lombok.Getter;
import net.lenni0451.asmevents.EventManager;

/**
 * @author JustMe.
 * @since 2024/4/28
 **/
@Getter
public class La {

    private final static La INSTANCE = new La();

    //客户端相关
    private final String La = "Lavender";
    private final String author = "JustMe";
    private final String version = "1.0";

    private EventManager eventManager;
    private FontManager fontManager;
    private ModuleManager moduleManager;

    private ConfigsManager configsManager;

    public void initialization() {
        eventManager = new EventManager();
        fontManager = new FontManager();
        moduleManager = new ModuleManager();
        moduleManager.onInitialization();

        configsManager = new ConfigsManager();
        configsManager.load();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> configsManager.save()));

        eventManager.register(this);
    }

    public static io.justme.lavender.La getINSTANCE() {
        return INSTANCE;
    }
}
