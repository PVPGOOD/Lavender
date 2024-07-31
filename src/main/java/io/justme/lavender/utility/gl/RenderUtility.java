package io.justme.lavender.utility.gl;

import io.justme.lavender.utility.gl.shader.interfaces.Shader;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

@UtilityClass
public class RenderUtility {

    public void drawRect(float x, float y, float width, float height, int color) {
        rectangle(x, y,x + width,y + height, color);
    }

    public void drawRect(float x, float y, float width, float height, Color color) {
        rectangle(x, y,x + width,y + height, color.getRGB());
    }

    public void drawRoundRect(float x, float y, float width, float height, float radius, Color color) {
        Shader.roundRect.drawRound(x, y, width, height, radius, color);
    }

    public void drawRoundRectWithCustomRounded(float x, float y, float width, float height,Color color, float topRadius,float buttonRadius, float leftRadius,float rightRadius) {
        Shader.roundCustomRadiusRect.drawRoundCustomRadiusRect(x, y, width, height,color,topRadius,buttonRadius,leftRadius,rightRadius);
    }

    public void drawRoundRectWithOutline(float x, float y, float width, float height, float radius, float outlineThickness, Color color, Color outlineColor) {
        Shader.roundOutlineRect.drawRoundOutline(x, y, width, height, radius,outlineThickness, color,outlineColor);
    }


    private void rectangle(float left, float top, float right, float bottom, int color)
    {
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        var minX = Math.min(left, right);
        var maxX = Math.max(left, right);
        var minY = Math.min(top, bottom);
        var maxY = Math.max(top, bottom);

        var alpha = (float) (color >> 24 & 255) / 255.0F;
        var red = (float) (color >> 16 & 255) / 255.0F;
        var green = (float) (color >> 8 & 255) / 255.0F;
        var blue = (float) (color & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(minX, maxY, 0.0D).endVertex();
        worldrenderer.pos(maxX, maxY, 0.0D).endVertex();
        worldrenderer.pos(maxX, minY, 0.0D).endVertex();
        worldrenderer.pos(minX, minY, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public void drawQuads(float x, float y, float width, float height) {
        if (Minecraft.getMinecraft().gameSettings.ofFastRender) return;
        OGLUtility.render(GL_QUADS ,() -> {
            glTexCoord2f(0, 0);
            glVertex2f(x, y);
            glTexCoord2f(0, 1);
            glVertex2f(x, y + height);
            glTexCoord2f(1, 1);
            glVertex2f(x + width, y + height);
            glTexCoord2f(1, 0);
            glVertex2f(x + width, y);
        });
    }

    public void drawQuads() {
        var sr = new ScaledResolution(Minecraft.getMinecraft());
        var width = (float) sr.getScaledWidth_double();
        var height = (float) sr.getScaledHeight_double();

        OGLUtility.render(GL11.GL_QUADS, () -> {
            GL11.glTexCoord2f(0F, 0F);
            GL11.glVertex2d(0, height);
            GL11.glTexCoord2f(1F, 0F);
            GL11.glVertex2d(width, height);
            GL11.glTexCoord2f(1F, 1F);
            GL11.glVertex2d(width, 0);
            GL11.glTexCoord2f(0F, 1F);
            GL11.glVertex2d(0, 0);
        });
    }
}
