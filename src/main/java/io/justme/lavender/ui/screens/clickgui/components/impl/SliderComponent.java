package io.justme.lavender.ui.screens.clickgui.components.impl;

import io.justme.lavender.ui.screens.clickgui.components.AbstractOptionComponent;
import io.justme.lavender.ui.screens.clickgui.components.ComponentType;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.utility.math.animation.util.Easings;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import org.lwjglx.input.Mouse;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/
@Getter
@Setter
public class SliderComponent extends AbstractOptionComponent {

    private NumberValue option = new NumberValue("slider",2,2,10f,1);
    private boolean dragging;

    public SliderComponent() {
        this.componentType = ComponentType.SLIDER;
    }

    @Override
    public void initGui() {

    }

    private Animation sliderAnimations = new Animation();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        getFontDrawer().drawString(getOption().getName(),getDescriptionX(),getDescriptionY(),new Color(0).getRGB());

        float inc = getOption().getIncrement();
        float max = getOption().getMax();
        float min = getOption().getMin();
        float value = getOption().getValue();
        float posX = getX();
        float longValue = getX() - (getX() - getWidth());

        getSliderAnimations().animate((longValue * (value - min) / (max - min)),0.1F, Easings.LINEAR);

        //背景
        RenderUtility.drawRoundRect( posX, getY(), getWidth(), getHeight(), 3, new Color(125, 131, 136));

        //值
        RenderUtility.drawRoundRect(posX, getY(), getSliderAnimations().getValue(), getHeight(), 3,new Color(255, 233, 240, 255));

        if (isDragging()) {
            float valAbs = mouseX - (posX);
            float percent = calculatePercentage(valAbs, longValue, value, max);
            float val = calculateAdjustedValue(percent, max, min, inc);
            getOption().setValue(val);
        }

        //点
        int size = 6;
        RenderUtility.drawRoundRectWithOutline(posX + getSliderAnimations().getValue() - size,
                getY() + getHeight()/2f - size, size * 2, size * 2, 6, 0.5f,new Color(255, 255, 255,255),new Color(0,0,0,64));


        if (MouseUtility.isHovering( posX, getY(), getWidth(), getHeight(),mouseX,mouseY)&& Mouse.isButtonDown(0)) {
            setDragging(true);
        }

        setWidth(100);
        setHeight(5);
        getSliderAnimations().update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        switch (state) {
            case 0:
                setDragging(false);
                break;
        }
    }


    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }

    private float calculatePercentage(float valAbs, float longValue, float value, float max) {
        float percent = valAbs / (longValue * Math.max(Math.min(value / max, 0), 1));
        return Math.min(Math.max(0, percent), 1);
    }

    private float calculateAdjustedValue(float percent, float max, float min, float inc) {
        float valRel = (max - min) * percent;
        float val = min + valRel;
        return Math.round(val * (1 / inc)) / (1 / inc);
    }

}
