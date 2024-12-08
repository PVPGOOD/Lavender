package io.justme.lavender.ui.screens.clickgui.controls.chill;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.controls.CheckboxControls;
import io.justme.lavender.value.impl.BoolValue;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/

//checkbox组件组合? XDD
@Getter
@Setter
public class ComBoxChill extends AbstractControlsComponent {

    private BoolValue boolOption;
    private CheckboxControls checkboxControls;

    public ComBoxChill(BoolValue boolOption) {
        this.boolOption = boolOption;

        CheckboxControls controls = new CheckboxControls();
        controls.setOption(boolOption);
        setCheckboxControls(controls);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Bold22();

        //背景
        fontDrawer.drawString(getBoolOption().getName(),getX() + 2,getY() + getHeight() /2f - fontDrawer.getHeight() /2f,new Color(0).getRGB());

        getCheckboxControls().setX(getX());
        getCheckboxControls().setY(getY() + getHeight());

        getCheckboxControls().drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {


        getCheckboxControls().mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        getCheckboxControls().mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
