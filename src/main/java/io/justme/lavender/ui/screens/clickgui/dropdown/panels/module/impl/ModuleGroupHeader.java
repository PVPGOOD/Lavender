package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.impl;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.AbstractModulePanel;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.ModulePanelType;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.shader.interfaces.Shader;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/8
 **/
@Getter
@Setter
public class ModuleGroupHeader extends AbstractModulePanel {


    public ModuleGroupHeader(CategoryType type, ModulePanelType panelType) {
        super(type, panelType);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        var font = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        font.drawString(type.getName() , getX() + 30,getY() + getHeight() /2f - font.getHeight() /2f + 3, new Color(0,0,0, 166).getRGB());

        if (isExpanded()) {
            Shader.roundGradientRect.drawGradientVertical(getX(), getY() + getHeight(), getWidth(), 3, 0, new Color(255, 200, 222, 255), new Color(0, 0, 0, 26));
        }

        var iconWidth = 12;
        var iconHeight = 12;
        RenderUtility.drawImage(new ResourceLocation("la/clickgui/category/md/" + type.getName() + ".png"),
                getX() + iconWidth/2f + 3,getY() + getHeight() /2f - iconHeight/2f,iconWidth,iconHeight,new Color(0,0,0,200));
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
