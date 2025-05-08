package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module;

import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.NavBarType;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/8
 **/
@Getter
@Setter
public abstract class AbstractModulePanel {

    public float x,y,width,height;
    public CategoryType type;
    public ModulePanelType panelType;
    public int requestHeight = 0;

    public AbstractModulePanel(CategoryType type) {
        this.type = type;
    }

    public AbstractModulePanel(CategoryType type ,ModulePanelType panelType) {
        this.type = type;
        this.panelType = panelType;
    }

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract boolean mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
    public abstract void handleMouseInput() throws IOException;


}
