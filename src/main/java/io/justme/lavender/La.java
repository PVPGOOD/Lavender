package io.justme.lavender;

import de.florianmichael.viamcp.ViaMCP;
import de.florianmichael.viamcp.gui.AsyncVersionSlider;
import io.justme.lavender.configs.ConfigsManager;
import io.justme.lavender.fonts.FontManager;
import io.justme.lavender.module.ModuleManager;
import io.justme.lavender.ui.elements.ElementsManager;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

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
    @Setter
    private int mouseX,mouseY;
    private EventManager eventManager;
    private FontManager fontManager;
    private ModuleManager moduleManager;
    private ElementsManager elementsManager;

    //via
    private ViaMCP viaMCP;
    private AsyncVersionSlider asyncVersionSlider;

    private ConfigsManager configsManager;

    public void initialization() {
        eventManager = new EventManager();
        fontManager = new FontManager();
        moduleManager = new ModuleManager();
        moduleManager.onInitialization();
        elementsManager = new ElementsManager();

        viaMCP = new ViaMCP();
        asyncVersionSlider = new AsyncVersionSlider(1337, 20, 5, 110,20);

        configsManager = new ConfigsManager();
        configsManager.load();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> configsManager.save()));

        eventManager.register(this);
    }

    public void print(String message) {

        String str = String.format(EnumChatFormatting.DARK_RED + "[%s]" + EnumChatFormatting.GRAY+ ":" + EnumChatFormatting.WHITE +" %s", getLa(),message);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(str));
    }

    public static io.justme.lavender.La getINSTANCE() {
        return INSTANCE;
    }
}
