package io.justme.lavender;

import de.florianmichael.viamcp.ViaMCP;
import de.florianmichael.viamcp.gui.AsyncVersionSlider;
import io.justme.lavender.configs.ConfigsManager;
import io.justme.lavender.events.game.EventKey;
import io.justme.lavender.fonts.FontManager;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleManager;
import io.justme.lavender.ui.elements.ElementsManager;
import io.justme.lavender.ui.screens.configscreen.ConfigScreen;
import io.justme.lavender.ui.screens.configscreen.frame.impl.button.ConfigButtonFrame;
import io.justme.lavender.ui.screens.configscreen.frame.impl.list.ConfigListFrame;
import io.justme.lavender.ui.screens.notifacation.NotificationsManager;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.EventManager;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.lwjglx.input.Keyboard;

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

    //screen
    private ConfigListFrame configListFrame;
    private ConfigButtonFrame configButtonFrame;
    private ConfigScreen configScreen;

    private NotificationsManager notificationsManager;

    private ConfigsManager configsManager;

    public void initialization() {
        eventManager = new EventManager();
        fontManager = new FontManager();
        moduleManager = new ModuleManager();
        moduleManager.onInitialization();
        elementsManager = new ElementsManager();

        viaMCP = new ViaMCP();
        asyncVersionSlider = new AsyncVersionSlider(1337, 20, 5, 110,20);

        configListFrame = new ConfigListFrame();
        configButtonFrame = new ConfigButtonFrame();
        configScreen = new ConfigScreen();

        notificationsManager = new NotificationsManager();

        configsManager = new ConfigsManager();
        configsManager.load();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> configsManager.save()));

        eventManager.register(this);
    }

    public void print(String message) {

        String str = String.format(EnumChatFormatting.DARK_RED + "[%s]" + EnumChatFormatting.GRAY+ ":" + EnumChatFormatting.WHITE +" %s", getLa(),message);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(str));
    }

    public void print(String message,String suffix) {

        String str = String.format(EnumChatFormatting.DARK_RED + "[%s]" + EnumChatFormatting.GRAY+ ":" + EnumChatFormatting.WHITE +" %s", suffix,message);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(str));
    }

    @EventTarget
    public void onKey(EventKey event) {
        for (Module module : moduleManager.getElements()) {
            if (module.key == event.getKeyCode()) module.setStatus(!module.isToggle());
        }

        if (event.getKeyCode() == Keyboard.KEY_L) {
            configsManager.load();
        }

        if (event.getKeyCode() == Keyboard.KEY_RSHIFT) {
            Minecraft.getMinecraft().displayGuiScreen(configScreen);
        }
    }

    public static io.justme.lavender.La getINSTANCE() {
        return INSTANCE;
    }
}
