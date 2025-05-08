package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module;

import io.justme.lavender.ui.screens.clickgui.dropdown.AbstractPanelUI;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.AbstractNavBar;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2025/5/8
 **/
//整体 非单个
@Getter
@Setter
public class ModulePanel extends AbstractPanelUI {

    private final CopyOnWriteArrayList<AbstractModulePanel> elements = new CopyOnWriteArrayList<>();

    public ModulePanel() {
        getElements().add(new ModuleList(CategoryType.FIGHT));
        getElements().add(new ModuleList(CategoryType.MOVEMENTS));
        getElements().add(new ModuleList(CategoryType.PLAYER));
        getElements().add(new ModuleList(CategoryType.VISUAL));
        getElements().add(new ModuleList(CategoryType.World));
        getElements().add(new ModuleList(CategoryType.MISC));
        getElements().add(new ModuleList(CategoryType.Exploit));
    }

    @Override
    public void initGui() {
        setX(0);
        setY(0);
        setWidth(10);
        setHeight(10);

        var intervalX = 0;

        for (AbstractModulePanel element : getElements()) {
            element.initGui();
            element.setX(140 + intervalX);
            element.setY(120);
            intervalX += 180;
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (AbstractModulePanel element : getElements()) {
            element.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractModulePanel element : getElements()) {
            element.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractModulePanel element : getElements()) {
            element.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {
        for (AbstractModulePanel element : getElements()) {
            element.handleMouseInput();
        }
    }
}
