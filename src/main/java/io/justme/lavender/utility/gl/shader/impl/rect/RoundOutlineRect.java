package io.justme.lavender.utility.gl.shader.impl.rect;

import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.shader.Shader;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RoundOutlineRect extends Shader {

    public RoundOutlineRect(){
        super("outline.frag");
    }

    public void drawRoundOutline(float x, float y, float width, float height, float radius, float outlineThickness, Color color, Color outlineColor) {
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        set();

        setUniformf("size", width * scaledResolution.getScaleFactor(), height * scaledResolution.getScaleFactor());
        setUniformf("radius", radius * scaledResolution.getScaleFactor());

        setUniformf("outlineThickness", outlineThickness);
        setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        setUniformf("outlineColor", outlineColor.getRed() / 255f, outlineColor.getGreen() / 255f, outlineColor.getBlue() / 255f, outlineColor.getAlpha() / 255f);
        RenderUtility.drawQuads(x, y , width, height);
        reset();

        GlStateManager.disableBlend();
    }

}
