package io.justme.lavender.utility.gl;

import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

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

}
