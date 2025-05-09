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
import java.util.ArrayList;

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

//        RenderUtility.drawRoundRectWithCustomRounded(getX(),getY(),getWidth(),getHeight(), new java.awt.Color(32),0,0,32,32);

        var font = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        font.drawString(type.getName() , getX() + 30,getY() + getHeight() /2f + font.getHeight() /2f - 15, new Color(0,0,0, 166).getRGB());
        Shader.roundGradientRect.drawGradientVertical(getX(), getY() + getHeight(), getWidth(), 3, 0, new Color(255, 200, 222, 255), new Color(0, 0, 0, 26));

        RenderUtility.drawImage(new ResourceLocation("la/clickgui/category/" + type.getName() + ".png"),getX() + 8,getY() + getHeight() /2f - 8,16,16,new Color(0,0,0,200));
//        RenderUtility.drawc(new ResourceLocation("la/clickgui/imgui/shadow/menu_shadow_bottom.png"),getX(),getY() + getHeight(),getWidth(),5,new Color(0,0,0,255));

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
