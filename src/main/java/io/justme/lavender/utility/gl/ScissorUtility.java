package io.justme.lavender.utility.gl;

import java.awt.Rectangle;
import java.util.ArrayDeque;
import java.util.Deque;

import lombok.experimental.UtilityClass;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

@UtilityClass
public class ScissorUtility {
    private class State {
        final boolean enabled;
        final Rectangle box;
        State(boolean enabled, Rectangle box) {
            this.enabled = enabled;
            this.box = box;
        }
    }

    private final Deque<State> stack = new ArrayDeque<>();

    /**
     * 在执行 runnable 期间，将渲染裁剪到 (x,y,width,height) 这一区域。
     * 支持多层嵌套，并能完整恢复到最初的开启/关闭状态和裁剪框。
     */
    public void scissor(float x, float y, float width, float height, Runnable runnable) {
        boolean wasEnabled = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);

        Rectangle prevBox;
        if (wasEnabled) {
            try (MemoryStack ms = MemoryStack.stackPush()) {
                IntBuffer buf = ms.mallocInt(4);
                GL11.glGetIntegerv(GL11.GL_SCISSOR_BOX, buf);
                prevBox = new Rectangle(buf.get(0), buf.get(1), buf.get(2), buf.get(3));
            }
        } else {
            prevBox = new Rectangle(
                    0,
                    0,
                    Minecraft.getMinecraft().displayWidth,
                    Minecraft.getMinecraft().displayHeight
            );
        }

        stack.push(new State(wasEnabled, prevBox));

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int scale = res.getScaleFactor();
        int sx = (int) (x * scale);
        int sy = Minecraft.getMinecraft().displayHeight - (int) ((y + height) * scale);
        int sw = (int) (width * scale);
        int sh = (int) (height * scale);

        int ix = Math.max(prevBox.x, sx);
        int iy = Math.max(prevBox.y, sy);
        int ir = Math.min(prevBox.x + prevBox.width, sx + sw);
        int it = Math.min(prevBox.y + prevBox.height, sy + sh);
        int iw = Math.max(0, ir - ix);
        int ih = Math.max(0, it - iy);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(ix, iy, iw, ih);

        runnable.run();

        State prev = stack.pop();
        if (prev.enabled) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(prev.box.x, prev.box.y, prev.box.width, prev.box.height);
        } else {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
    }
}
