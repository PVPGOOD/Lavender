package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value;

import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value.impl.*;
import io.justme.lavender.utility.ScissorHelper;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JustMe.
 * @since 2025/5/8
 **/
@Getter
@Setter
public class ModuleValuePanel extends AbstractModuleValue {


    private float draggingX,draggingY,scalingWidth, scalingHeight;

    private CopyOnWriteArrayList<AbstractOptionComponent> valueComponents = new CopyOnWriteArrayList<>();

    public ModuleValuePanel(Module module) {
        super(module);

        getValueComponents().clear();

        for (DefaultValue<?> setting : getModule().getOptions()) {
            var component = getComponent(setting);
            getValueComponents().add(component);
        }
    }


    @Override
    public void initGui() {

    }

    private Animation expandAnimation = new Animation();

    private final Animation scrollAnimation = new Animation();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRectWithCustomRounded(getX(),getY(),getWidth(),getHeight(),  new java.awt.Color(0xE8DEF8),25,25,0,0);

        //值
        AtomicInteger intervalY = new AtomicInteger();
        int rightSide = 10;
        int leftSide = 3;
        int initY = 10;

        ScissorHelper.scissor(getX(),getY(),getWidth(),getHeight(), () -> {
            for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
                switch (abstractOptionComponent.getModuleComponentType()) {
                    case MODE -> {
                        abstractOptionComponent.setX(getX() + 5);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY + 18);

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY );
                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                        intervalY.addAndGet((int) (38 + abstractOptionComponent.getModeExpandingHeight()));
                    }

                    case COMBOX -> {
                        abstractOptionComponent.setX(getX() + getWidth() /2f - abstractOptionComponent.getWidth() /2f);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY );

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY );

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                        intervalY.addAndGet(30);
                    }

                    case SLIDER -> {
                        abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide + 5);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY  + 15);

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY );

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                        intervalY.addAndGet(40);
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
                        abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY );

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() +initY );

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);
                        intervalY.addAndGet(25);
                    }

                    case CHECKBOX -> {
                        abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY );

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY + 3);

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);
                        intervalY.addAndGet(15);
                    }
                }

            }
        });

        getScrollAnimation().update();
        setRequestHeight(intervalY.get());
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractOptionComponent valueComponent : getValueComponents()) {
            valueComponent.mouseClicked(mouseX, mouseY, mouseButton);
        }

        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {

        for (AbstractOptionComponent valueComponent : getValueComponents()) {
            valueComponent.mouseReleased(mouseX, mouseY, state);
        }

        return false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

        for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
            abstractOptionComponent.handleMouseInput();
        }
    }

    private AbstractOptionComponent getComponent(DefaultValue<?> setting) {
        AbstractOptionComponent component = null;

        if (setting instanceof BoolValue)  {
//            SwitchComponent switchComponent = new SwitchComponent();
//            switchComponent.setOption((BoolValue) setting);
//            switchComponent.afterAddOption();
//            component = switchComponent;
            var checkBoxComponent = new CheckboxComponent();
            checkBoxComponent.setOption((BoolValue) setting);
            component = checkBoxComponent;
        } else if (setting instanceof MultiBoolValue) {
            ComBoxComponent comBoxComponent = new ComBoxComponent();
            comBoxComponent.setOption((MultiBoolValue) setting);
            component = comBoxComponent;
        } else if (setting instanceof NumberValue) {
            SliderComponent sliderComponent = new SliderComponent();
            sliderComponent.setOption((NumberValue) setting);
            component = sliderComponent;
        } else if (setting instanceof NumberRangeValue) {
            SliderRangeComponent sliderRangeComponent = new SliderRangeComponent();
            sliderRangeComponent.setOption((NumberRangeValue) setting);
            component = sliderRangeComponent;
        }

        else if (setting instanceof ModeValue) {
            ModeComponent modeComponent = new ModeComponent();
            modeComponent.setOption((ModeValue) setting);
            modeComponent.afterAddOption();
            component = modeComponent;
        }


        return component;
    }
}
