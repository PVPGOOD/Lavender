package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.componenets.impl.chill;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.componenets.AbstractOptionComponent;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.value.impl.ModeValue;
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
public class ModeChill extends AbstractOptionComponent {
    private String comBoxChillName;
    private ModeValue modeValue;

    public ModeChill(String chillName, ModeValue modeValue) {
        this.comBoxChillName = chillName;
        this.modeValue = modeValue;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (isHover(mouseX, mouseY)) {
            RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, new Color(201, 201, 201, 155));
        }

        if (getComBoxChillName().equalsIgnoreCase(getModeValue().getValue())) {
            RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, new Color(201, 201, 201, 155));
        }

        var fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        fontDrawer.drawString(getComBoxChillName(), getX() + 6, getY() + getHeight() / 2f - fontDrawer.getHeight()/2f + 3, new Color(0, 0, 0, 155).getRGB());
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