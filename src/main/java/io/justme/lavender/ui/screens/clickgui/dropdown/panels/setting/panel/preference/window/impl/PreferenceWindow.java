package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.window.impl;

import io.justme.lavender.La;
import io.justme.lavender.setting.SettingPreferenceType;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.AbstractSettingComponent;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.impl.*;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.window.AbstractPreferenceWindow;
import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JustMe.
 * @since 2025/5/30
 **/
@Getter
@Setter
public class PreferenceWindow extends AbstractPreferenceWindow {


    public SettingPreferenceType type;
    private CopyOnWriteArrayList<AbstractSettingComponent> valueComponents = new CopyOnWriteArrayList<>();

    public PreferenceWindow(SettingPreferenceType type) {
        super(type);
        this.type = type;

    }

    @Override
    public void initGui() {
        getValueComponents().clear();

        for (var entry : La.getINSTANCE().getSettingManager().getSettingList().entrySet()) {
            if (entry.getKey().equals(type)) {
                for (DefaultValue<?> setting : entry.getValue()) {
                    var component = getComponent(setting);
                    getValueComponents().add(component);
                }
                break;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        //值
        var intervalY = new AtomicInteger();
        var initY = 6;
        var initX = 5;
        for (AbstractSettingComponent abstractOptionComponent : getValueComponents()) {
            if (!abstractOptionComponent.getOption().isAvailable()) {
                continue;
            }

            switch (abstractOptionComponent.getModuleComponentType()) {
                case MODE -> {
                    abstractOptionComponent.setX(getX() + initX + 5);
                    abstractOptionComponent.setY(getY() + initY + intervalY.get() + 18);

                    abstractOptionComponent.setDescriptionX(getX() + initX);
                    abstractOptionComponent.setDescriptionY(getY() + initY + intervalY.get());
                    abstractOptionComponent.drawScreen(mouseX, mouseY, partialTicks);

                    intervalY.addAndGet((int) (38 + abstractOptionComponent.getModeExpandingHeight()));
                }
                case SLIDER -> {
                    abstractOptionComponent.setX(getX() + initX);
                    abstractOptionComponent.setY(getY() + initY + intervalY.get() + 20);
                    abstractOptionComponent.setDescriptionX(getX() + initX);
                    abstractOptionComponent.setDescriptionY(getY() + initY + intervalY.get());

                    abstractOptionComponent.drawScreen(mouseX, mouseY, partialTicks);

                    intervalY.addAndGet(45);
                }
                case SLIDER_RANGE -> {
                    abstractOptionComponent.setX(getX() + initX + getWidth() - abstractOptionComponent.getWidth());
                    abstractOptionComponent.setY(getY() + initY + intervalY.get() + 15);

                    abstractOptionComponent.setDescriptionX(getX() + initX);
                    abstractOptionComponent.setDescriptionY(getY() + initY + intervalY.get());

                    abstractOptionComponent.drawScreen(mouseX, mouseY, partialTicks);

                    intervalY.addAndGet(45);
                }
                case SWITCH -> {
                    abstractOptionComponent.setX(getX() + initX + getWidth() / 2f);
                    abstractOptionComponent.setY(getY() + initY + intervalY.get());

                    abstractOptionComponent.setDescriptionX(getX() + initX);
                    abstractOptionComponent.setDescriptionY(getY() + initY + intervalY.get() + 2.5f);

                    abstractOptionComponent.drawScreen(mouseX, mouseY, partialTicks);
                    intervalY.addAndGet(15);
                }
                case SELECTION -> {
                    abstractOptionComponent.setDescriptionX(getX() + initX);
                    abstractOptionComponent.setDescriptionY(getY() + initY + intervalY.get());

                    abstractOptionComponent.setX(getX() + initX + 5);
                    abstractOptionComponent.setY(getY() + initY + intervalY.get());
                    abstractOptionComponent.drawScreen(mouseX, mouseY, partialTicks);

                    intervalY.addAndGet(abstractOptionComponent.getRequestInterval());
                }
                case CHECKBOX -> {
                    abstractOptionComponent.setDescriptionX(getX() + initX + abstractOptionComponent.getHeight() /2f );
                    abstractOptionComponent.setDescriptionY(getY() + initY + intervalY.get() + 2);

                    abstractOptionComponent.drawScreen(mouseX, mouseY, partialTicks);
                    intervalY.addAndGet(15);
                }
            }
        }

        setHeight(intervalY.get());
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractSettingComponent abstractSettingComponent : getValueComponents()) {
            if (!abstractSettingComponent.getOption().isAvailable()) {
                continue;
            }

            abstractSettingComponent.mouseClicked(mouseX, mouseY, mouseButton);
        }

        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {

        for (AbstractSettingComponent abstractSettingComponent : getValueComponents()) {
            if (!abstractSettingComponent.getOption().isAvailable()) {
                continue;
            }

            abstractSettingComponent.mouseReleased(mouseX, mouseY, state);
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
            component.setOption(setting);
        } else if (setting instanceof MultiBoolValue) {
            SelectionComponent selectionComponent = new SelectionComponent();
            selectionComponent.setOption((MultiBoolValue) setting);
            selectionComponent.afterAddOption();
            component = selectionComponent;
            component.setOption(setting);
        } else if (setting instanceof NumberValue) {
            var sliderComponent = new SliderComponent();
            sliderComponent.setOption((NumberValue) setting);
            component = sliderComponent;

        } else if (setting instanceof NumberRangeValue) {
            var sliderRangeComponent = new SliderRangeComponent();
            sliderRangeComponent.setOption((NumberRangeValue) setting);
            component = sliderRangeComponent;
            component.setOption(setting);
        }

        else if (setting instanceof ModeValue) {
            var modeComponent = new ModeComponent();
            modeComponent.setOption((ModeValue) setting);
            modeComponent.afterAddOption();
            component = modeComponent;
            component.setOption(setting);
        }


        return component;
    }
}
