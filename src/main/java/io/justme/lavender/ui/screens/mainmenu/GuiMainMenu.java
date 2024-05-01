package io.justme.lavender.ui.screens.mainmenu;

import io.justme.lavender.ui.screens.microsoft.GuiMicrosoftLogin;
import io.justme.lavender.ui.screens.multiplayer.GuiMultiplayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/4/10
 **/
public class GuiMainMenu extends GuiScreen {


    public GuiMainMenu() {

    }

    @Override
    public void initGui()
    {
        buttonList.clear();
        buttonList.add(new GuiButton(0, 20,  20,80,20, "Select World"));
        buttonList.add(new GuiButton(1, 20,  60,80,20, "Multi Player"));
        buttonList.add(new GuiButton(2, 20,  80,80,20, "Microsoft"));
        buttonList.add(new GuiButton(3, 20,  100,80,20, "Language"));
        buttonList.add(new GuiButton(4, 20,  120,80,20, "Options"));
        buttonList.add(new GuiButton(5, 20,  140,80,20, "ShutDown"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMicrosoftLogin(this));
        }

        if (button.id == 3) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 4) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 5) {
            Minecraft.getMinecraft().shutdown();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }

}
