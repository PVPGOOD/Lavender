package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value.impl;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value.AbstractOptionComponent;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value.ModuleComponentType;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.utility.math.animation.util.Easings;
import io.justme.lavender.value.impl.NumberRangeValue;
import lombok.Getter;
import lombok.Setter;
import org.lwjglx.input.Mouse;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/9
 **/
@Getter
@Setter
public class SliderRangeComponent extends AbstractOptionComponent {

    private NumberRangeValue option;
    private boolean draggingMin;
    private boolean draggingMax;

    private Animation minSliderAnimation = new Animation();
    private Animation maxSliderAnimation = new Animation();

    public SliderRangeComponent() {
        this.moduleComponentType = ModuleComponentType.SLIDER_RANGE;
    }

    @Override
    public void initGui() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        var font = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        font.drawString(option.getName(), getDescriptionX() + 2, getDescriptionY(), new Color(0, 0, 0, 155).getRGB());

        float inc = option.getIncrement();
        float max = option.getAbsoluteMax();
        float min = option.getAbsoluteMin();
        float minValue = option.getLowerValue();
        float maxValue = option.getUpperValue();
        float posX = getX();
        float longValue = getWidth();

        // 动画更新
        minSliderAnimation.animate((longValue * (minValue - min) / (max - min)), 0.1F, Easings.LINEAR);
        maxSliderAnimation.animate((longValue * (maxValue - min) / (max - min)), 0.1F, Easings.LINEAR);

        // 背景
        RenderUtility.drawRoundRect(posX, getY(), getWidth(), getHeight(), 5, new Color(194, 194, 194,155));

        // 范围值
//        RenderUtility.drawRoundRectWithCustomRounded(
//                posX + minSliderAnimation.getValue() + 5, // 左边增加偏移量
//                getY(),
//                maxSliderAnimation.getValue() - minSliderAnimation.getValue() - 10, // 宽度减少两边的偏移量
//                getHeight(),
//                new Color(208, 188, 255, 255),
//                3, 3, 3, 3
//        );

        RenderUtility.drawRoundRect(
                posX + minSliderAnimation.getValue() + 5, // 左边增加偏移量
                getY(),
                maxSliderAnimation.getValue() - minSliderAnimation.getValue() - 10, // 宽度减少两边的偏移量
                getHeight(),3,
                new Color(208, 188, 255, 255));
        float circleSize = 1.5f;
        //最小值圆
        RenderUtility.drawCircle(
                posX - circleSize / 2f + 5,
                getY() + getHeight() / 2f,
                circleSize,  new Color(74, 68, 89, 255).getRGB());

        //最大值 圆
        RenderUtility.drawCircle(
                posX + getWidth() - circleSize / 2f - 5,
                getY() + getHeight() / 2f,
                circleSize, new Color(74, 68, 89, 255).getRGB());

        //中心圆
        RenderUtility.drawCircle(
                posX + getWidth() / 2f - circleSize / 2f,
                getY() + getHeight() / 2f, circleSize,
                new Color(74, 68, 89, 255).getRGB());

        // 拖动逻辑
        if (draggingMin) {
            float valAbs = mouseX - posX;
            float percent = calculatePercentage(valAbs, longValue);
            float val = calculateAdjustedValue(percent, max, min, inc);
            option.setLowerValue(Math.min(val, maxValue)); // 确保最小值不超过最大值
        }

        if (draggingMax) {
            float valAbs = mouseX - posX;
            float percent = calculatePercentage(valAbs, longValue);
            float val = calculateAdjustedValue(percent, max, min, inc);
            option.setUpperValue(Math.max(val, minValue)); // 确保最大值不低于最小值
        }

        // 最小值竖条
        var barWidth = 3;
        var barHeight = 25;
        RenderUtility.drawRoundRectWithOutline(posX + minSliderAnimation.getValue() - barWidth / 2f,
                getY() + getHeight() / 2f - barHeight / 2f, barWidth, barHeight, 2.2f, 0.01f,
                new Color(101, 85, 143, 255), new Color(0, 0, 0, 255));

        //最大值竖条
        RenderUtility.drawRoundRectWithOutline(posX + maxSliderAnimation.getValue() - barWidth / 2f,
                getY() + getHeight() / 2f - barHeight / 2f, barWidth, barHeight, 2.2f, 0.01f,
                new Color(101, 85, 143, 255), new Color(0, 0, 0, 255));

        if (MouseUtility.isHovering(posX + minSliderAnimation.getValue() - barWidth / 2f,
                getY() + getHeight() / 2f - barHeight / 2f, barWidth, barHeight, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            draggingMin = true;
        }

        if (MouseUtility.isHovering(posX + maxSliderAnimation.getValue() - barWidth / 2f,
                getY() + getHeight() / 2f - barHeight / 2f, barWidth, barHeight, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            draggingMax = true;
        }

        // 显示当前值
        font.drawString(String.format("%.2f - %.2f", minValue, maxValue),
                posX + getWidth() / 2f - font.getStringWidth(String.format("%.2f - %.2f", minValue, maxValue)) / 2f,
                getY() + getHeight() + 6, new Color(0, 0, 0, 128).getRGB());


        setWidth(100);
        setHeight(8);
        minSliderAnimation.update();
        maxSliderAnimation.update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            draggingMin = false;
            draggingMax = false;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    @Override
    public void handleMouseInput() throws IOException {
    }

    private float calculatePercentage(float valAbs, float longValue) {
        return Math.min(Math.max(valAbs / longValue, 0), 1);
    }

    private float calculateAdjustedValue(float percent, float max, float min, float inc) {
        float valRel = (max - min) * percent;
        float val = min + valRel;
        return Math.round(val / inc) * inc;
    }
}
