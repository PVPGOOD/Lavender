package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value.impl;

import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value.AbstractOptionComponent;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value.ModuleComponentType;


import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.value.impl.MultiBoolValue;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/
@Getter
@Setter
public class ComBoxComponent extends AbstractOptionComponent {

    public MultiBoolValue option;

    public ComBoxComponent() {
        this.moduleComponentType = ModuleComponentType.COMBOX;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRectWithOutline(getX(),getY(),getWidth(),getHeight(),8,1,new Color(211, 188, 253, 255),new Color(215, 198, 215, 255));

        FontDrawer fontDrawer = getFontDrawer();
        fontDrawer.drawString(getOption().getName() ,
                getX() + getWidth() /2f - fontDrawer.getStringWidth(getOption().getName()) /2f,
                getY() + getHeight()/2f - fontDrawer.getHeight()/2f + 3,
                new Color(0, 0, 0,155).getRGB());

        setWidth(100);
        setHeight(25);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {


    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
