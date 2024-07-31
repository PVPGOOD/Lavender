package io.justme.lavender.utility.gl.shader.impl.rect;

import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.shader.Shader;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RoundRect extends Shader {


    public RoundRect() {
        super("round.frag");
    }

    public void drawRound(float x, float y, float width, float height, float radius, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        set();

        setUniformf("size", width * scaledResolution.getScaleFactor(), height * scaledResolution.getScaleFactor());
        setUniformf("radius", radius * scaledResolution.getScaleFactor());
        setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        RenderUtility.drawQuads(x - 1, y - 1, width + 2, height + 2);
        reset();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

}
