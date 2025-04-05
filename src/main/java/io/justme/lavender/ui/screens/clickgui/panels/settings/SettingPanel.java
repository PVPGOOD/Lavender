package io.justme.lavender.ui.screens.clickgui.panels.settings;

import io.justme.lavender.La;
import io.justme.lavender.setting.SettingType;
import io.justme.lavender.ui.screens.clickgui.panels.AbstractPanel;
import io.justme.lavender.ui.screens.clickgui.panels.settings.impl.SettingWindow;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2025/4/4
 **/
@Getter
@Setter
public class SettingPanel extends AbstractPanel {

    private final CopyOnWriteArrayList<AbstractSetting> abstractSettings = new CopyOnWriteArrayList<>();

    public SettingPanel() {
        setName("SettingPanel");

        getAbstractSettings().add(new SettingWindow(SettingType.GLOBAL_SETTING));
        getAbstractSettings().add(new SettingWindow(SettingType.RENDERING));
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        var titleHeight = 55;
        RenderUtility.drawRoundRectWithCustomRounded(getX(),getY(),getWidth(),titleHeight, new Color(255, 228, 238),0,0,15,15);

        var intervalY = titleHeight + 5;
        for (AbstractSetting abstractSetting : getAbstractSettings()) {
            abstractSetting.setX(getX());
            abstractSetting.setY(getY() + intervalY);
            abstractSetting.setWidth(getWidth());
            abstractSetting.drawScreen(mouseX, mouseY, partialTicks);
            intervalY += (int) abstractSetting.getRequestInterval();
        }

        var fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Bold22();
        fontDrawer.drawString("设置 ",getX() + 10,getY() + 10,new Color(129, 57, 80,128).getRGB());

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractSetting abstractSetting : getAbstractSettings()) {
            abstractSetting.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractSetting abstractSetting : getAbstractSettings()) {
            abstractSetting.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for (AbstractSetting abstractSetting : getAbstractSettings()) {
            abstractSetting.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
