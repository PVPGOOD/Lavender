package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value.impl.chill;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.imgui.components.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.components.impl.CheckboxComponent;
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
    private CheckboxComponent checkboxComponent;

    public ComBoxChill(BoolValue boolOption) {
        this.boolOption = boolOption;

        CheckboxComponent controls = new CheckboxComponent();
        controls.setOption(boolOption);
        setCheckboxComponent(controls);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Bold22();

        //背景
        fontDrawer.drawString(getBoolOption().getName(),getX() + 2,getY() + getHeight() /2f - fontDrawer.getHeight() /2f,new Color(0).getRGB());

        getCheckboxComponent().setX(getX());
        getCheckboxComponent().setY(getY() + getHeight());

        getCheckboxComponent().drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {


        getCheckboxComponent().mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        getCheckboxComponent().mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
