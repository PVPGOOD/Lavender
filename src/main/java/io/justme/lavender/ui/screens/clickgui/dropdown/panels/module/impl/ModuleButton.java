package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.impl;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.AbstractModulePanel;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.ModulePanelType;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/8
 **/
@Getter
@Setter
public class ModuleButton extends AbstractModulePanel {

    private Module module;

    public ModuleButton(CategoryType type, ModulePanelType panelType,Module module) {
        super(type, panelType);

        this.module = module;
    }


    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        var font = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        var background_color = module.isToggle() ? new Color(0xD0BCFE) : new Color(0xEADDFF);

        RenderUtility.drawRoundRectWithCustomRounded(getX(), getY(), getWidth(), getHeight(), background_color,20,20,20,20);

        font.drawString(module.getName(),getX() + 8,getY() + getHeight() /2f - font.getHeight()/2f + 3,new Color(0,0,0,155).getRGB());

        setWidth(110);
        setHeight(25);

    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        return false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
