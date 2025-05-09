package io.justme.lavender.utility.gl;

import io.justme.lavender.utility.gl.shader.interfaces.Shader;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
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

    public void drawRoundedTexture(ResourceLocation texId, float x, float y, float width, float height, float radius, float alpha) {
        Shader.roundRectTextured.drawRoundTextured(texId,x, y, width, height, radius, alpha);
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

    public void drawCircle(double x, double y, float radius, int color) {

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);

        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        glPointSize(radius * 4);

        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        glColor4f(red, green, blue, alpha);

        OGLUtility.render(GL_POINTS, () -> glVertex2d(x, y));

        glEnable(GL_TEXTURE_2D);
        GlStateManager.disableBlend();
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

    public void drawImage(ResourceLocation image, float x, float y, float width, float height, Color color) {
        new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width,
                                                           float height, float textureWidth, float textureHeight) {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double) x, (double) (y + height), 0.0D)
                .tex((double) (u * f), (double) ((v + (float) height) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) (y + height), 0.0D)
                .tex((double) ((u + (float) width) * f), (double) ((v + (float) height) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) y, 0.0D)
                .tex((double) ((u + (float) width) * f), (double) (v * f1)).endVertex();
        worldrenderer.pos((double) x, (double) y, 0.0D).tex((double) (u * f), (double) (v * f1)).endVertex();
        tessellator.draw();
    }

}
