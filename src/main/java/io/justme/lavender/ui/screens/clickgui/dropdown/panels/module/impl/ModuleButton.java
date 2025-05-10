package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.impl;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.AbstractModulePanel;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.ModulePanelType;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value.ModuleValuePanel;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2025/5/8
 **/
@Getter
@Setter
public class ModuleButton extends AbstractModulePanel {

    private Module module;
    private ArrayList<ModuleValuePanel> valuePanels = new ArrayList<>();
    private boolean expanded;

    public ModuleButton(CategoryType type, ModulePanelType panelType,Module module) {
        super(type, panelType);
        this.module = module;

        getValuePanels().add(new ModuleValuePanel(module));
    }


    @Override
    public void initGui() {

    }

    private Animation expandAnimation = new Animation();
    private Animation expandRoundedAnimation = new Animation();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        var font = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        var background_color = module.isToggle() ? new Color(0xD0BCFE) : new Color(0xEADDFF);

        var valueResque = 0f;
        for (ModuleValuePanel valuePanel : getValuePanels()) {
            valuePanel.setX(getX());
            valuePanel.setY(getY() + getHeight() - 5);
            valuePanel.setWidth(getWidth());
            valuePanel.setHeight(getExpandAnimation().getValue() - 5);
            valuePanel.drawScreen(mouseX, mouseY, partialTicks);
            valueResque = valuePanel.getRequestHeight() + 25;
        }


        RenderUtility.drawRoundRectWithCustomRounded(getX(), getY(), getWidth(), getHeight(), background_color,getExpandRoundedAnimation().getValue(),getExpandRoundedAnimation().getValue(),20,20);
        font.drawString(module.getName(),getX() + 8,getY() + getHeight() /2f - font.getHeight()/2f + 3,new Color(0,0,0,155).getRGB());

        setWidth(110);
        setHeight(25);

        setRequestHeight(getExpandAnimation().getValue());
        getExpandAnimation().animate(isExpanded() ? valueResque : 0,0.1f, Easings.EXPO_OUT);
        getExpandRoundedAnimation().animate(isExpanded() ? 0 : 20,0.07f);

        getExpandRoundedAnimation().update();
        getExpandAnimation().update();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isExpanded()) {

            for (ModuleValuePanel valuePanel : getValuePanels()) {
                valuePanel.mouseClicked(mouseX, mouseY, mouseButton);
            }

            return MouseUtility.isHovering(getX(),getY(),getWidth(),25,mouseX,mouseY);
        } else {
            return isHover(mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        if (isExpanded()) {

            for (ModuleValuePanel valuePanel : getValuePanels()) {
                valuePanel.mouseReleased(mouseX, mouseY, state);
            }

            return MouseUtility.isHovering(getX(),getY(),getWidth(),25,mouseX,mouseY);
        } else {
            return isHover(mouseX, mouseY);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {
        for (ModuleValuePanel valuePanel : getValuePanels()) {
            valuePanel.handleMouseInput();
        }
    }
}
