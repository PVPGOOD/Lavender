package io.justme.lavender.ui.screens.clickgui.dropdown;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.dropdown.element.navbar.NavBarElement;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.AbstractPanel;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.popup.PopupPanel;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
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

    private ArrayList<AbstractElementUI> abstractElementUIS = new ArrayList<>();

    public DropScreen() {
        getAbstractElementUIS().add(new NavBarElement());
    }

    @Override
    public void initGui() {
        super.initGui();

        for (AbstractElementUI abstractElementUI : getAbstractElementUIS()) {
            abstractElementUI.initGui();
        }

        La.getINSTANCE().getConfigScreen().initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        for (AbstractElementUI abstractElementUI : getAbstractElementUIS()) {
            abstractElementUI.drawScreen(mouseX, mouseY, partialTicks);
        }

        La.getINSTANCE().getConfigScreen().drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        for (AbstractElementUI abstractElementUI : getAbstractElementUIS()) {
            abstractElementUI.mouseClicked(mouseX, mouseY, mouseButton);
        }

        La.getINSTANCE().getConfigScreen().mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

        for (AbstractElementUI abstractElementUI : getAbstractElementUIS()) {
            abstractElementUI.mouseReleased(mouseX, mouseY, state);
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
        super.handleMouseInput();
    }
}
