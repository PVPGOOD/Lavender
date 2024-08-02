package io.justme.lavender.utility.gl.shader.impl.rect;

import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.shader.Shader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * @author JustMe.
 * @since 2024/8/2
 **/
public class RoundRectTextured extends Shader {
    public RoundRectTextured() {
        super("textured.frag");
    }

    public void drawRoundTextured(ResourceLocation texId,float x, float y, float width, float height, float radius, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        set();

        setUniformf("size", width * scaledResolution.getScaleFactor(), height * scaledResolution.getScaleFactor());
        setUniformf("radius", radius * scaledResolution.getScaleFactor());

        Minecraft.getMinecraft().getTextureManager().bindTexture(texId);

        setUniformi("textureIn", 0);
        setupRoundedRectUniforms(x, y, width, height, radius);
        setUniformf("alpha", alpha);

        RenderUtility.drawQuads(x - 1, y - 1, width + 2, height + 2);
        reset();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void setupRoundedRectUniforms(float x, float y, float width, float height, float radius) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        setUniformf("location", x * sr.getScaleFactor(),
                (Minecraft.getMinecraft().displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        setUniformf("radius", radius * sr.getScaleFactor());
    }
}
