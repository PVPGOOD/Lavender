package io.justme.lavender.ui.screens.clickgui.panels.popup;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.panels.AbstractPanel;
import io.justme.lavender.ui.screens.clickgui.components.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.components.impl.chill.ComBoxChill;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.MultiBoolValue;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/

//感觉有点啰嗦了...
@Getter
@Setter
public class PopupComBox extends AbstractPanel {

    private MultiBoolValue comBoxOption;
    private CopyOnWriteArrayList<AbstractControlsComponent> checkBoxChill = new CopyOnWriteArrayList<>();
    private ScaledResolution scaledResolution;

    public PopupComBox(MultiBoolValue comBoxOption) {
        setName("PopupComBox");
        setScaledResolution(new ScaledResolution(Minecraft.getMinecraft()));
        setComBoxOption(comBoxOption);

        for (BoolValue value : getComBoxOption().getValue()) {
            getCheckBoxChill().add(new ComBoxChill(value));
        }

        setScaledResolution(new ScaledResolution(Minecraft.getMinecraft()));

        setWidth(300);
        setHeight(300);
        setX(getScaledResolution().getScaledWidth() /2f - getWidth() /2f);
        setY(getScaledResolution().getScaledHeight() /2f - getHeight() /2f);
    }

    @Override
    public void initGui() {

        setScaledResolution(new ScaledResolution(Minecraft.getMinecraft()));

        setWidth(300);
        setHeight(300);
        setX(getScaledResolution().getScaledWidth() /2f - getWidth() /2f);
        setY(getScaledResolution().getScaledHeight() /2f - getHeight() /2f);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRect(0,0, getScaledResolution().getScaledWidth(), getScaledResolution().getScaledHeight(), new Color(0,0,0,64));
        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),10, La.getINSTANCE().getClickScreen().getClickGuiColor());

        int intervalY = 0;
        for (AbstractControlsComponent components : getCheckBoxChill()) {
            components.setX(getX());
            components.setY(getY() + intervalY);

            intervalY += 20;
            components.drawScreen(mouseX, mouseY, partialTicks);
        }


    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY)) {
            for (AbstractControlsComponent checkBoxChill : getCheckBoxChill()) {
                checkBoxChill.mouseClicked(mouseX,mouseY,mouseButton);
            }
        }

        if (MouseUtility.isHovering(getX() + getWidth(),getY(),20,20,mouseX,mouseY)) {
            La.getINSTANCE().getClickScreen().getAbstractPanels().remove(this);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY)) {
            for (AbstractControlsComponent checkBoxChill : getCheckBoxChill()) {
                checkBoxChill.mouseReleased(mouseX,mouseY,state);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            La.getINSTANCE().getClickScreen().getAbstractPanels().remove(this);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
