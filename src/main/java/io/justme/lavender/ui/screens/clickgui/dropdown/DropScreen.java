package io.justme.lavender.ui.screens.clickgui.dropdown;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.ModulePanel;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.NavBarPanel;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.SettingPanel;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.impl.category.CategoryType;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2025/5/7
 **/
@Getter
@Setter
public class DropScreen extends GuiScreen {

    private CategoryType currentCategory = CategoryType.SETTING;
    private CopyOnWriteArrayList<AbstractPanelUI> abstractPanelUIS = new CopyOnWriteArrayList<>();
    private SettingPanel settingPanel = new SettingPanel();
    private NavBarPanel navBarPanel = new NavBarPanel();
    private ModulePanel modulePanel = new ModulePanel();


    public DropScreen() {
        getAbstractPanelUIS().add(getSettingPanel());
        getAbstractPanelUIS().add(getNavBarPanel());
        getAbstractPanelUIS().add(getModulePanel());
    }

    @Override
    public void initGui() {
        for (AbstractPanelUI abstractPanelUI : getAbstractPanelUIS()) {

            abstractPanelUI.initGui();
        }

        La.getINSTANCE().getConfigScreen().initGui();

        super.initGui();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (AbstractPanelUI abstractPanelUI : getAbstractPanelUIS()) {
            if (!abstractPanelUI.isShowing()) continue;

            abstractPanelUI.drawScreen(mouseX, mouseY, partialTicks);
        }

//        RenderUtility.drawRoundRectWithCustomRounded(0,0,mouseX,mouseY,new Color(255,255,255),25,25,25,25);


        La.getINSTANCE().getConfigScreen().drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (AbstractPanelUI abstractPanelUI : getAbstractPanelUIS()) {
            if (!abstractPanelUI.isShowing()) continue;

            abstractPanelUI.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        for (AbstractPanelUI abstractPanelUI : getAbstractPanelUIS()) {
            if (!abstractPanelUI.isShowing()) continue;

            abstractPanelUI.mouseClicked(mouseX, mouseY, mouseButton);
        }

        La.getINSTANCE().getConfigScreen().mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

        for (AbstractPanelUI abstractPanelUI : getAbstractPanelUIS()) {
            if (!abstractPanelUI.isShowing()) continue;

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
            if (!getSettingPanel().isShowing()) continue;

            abstractPanelUI.handleMouseInput();
        }
        super.handleMouseInput();
    }
}
