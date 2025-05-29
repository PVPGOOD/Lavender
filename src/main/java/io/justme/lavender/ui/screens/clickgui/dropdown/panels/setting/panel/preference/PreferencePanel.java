package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.AbstractSetting;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.SettingType;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.AbstractSettingComponent;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.impl.*;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.ScissorUtility;
import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.*;
import lombok.Getter;
import lombok.Setter;

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

    private CopyOnWriteArrayList<AbstractSettingComponent> valueComponents = new CopyOnWriteArrayList<>();

    public PreferencePanel(SettingType type) {
        super(type);

        getValueComponents().clear();

        for (DefaultValue<?> setting : La.getINSTANCE().getSettingManager().getSettingList()) {
            var component = getComponent(setting);
            getValueComponents().add(component);
        }
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),25, new Color(0xF8F2FD));

        //值
        AtomicInteger intervalY = new AtomicInteger();
        int rightSide = 10;
        int leftSide = 3;
        int initY = 10;

        ScissorUtility.scissor(getX(),getY(),getWidth(),getHeight(), () -> {
            for (AbstractSettingComponent abstractOptionComponent : getValueComponents()) {
                switch (abstractOptionComponent.getModuleComponentType()) {
                    case MODE -> {
                        abstractOptionComponent.setX(getX() + 5);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY + 18);

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY );
                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                        intervalY.addAndGet((int) (38 + abstractOptionComponent.getModeExpandingHeight()));
                    }

                    case SLIDER -> {
                        abstractOptionComponent.setX(getX() + rightSide);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY + 20);

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY );

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                        intervalY.addAndGet(45);
                    }

                    case SLIDER_RANGE -> {
                        abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide + 5);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY  + 15);

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY );

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                        intervalY.addAndGet(45);
                    }

                    case SWITCH -> {
                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY );
                        abstractOptionComponent.setX(getX() + getWidth() /2f);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY );

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);
                        intervalY.addAndGet(25);
                    }

                    case SELECTION -> {
                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY );

                        abstractOptionComponent.setX(getX() +  leftSide  + 5);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY);

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                        intervalY.addAndGet(abstractOptionComponent.getRequestInterval());
                    }

                    case CHECKBOX -> {

                        abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY );

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY + 2);

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);
                        intervalY.addAndGet(15);
                    }
                }

            }
        });

    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractSettingComponent valueComponent : getValueComponents()) {
            valueComponent.mouseClicked(mouseX, mouseY, mouseButton);
        }

        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {

        for (AbstractSettingComponent valueComponent : getValueComponents()) {
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

    private AbstractSettingComponent getComponent(DefaultValue<?> setting) {
        AbstractSettingComponent component = null;

        if (setting instanceof BoolValue)  {
            var switchComponent = new SwitchComponent();
            switchComponent.setOption((BoolValue) setting);
            switchComponent.afterAddOption();
            component = switchComponent;
        } else if (setting instanceof MultiBoolValue) {
            SelectionComponent selectionComponent = new SelectionComponent();
            selectionComponent.setOption((MultiBoolValue) setting);
            selectionComponent.afterAddOption();
            component = selectionComponent;
        } else if (setting instanceof NumberValue) {
            var sliderComponent = new SliderComponent();
            sliderComponent.setOption((NumberValue) setting);
            component = sliderComponent;
        } else if (setting instanceof NumberRangeValue) {
            var sliderRangeComponent = new SliderRangeComponent();
            sliderRangeComponent.setOption((NumberRangeValue) setting);
            component = sliderRangeComponent;
        }

        else if (setting instanceof ModeValue) {
            var modeComponent = new ModeComponent();
            modeComponent.setOption((ModeValue) setting);
            modeComponent.afterAddOption();
            component = modeComponent;
        }


        return component;
    }
}
