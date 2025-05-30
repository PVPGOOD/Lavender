package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.AbstractSetting;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.SettingType;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.window.AbstractPreferenceWindow;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.window.impl.PreferenceWindow;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.animation.Animation;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2025/5/29
 **/
@Getter
@Setter
public class PreferencePanel extends AbstractSetting {

    private CopyOnWriteArrayList<AbstractPreferenceWindow> abstractPreferenceWindows = new CopyOnWriteArrayList<>();

    public PreferencePanel(SettingType type) {
        super(type);


        getAbstractPreferenceWindows().clear();
        var settingList = La.getINSTANCE().getSettingManager().getSettingList();
        for (var entry : settingList.entrySet()) {
            PreferenceWindow window = new PreferenceWindow(entry.getKey());
            getAbstractPreferenceWindows().add(window);
        }

    }

    @Override
    public void initGui() {
        for (AbstractPreferenceWindow abstractPreferenceWindow : getAbstractPreferenceWindows()) {
            abstractPreferenceWindow.initGui();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),25, new Color(0xF8F2FD));

        var groupInterval = 0;

        int initY = 10;
        int leftSide = 3;
        int rightSide = 10;

        for (AbstractPreferenceWindow abstractPreferenceWindow : getAbstractPreferenceWindows()) {
            abstractPreferenceWindow.setX(getX() + leftSide);
            float y1 = getY() + initY + groupInterval + 15;

            if (abstractPreferenceWindow.getPositionYAnimation() == null) {
                abstractPreferenceWindow.setPositionYAnimation(new Animation(y1));
            }

            abstractPreferenceWindow.setY(abstractPreferenceWindow.getPositionYAnimation().getValue());
            abstractPreferenceWindow.setWidth(getWidth() - rightSide);

            abstractPreferenceWindow.drawScreen(mouseX, mouseY, partialTicks);
            groupInterval += (int) (abstractPreferenceWindow.getHeight() + 10);

            RenderUtility.drawRect(getX(), y1,getWidth(),.8f,new Color(0xCCE8DEF8, true));

            abstractPreferenceWindow.getPositionYAnimation().animate(y1,.05f);
            abstractPreferenceWindow.getPositionYAnimation().update();
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractPreferenceWindow valueComponent : getAbstractPreferenceWindows()) {
            valueComponent.mouseClicked(mouseX, mouseY, mouseButton);
        }

        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractPreferenceWindow valueComponent : getAbstractPreferenceWindows()) {
            valueComponent.mouseReleased(mouseX, mouseY, state);
        }

        return false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }

}
