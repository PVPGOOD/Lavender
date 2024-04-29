package io.justme.lavender.utility.math;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MouseUtility {

    public boolean isHovering(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public boolean isHovering(float[] vertices, int mouseX, int mouseY) {
        return mouseX >= vertices[0] && mouseX <= vertices[0] + vertices[2] && mouseY >= vertices[1] && mouseY <= vertices[1] + vertices[3];
    }

}
