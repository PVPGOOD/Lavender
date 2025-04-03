package io.justme.lavender.ui.elements.quadrant;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;


/**
 * @author JustMe.
 * @since 2025/4/3
 **/
@Getter
@Setter
public class Quadrant {
    private final float centerX;
    private final float centerY;

    public Quadrant() {
        var scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        this.centerX = scaledResolution.getScaledWidth() / 2f;
        this.centerY = scaledResolution.getScaledHeight() / 2f;
    }

    public QuadrantEnum getQuadrant(float posX, float posY) {
        if (posX >= centerX && posY < centerY) {
            return QuadrantEnum.FIRST;
        } else if (posX < centerX && posY < centerY) {
            return QuadrantEnum.SECOND;
        } else if (posX < centerX && posY >= centerY) {
            return QuadrantEnum.THIRD;
        } else {
            return QuadrantEnum.FOURTH;
        }
    }
}