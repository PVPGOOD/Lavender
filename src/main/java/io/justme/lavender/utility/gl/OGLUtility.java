package io.justme.lavender.utility.gl;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * @author JustMe.
 * @since 2024/4/28
 **/
@UtilityClass
public class OGLUtility {

    public void scale(float x, float y, float scale, Runnable runnable) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, 1);
        GL11.glTranslatef(-x, -y, 0);
        runnable.run();
        GL11.glPopMatrix();
    }

    public void scissor(float x, float y, float width, float height,Runnable runnable) {
        var scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (x * scaleFactor), (int) (Minecraft.getMinecraft().displayHeight - (y + height) * scaleFactor), (int) (width * scaleFactor), (int) ((height + 14) * scaleFactor));
        runnable.run();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    public void color(int hex) {
        GlStateManager.color(
                (hex >> 16 & 0xFF) / 255.0f,
                (hex >> 8 & 0xFF) / 255.0f,
                (hex & 0xFF) / 255.0f,
                (hex >> 24 & 0xFF) / 255.0f);
    }

    public void color(int r,int g,int b,int a) {
        GlStateManager.color(
                r / 255f,
                g / 255f,
                b / 255f,
                a / 255f);
    }
}
