package io.justme.lavender.utility.gl.shader.impl;

import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.StencilUtility;
import io.justme.lavender.utility.gl.shader.Shader;
import io.justme.lavender.utility.math.MathUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class GaussianBlur extends Shader {

    public Framebuffer input = new Framebuffer(1, 1, false);

    public GaussianBlur() {
        super("gaussian.frag");
    }

    public void draw(float radius,float depth) {

        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        if (input == null || input.framebufferWidth != Minecraft.getMinecraft().displayWidth || input.framebufferHeight != Minecraft.getMinecraft().displayHeight) {
            if (input != null) {
                input.deleteFramebuffer();
                input = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
            }
            input = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
        }

        input.bindFramebuffer(true);
        set();

        var color = new Color(0);
        setUniformi("textureIn", 0);
        setUniformf("texelSize", 1.0F / (float) Minecraft.getMinecraft().displayWidth, 1.0F / (float) Minecraft.getMinecraft().displayHeight);
        setUniformf("radius", radius);
        setUniformf("r", color.getRed() /2550f);
        setUniformf("g", color.getGreen() /25f);
        setUniformf("b", color.getBlue() /255f);
        setUniformf("depth", depth);

        var weights = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weights.put(MathUtility.calculateGaussianValue(i, radius));
        }

        setUniformf("direction", 1, 0);
        weights.rewind();
        GL20.glUniform1fv(getUniform("weights"), weights);
        glBindTexture(GL_TEXTURE_2D, Minecraft.getMinecraft().getFramebuffer().framebufferTexture);
        RenderUtility.drawQuads();
        input.unbindFramebuffer();
        GL20.glUseProgram(GL_NONE);

        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);

        set();
        setUniformf("direction", 0, 1);

        weights.rewind();
        GL20.glUniform1fv(getUniform("weights"), weights);
        glBindTexture(GL_TEXTURE_2D, input.framebufferTexture);
        RenderUtility.drawQuads();
        reset();

        GlStateManager.bindTexture(GL_NONE);
    }

    public void run(Runnable runnable,float radius,float depth) {


        GL11.glPushMatrix();
        StencilUtility.initStencilToWrite();
        runnable.run();
        StencilUtility.readStencilBuffer(1);
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        draw(radius,depth);
        StencilUtility.uninitStencilBuffer();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

}
