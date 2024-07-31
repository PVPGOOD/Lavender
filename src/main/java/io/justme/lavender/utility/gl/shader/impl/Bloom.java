package io.justme.lavender.utility.gl.shader.impl;

import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.shader.Shader;
import io.justme.lavender.utility.math.MathUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.awt.*;

import static net.minecraft.client.renderer.OpenGlHelper.glUniform1;
import static org.lwjgl.opengl.GL11.*;

public class Bloom extends Shader {

    public Framebuffer input = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
    public Framebuffer output = new Framebuffer(mc.displayWidth, mc.displayHeight, true);

    public Bloom() {
        super("bloom.frag");
    }

    public void draw(int sourceTexture, int radius, int offset) {
        if (input == null || input.framebufferWidth != Minecraft.getMinecraft().displayWidth || input.framebufferHeight != Minecraft.getMinecraft().displayHeight) {
            if (input != null) {
                input.deleteFramebuffer();
                input = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);

                output.deleteFramebuffer();
                output = new Framebuffer(Minecraft.getMinecraft().displayWidth,  Minecraft.getMinecraft().displayHeight, true);
            }
            input = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
        }

        var weights = BufferUtils.createFloatBuffer(256);

        for (int i = 0; i <= radius; i++) {
            weights.put(MathUtility.calculateGaussianValue(i, radius));
        }
        weights.flip();

        var color = new Color(0, 0, 0);


        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (0 * .01));

        input.framebufferClear();
        input.bindFramebuffer(true);
        set();

        setUniformf("r", color.getRed() / 255f);
        setUniformf("g",  color.getGreen() / 255f);
        setUniformf("b",  color.getBlue() / 255f);

        setUniformi("inTexture", 0);
        setUniformi("textureToCheck", 16);
        setUniformf("radius", radius);
        setUniformf("texelSize", 1.0F / (float) Minecraft.getMinecraft().displayWidth, 1.0F / (float) Minecraft.getMinecraft().displayHeight);
        setUniformf("direction", offset, 0);


        glUniform1(getUniform("weights"), weights);

        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        glBindTexture(GL_TEXTURE_2D, sourceTexture);

        RenderUtility.drawQuads();
        reset();
        input.unbindFramebuffer();

        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);

        set();

        setUniformf("direction", 0, offset);
        glUniform1(getUniform("weights"), weights);

        GL13.glActiveTexture(GL13.GL_TEXTURE16);
        glBindTexture(GL_TEXTURE_2D, sourceTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, input.framebufferTexture);
        RenderUtility.drawQuads();

        reset();

        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();

        GlStateManager.bindTexture(0);
    }

    public Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != Minecraft.getMinecraft().displayWidth || framebuffer.framebufferHeight != Minecraft.getMinecraft().displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
        }
        return framebuffer;
    }

    public void run(Runnable runnable) {
        output = createFrameBuffer(output);
        output.framebufferClear();
        output.bindFramebuffer(true);
        runnable.run();
        output.unbindFramebuffer();
        draw(output.framebufferTexture, 8, 3);
    }

    public void run(Runnable runnable,boolean reDraw) {
        output = createFrameBuffer(output);
        output.framebufferClear();
        output.bindFramebuffer(true);
        runnable.run();
        output.unbindFramebuffer();
        draw(output.framebufferTexture, 8, 3);

        runnable.run();
    }

}

