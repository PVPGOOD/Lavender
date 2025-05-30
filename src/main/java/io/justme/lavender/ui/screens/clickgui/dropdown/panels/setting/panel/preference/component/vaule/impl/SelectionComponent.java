package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.impl;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.AbstractSettingComponent;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.SettingComponentType;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.impl.chill.selection.SelectionBoxChill;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.MultiBoolValue;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/
@Getter
@Setter
public class SelectionComponent extends AbstractSettingComponent {

    public MultiBoolValue option;
    private CopyOnWriteArrayList<AbstractSettingComponent> selectionChill = new CopyOnWriteArrayList<>();


    public SelectionComponent() {
        this.moduleComponentType = SettingComponentType.SELECTION;
    }

    public void afterAddOption() {
        for (BoolValue boolValue : getOption().getValue()) {
            getSelectionChill().add(new SelectionBoxChill(boolValue));
        }
    }

    @Override
    public void initGui() {

    }

    private float interval = 0;
    private Animation expandingRadius = new Animation();
    private Animation expandingHeight = new Animation();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        var fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        fontDrawer.drawString(
                getOption().getName(),
                getDescriptionX() + 2,
                getDescriptionY() + getHeight() /2f - fontDrawer.getHeight() /2f,
                new Color(0,0,0,155).getRGB());



        var interval = 0;
        var posX = getX();
        var posY = getY() + getHeight() - 5;

                //背景
        RenderUtility.drawRoundRectWithOutline(
                posX,
                getY() + getHeight() - 8,
                getWidth(),
                getExpandingHeight().getValue(),
                10,1,new Color(248, 225, 234, 255),
                new Color(255, 230, 241, 255));

        for (AbstractSettingComponent chill : getSelectionChill()) {
            chill.setX(getX() + 4);
            chill.setY(getY() + getHeight() + interval);
            chill.setWidth(getWidth() - 8);
            chill.setHeight(17);
            chill.drawScreen(mouseX, mouseY, partialTicks);

            interval += 20;
        }


        setRequestInterval((int) (getHeight() + interval));
        getExpandingHeight().update();
        getExpandingRadius().update();
        setWidth(100);
        setHeight(25);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        for (AbstractSettingComponent chill : getSelectionChill()) {

            if (chill.isHover(mouseX, mouseY)) {
                chill.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractSettingComponent chill : getSelectionChill()) {

            if (chill.isHover(mouseX, mouseY)) {
                chill.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
