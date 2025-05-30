package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.impl.chill.selection;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.AbstractSettingComponent;
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
public class SelectionBoxChill extends AbstractSettingComponent {

    private BoolValue boolOption;
    private SelectionCheckBox selectionCheckBox;

    public SelectionBoxChill(BoolValue boolOption) {
        this.boolOption = boolOption;

        SelectionCheckBox checkBox = new SelectionCheckBox();
        checkBox.setOption(boolOption);
        setSelectionCheckBox(checkBox);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        var fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        //description
        fontDrawer.drawString(
                getBoolOption().getName(),getX() + 15,getY(),new Color(0,0,0,155).getRGB());

        getSelectionCheckBox().setX(getX());
        getSelectionCheckBox().setY(getY());

        getSelectionCheckBox().drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {


        getSelectionCheckBox().mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        getSelectionCheckBox().mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
