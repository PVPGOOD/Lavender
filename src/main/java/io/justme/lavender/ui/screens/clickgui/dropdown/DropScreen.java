package io.justme.lavender.ui.screens.clickgui.dropdown;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.ModulePanel;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.NavBarPanel;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.shader.interfaces.Shader;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2025/5/7
 **/
@Getter
@Setter
public class DropScreen extends GuiScreen {

    private ArrayList<AbstractPanelUI> abstractPanelUIS = new ArrayList<>();

    public DropScreen() {
        getAbstractPanelUIS().add(new NavBarPanel());
        getAbstractPanelUIS().add(new ModulePanel());
    }

    @Override
    public void initGui() {
        super.initGui();

        for (AbstractPanelUI abstractPanelUI : getAbstractPanelUIS()) {
            abstractPanelUI.initGui();
        }

        La.getINSTANCE().getConfigScreen().initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (AbstractPanelUI abstractPanelUI : getAbstractPanelUIS()) {
            abstractPanelUI.drawScreen(mouseX, mouseY, partialTicks);
        }

//        RenderUtility.drawRoundRectWithCustomRounded(0,0,mouseX,mouseY,new Color(255,255,255),25,25,25,25);


        La.getINSTANCE().getConfigScreen().drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);


        for (AbstractPanelUI abstractPanelUI : getAbstractPanelUIS()) {
            abstractPanelUI.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        for (AbstractPanelUI abstractPanelUI : getAbstractPanelUIS()) {
            abstractPanelUI.mouseClicked(mouseX, mouseY, mouseButton);
        }

        La.getINSTANCE().getConfigScreen().mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

        for (AbstractPanelUI abstractPanelUI : getAbstractPanelUIS()) {
            abstractPanelUI.mouseReleased(mouseX, mouseY, state);
        }

        La.getINSTANCE().getConfigScreen().mouseReleased(mouseX, mouseY,state);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        for (AbstractPanelUI abstractPanelUI : getAbstractPanelUIS()) {
            abstractPanelUI.handleMouseInput();
        }
        super.handleMouseInput();
    }
}
