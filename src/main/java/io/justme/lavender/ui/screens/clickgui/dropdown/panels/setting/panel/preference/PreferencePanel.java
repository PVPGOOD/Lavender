package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.AbstractSetting;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.SettingType;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.window.AbstractPreferenceWindow;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.window.impl.PreferenceWindow;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;
import lombok.Setter;
import org.lwjglx.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JustMe.
 * @since 2025/5/29
 **/
@Getter
@Setter
public class PreferencePanel extends AbstractSetting {

    private int mouseX,mouseY;
    private float ScrollOffset = 0;
    private float maxScroll = 0;

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

    private float lastHeight = 300;
    private Animation scrollAnimation = new Animation();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),25, new Color(0xF8F2FD));

        var groupInterval = new AtomicInteger();
        int initY = 10;
        int leftSide = 3;
        int rightSide = 10;

        setScrollOffset(getScrollAnimation().getValue());

        for (AbstractPreferenceWindow abstractPreferenceWindow : getAbstractPreferenceWindows()) {
            abstractPreferenceWindow.setX(getX() + leftSide);
            float y1 = getY() + initY + groupInterval.get() + 15;

            if (abstractPreferenceWindow.getPositionYAnimation() == null) {
                abstractPreferenceWindow.setPositionYAnimation(new Animation(y1));
            }

            abstractPreferenceWindow.setY(abstractPreferenceWindow.getPositionYAnimation().getValue() + ScrollOffset);
            abstractPreferenceWindow.setWidth(getWidth() - rightSide);

            abstractPreferenceWindow.drawScreen(mouseX, mouseY, partialTicks);
            groupInterval.addAndGet((int) (abstractPreferenceWindow.getHeight() + 10));

            abstractPreferenceWindow.getPositionYAnimation().animate(y1,.05f);
            abstractPreferenceWindow.getPositionYAnimation().update();
        }

        setMouseX(mouseX);
        setMouseY(mouseY);

        setMaxScroll(getHeight());
        getScrollAnimation().update();
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
        if (isHover(getMouseX(),getMouseY())) {
            int scroll = Mouse.getEventDWheel();
            if (scroll != 0) {
                float targetOffset = ScrollOffset + scroll * 50;
                targetOffset = Math.max(-maxScroll, Math.min(0, targetOffset));
                scrollAnimation.animate(targetOffset, 0.1f, Easings.QUAD_OUT);
            }
        }
    }

}
