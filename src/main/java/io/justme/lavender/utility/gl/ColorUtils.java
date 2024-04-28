package io.justme.lavender.utility.gl;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ColorUtils {
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
}
