package io.justme.lavender.ui.screens.clickgui.imgui.panels.settings.impl;

import io.justme.lavender.La;
import io.justme.lavender.setting.SettingType;
import io.justme.lavender.ui.screens.clickgui.imgui.components.AbstractOptionComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.components.impl.ComBoxComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.components.impl.ModeComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.components.impl.SliderComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.components.impl.SwitchComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.settings.AbstractSetting;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.MultiBoolValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JustMe.
 * @since 2025/4/4
 **/
@Getter
@Setter
public class SettingWindow extends AbstractSetting {

    private CopyOnWriteArrayList<AbstractOptionComponent> valueComponents = new CopyOnWriteArrayList<>();
    private final Animation scrollAnimation = new Animation();
    private int mouseX,mouseY;
    private float ScrollOffset = 0;
    private float maxScroll = 0;

    public SettingWindow(SettingType settingType) {
        super(settingType);

//        我操我什么时候写这个了失忆了 2025/5/29


        //有settingType -> key get values
//        var settingManager = La.getINSTANCE().getSettingManager();
//        var settingHashMap = settingManager.getSettingTypeHashMap();
//
//        for (var entry : settingHashMap.entrySet()) {
//            if (entry.getKey().equals(settingType)) {
//                var settings = entry.getValue();
//                for (DefaultValue<?> setting : settings) {
//                    getValueComponents().add(getComponent(setting));
//                }
//            }
//        }
    }

    @Override
    public void initGui() {
        for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
            abstractOptionComponent.initGui();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(), 5, new Color(255, 228, 238));

        var fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Regular18();

        fontDrawer.drawString(getSettingType().getName(),
                getX() + 10,
                getY() + 10,
                new Color(129, 57, 80,255).getRGB());

        //值
        AtomicInteger intervalY = new AtomicInteger();
        setScrollOffset(getScrollAnimation().getValue());
        int rightSide = 10;
        int leftSide = 10;
        int initY = 30;
        for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
            switch (abstractOptionComponent.getComponentType()) {
                case MODE -> {
                    abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide);
                    abstractOptionComponent.setY(getY() + intervalY.get() + initY + ScrollOffset);

                    abstractOptionComponent.setDescriptionX(getX() + leftSide);
                    abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY + ScrollOffset);
                    abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                    intervalY.addAndGet((int) (35 + abstractOptionComponent.getModeExpandingHeight()));
                }

                case COMBOX -> {
                    abstractOptionComponent.setX(getX() + getWidth() /2f - abstractOptionComponent.getWidth() /2f);
                    abstractOptionComponent.setY(getY() + intervalY.get() + initY + ScrollOffset);

                    abstractOptionComponent.setDescriptionX(getX() + leftSide);
                    abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY + ScrollOffset);

                    abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                    intervalY.addAndGet(30);
                }

                case SLIDER -> {
                    var leftY = 15;
                    abstractOptionComponent.setX(getX() + rightSide + 5);
                    abstractOptionComponent.setY(getY() + intervalY.get() + initY + ScrollOffset + leftY);

                    abstractOptionComponent.setDescriptionX(getX() + leftSide);
                    abstractOptionComponent.setDescriptionY(getY() + intervalY.get() +initY + ScrollOffset - fontDrawer.getHeight() /2f + abstractOptionComponent.getHeight());

                    abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                    intervalY.addAndGet(25);
                }

                case SWITCH -> {
                    var leftY = 15;
                    abstractOptionComponent.setX(getX() + rightSide + 5);
                    abstractOptionComponent.setY(getY() + intervalY.get() + initY + ScrollOffset + leftY);

                    abstractOptionComponent.setDescriptionX(getX() + leftSide);
                    abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY + ScrollOffset);

                    abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);
                    intervalY.addAndGet(28 + leftY);
                }

                case CHECKBOX -> {
                    abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide);
                    abstractOptionComponent.setY(getY() + intervalY.get() + initY + ScrollOffset);

                    abstractOptionComponent.setDescriptionX(getX() + leftSide);
                    abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY + ScrollOffset);

                    abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);
                    intervalY.addAndGet(30);
                }
            }

        }

        setHeight(intervalY.get() + initY);
        setRequestInterval(intervalY.get() + initY + 15);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        switch (mouseButton) {
            case 0 -> {
                for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
                    abstractOptionComponent.mouseClicked(mouseX,mouseY,mouseButton);
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0){
            if (-getScrollOffset() > getMaxScroll()) {
                getScrollAnimation().animate(-getMaxScroll(),0.1f);
            }
        }

        for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
            abstractOptionComponent.mouseReleased(mouseX,mouseY,state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }

    private AbstractOptionComponent getComponent(DefaultValue<?> setting) {
        AbstractOptionComponent component = null;

        if (setting instanceof BoolValue)  {
            SwitchComponent switchComponent = new SwitchComponent();
            switchComponent.setOption((BoolValue) setting);
            switchComponent.afterAddOption();
            component = switchComponent;
        } else if (setting instanceof MultiBoolValue) {
            ComBoxComponent comBoxComponent = new ComBoxComponent();
            comBoxComponent.setOption((MultiBoolValue) setting);
            component = comBoxComponent;
        } else if (setting instanceof NumberValue) {
            SliderComponent sliderComponent = new SliderComponent();
            sliderComponent.setOption((NumberValue) setting);
            component = sliderComponent;
        } else if (setting instanceof ModeValue) {
            ModeComponent modeComponent = new ModeComponent();
            modeComponent.setOption((ModeValue) setting);
            modeComponent.afterAddOption();
            component = modeComponent;
        }


        return component;
    }
}
