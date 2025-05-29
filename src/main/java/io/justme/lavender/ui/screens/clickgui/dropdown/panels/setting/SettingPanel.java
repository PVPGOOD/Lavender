package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.dropdown.AbstractPanelUI;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.category.CategoryPanel;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2025/5/26
 **/
@Getter
@Setter
public class SettingPanel extends AbstractPanelUI {

    private ScaledResolution scaledResolution;
    private CopyOnWriteArrayList<AbstractSetting> settings = new CopyOnWriteArrayList<>();


    public SettingPanel() {
        setShowing(false);
        getSettings().add(new CategoryPanel(SettingType.CATEGORY_PANEL));
    }

    @Override
    public void initGui() {
        for (AbstractSetting setting : getSettings()) {
            setting.initGui();
        }

        initializeDimensions();
    }

    public void initializeDimensions() {
        setScaledResolution(new ScaledResolution(Minecraft.getMinecraft()));
        setWidth(520);
        setHeight(450);
        setX(getScaledResolution().getScaledWidth() / 2f - getWidth() / 2f);
        setY(getScaledResolution().getScaledHeight() / 2f - getHeight() / 2f);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),20,new Color(253, 248, 255));

        var font = La.getINSTANCE().getFontManager().getPingFang_Medium18();

//        font.drawString("Setting Panel", getX() + 10, getY() + 10, new Color(29, 27, 32, 191).getRGB());
        int abstractComponentInitY = 30;
        int categoryWidth = 115;
        for (AbstractSetting setting : getSettings()) {

            switch (setting.getType()) {
                case CATEGORY_PANEL -> {
                    setting.setX(getX());
                    setting.setY(getY());
                    setting.setWidth(categoryWidth);
                    setting.setHeight(getHeight());
                    setting.drawScreen(mouseX, mouseY, partialTicks);
                }
            }

            setting.drawScreen(mouseX, mouseY, partialTicks);
        }


        initializeDimensions();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractSetting setting : getSettings()) {
            setting.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractSetting setting : getSettings()) {
            setting.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for (AbstractSetting setting : getSettings()) {
            setting.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
