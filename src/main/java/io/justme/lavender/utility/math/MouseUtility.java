package io.justme.lavender.utility.math;

import lombok.experimental.UtilityClass;
import org.lwjglx.input.Mouse;

@UtilityClass
public class MouseUtility {

    public boolean isHovering(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public boolean isHovering(float[] vertices, int mouseX, int mouseY) {
        return mouseX >= vertices[0] && mouseX <= vertices[0] + vertices[2] && mouseY >= vertices[1] && mouseY <= vertices[1] + vertices[3];
    }

    public int getScroll() {
        int scroll = Mouse.getDWheel();
        if (scroll > 0) return 1;   // 向上滚动
        if (scroll < 0) return -1;  // 向下滚动
        return 0;                  // 无滚动
    }

}
