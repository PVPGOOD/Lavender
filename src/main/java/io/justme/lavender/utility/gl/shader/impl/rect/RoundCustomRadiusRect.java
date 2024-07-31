package io.justme.lavender.utility.gl.shader.impl.rect;

import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.shader.Shader;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RoundCustomRadiusRect extends Shader {

    public RoundCustomRadiusRect() {
        super("customround.frag");
    }

    public void drawRoundCustomRadiusRect(float x, float y, float width, float height,Color color, float topRadius,float buttonRadius, float leftRadius,float rightRadius) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        set();

        setUniformf("u_size", width * scaledResolution.getScaleFactor(), height * scaledResolution.getScaleFactor());
        setUniformf("radiusTopLeft", topRadius);
        setUniformf("radiusTopRight", buttonRadius );
        setUniformf("radiusBottomLeft", leftRadius);
        setUniformf("radiusBottomRight", rightRadius );
        setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        RenderUtility.drawQuads(x, y, width, height);
        reset();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
