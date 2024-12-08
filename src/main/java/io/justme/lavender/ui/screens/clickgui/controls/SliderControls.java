package io.justme.lavender.ui.screens.clickgui.controls;

import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractOptionComponent;
import io.justme.lavender.ui.screens.clickgui.controls.type.ControlsType;
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
public class SliderControls extends AbstractOptionComponent {

    private NumberValue option = new NumberValue("slider",2,2,10f,1);
    private boolean dragging;

    public SliderControls() {
        this.controlsType = ControlsType.SLIDER;
    }

    @Override
    public void initGui() {

    }

    private Animation sliderAnimations = new Animation();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        colorAnimation.update();
//        colorAnimation.animate(MouseUtility.isHovering(getX(), getY(), getWidth(), getHeight() + 15F, mouseX, mouseY) ? Color.WHITE : new Rectangle(getX(), getY(), getWidth(), getHeight(), -1).contains(mouseX, mouseY) ? new Color(77, 76, 81).brighter().brighter() : new Color(77, 76, 81), .3F, Easings.EXPO_OUT);

//        FontDrawer fontDrawer = La.getINSTANCE().
//        new Text(option.getName(), getRenderer(), getX(), getY(), colorAnimation.getColor().getRGB()).draw();
        float inc = getOption().getIncrement();
        float max = getOption().getMax();
        float min = getOption().getMin();
        float value = getOption().getValue();
        float posX = getX();
        float longValue = getX() - (getX() - getWidth());

        getSliderAnimations().animate((longValue * (value - min) / (max - min)),0.1F, Easings.LINEAR);

        //背景
        RenderUtility.drawRoundRect( posX, getY() + 16, getWidth(), getHeight(), 3, new Color(81, 87, 93));

        //值
        RenderUtility.drawRoundRect(posX, getY() + 16, getSliderAnimations().getValue(), getHeight(), 3, new Color(255, 255, 255));

        if (isDragging()) {
            float valAbs = mouseX - (posX);
            float percent = calculatePercentage(valAbs, longValue, value, max);
            float val = calculateAdjustedValue(percent, max, min, inc);
            getOption().setValue(val);
        }

        //点
        RenderUtility.drawRoundRectWithOutline(posX + getSliderAnimations().getValue() - 3,
                getY() + 15 - 1.5f, 8, 8, 4, 0.5f,new Color(255,255,255,255),new Color(0,0,0,64));


        if (MouseUtility.isHovering( posX, getY() + 16, getWidth(), getHeight(),mouseX,mouseY)&& Mouse.isButtonDown(0)) {
            setDragging(true);
        }

        //定值 自己修改
        setWidth(150);
        setHeight(3);

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
