package io.justme.lavender.value.impl;

import io.justme.lavender.value.DefaultValue;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.function.Supplier;

@Getter
@Setter
public class ColorValue extends DefaultValue<Boolean> {

    private float hue, saturation, brightness, alpha = 1F;


    public ColorValue(String name, Boolean value, Supplier<Boolean> dependency) {
        super(name, value, dependency);
    }

    public ColorValue(String name, Boolean value) {
        super(name, value);
    }


    public Color getColor() {
        return reAlpha(Color.getHSBColor(hue, saturation, brightness), getValue() ? Math.round(alpha * 255) : 255);
    }

    public ColorValue setColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hue = hsb[0];
        saturation = hsb[1];
        brightness= hsb[2];
        if (getValue()) alpha = color.getAlpha() / 255F;
        return this;
    }

    private Color reAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

}
