package io.justme.lavender.utility.gl;

import io.justme.lavender.utility.math.MathUtility;
import lombok.experimental.UtilityClass;

import java.awt.*;

@UtilityClass
public final class ColorUtility {
    public final int RED = getRGB(255,0,0);
    public final int GREED = getRGB(0,255,0);
    public final int BLUE = getRGB(0,0,255);
    public final int WHITE = getRGB(255,255,255);
    public final int BLACK = getRGB(0,0,0);
    public final int NO_COLOR = getRGB(0,0,0,0);

    public int getRGB(int r, int g, int b) {
        return getRGB(r,g,b,255);
    }

    public int getRGB(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                (b & 0xFF);
    }

    public int[] splitRGB(int rgb) {
        final int[] ints = new int[3];

        ints[0] = (rgb >> 16) & 0xFF;
        ints[1] = (rgb >> 8) & 0xFF;
        ints[2] = rgb & 0xFF;

        return ints;
    }

    public int getRGB(int rgb) {
        return 0xff000000 | rgb;
    }

    public int reAlpha(int rgb,int alpha) {
        return getRGB(getRed(rgb),getGreen(rgb),getBlue(rgb),alpha);
    }

    public int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    public int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    public int getBlue(int rgb) {
        return rgb & 0xFF;
    }

    public int getAlpha(int rgb) {
        return (rgb >> 24) & 0xff;
    }

    public int fadeBetween(double index, double speed, int startColor, int endColor) {
        double progress = Math.ceil((System.currentTimeMillis() + index * 100.0) / speed) % 360F / 180F;
        return fadeBetween(startColor, endColor, (float) progress);
    }

    public int fadeBetween(int startColor, int endColor, float progress) {
        if (progress > 1F) progress = 1F - progress % 1F;
        return fadeTo(startColor, endColor, progress);
    }

    public int fadeTo(int startColor, int endColor, float progress) {
        double invert = 1F - progress;
        int r = (int) ((startColor >> 16 & 0xFF) * invert + (endColor >> 16 & 0xFF) * progress);
        int g = (int) ((startColor >> 8 & 0xFF) * invert + (endColor >> 8 & 0xFF) * progress);
        int b = (int) ((startColor & 0xFF) * invert + (endColor & 0xFF) * progress);
        int a = (int) ((startColor >> 24 & 0xFF) * invert + (endColor >> 24 & 0xFF) * progress);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public int interpolateColor(int color1, int color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));
        Color cColor1 = new Color(color1);
        Color cColor2 = new Color(color2);
        return interpolateColorC(cColor1, cColor2, amount).getRGB();
    }

    public Color interpolateColorC(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));
        return new Color(MathUtility.interpolateInt(color1.getRed(), color2.getRed(), amount),
                MathUtility.interpolateInt(color1.getGreen(), color2.getGreen(), amount),
                MathUtility.interpolateInt(color1.getBlue(), color2.getBlue(), amount),
                MathUtility.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }
}
