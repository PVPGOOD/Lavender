package io.justme.lavender.ui.screens.clickgui.controls.chill;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponents;
import io.justme.lavender.utility.gl.RenderUtility;
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
public class ModeChill extends AbstractControlsComponents {


    public ModeChill(String chillName) {
        this.comBoxChillName = chillName;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (isHover(mouseX,mouseY)) {
            RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),8, new Color(201, 201, 201, 155));
        }

        if (getComBoxChillName().equals(getComBoxSelectingName())) {
            RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),8, new Color(201, 201, 201, 155));
        }

        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Bold18();
        fontDrawer.drawString(getComBoxChillName(),getX() + 6,getY() + getHeight()/2f - 5,new Color(129, 57, 80).getRGB());
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
}
