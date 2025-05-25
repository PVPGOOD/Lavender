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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
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
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
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


    public void drawLine(Vec3 from, Vec3 to, float red, float green, float blue, float alpha) {
        GlStateManager.pushMatrix();

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);

        GlStateManager.color(red, green, blue, alpha);
        GL11.glLineWidth(2.0f);

        Vec3 renderPos = getRenderPos();
        double fx = from.xCoord - renderPos.xCoord;
        double fy = from.yCoord - renderPos.yCoord;
        double fz = from.zCoord - renderPos.zCoord;
        double tx = to.xCoord - renderPos.xCoord;
        double ty = to.yCoord - renderPos.yCoord;
        double tz = to.zCoord - renderPos.zCoord;

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(fx, fy, fz);
        GL11.glVertex3d(tx, ty, tz);
        GL11.glEnd();

        drawPoint(to, red, green, blue, alpha);

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }



    public void drawBox(AxisAlignedBB box, float r, float g, float b, float a) {
        var mc = Minecraft.getMinecraft();

        var renderManager = mc.getRenderManager();

        double renderX = renderManager.viewerPosX;
        double renderY = renderManager.viewerPosY;
        double renderZ = renderManager.viewerPosZ;

        Vec3 renderPos = new Vec3(renderX, renderY, renderZ);

        double minX = box.minX - renderPos.xCoord;
        double minY = box.minY - renderPos.yCoord;
        double minZ = box.minZ - renderPos.zCoord;
        double maxX = box.maxX - renderPos.xCoord;
        double maxY = box.maxY - renderPos.yCoord;
        double maxZ = box.maxZ - renderPos.zCoord;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GL11.glLineWidth(2.0F);
        GlStateManager.color(r, g, b, a);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(minX, maxY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(minX, maxY, minZ);

        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);

        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(maxX, maxY, maxZ);

        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public void drawPoint(Vec3 vec, float r, float g, float b, float a) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.color(r, g, b, a);
        GL11.glPointSize(6f);

        GL11.glBegin(GL11.GL_POINTS);
        Minecraft mc = Minecraft.getMinecraft();
        var renderManager = mc.getRenderManager();

        double renderX = renderManager.viewerPosX;
        double renderY = renderManager.viewerPosY;
        double renderZ = renderManager.viewerPosZ;

        Vec3 renderPos = new Vec3(renderX, renderY, renderZ);

        GL11.glVertex3d(vec.xCoord - renderPos.xCoord, vec.yCoord - renderPos.yCoord, vec.zCoord - renderPos.zCoord);
        GL11.glEnd();

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public void drawZeroDayMark() {
        Minecraft mc = Minecraft.getMinecraft();

        double partialTicks = mc.timer.renderPartialTicks;
        double interpX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTicks;
        double interpY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTicks;
        double interpZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTicks;

        double renderX = interpX - mc.getRenderManager().viewerPosX;
        double renderY = interpY - mc.getRenderManager().viewerPosY;
        double renderZ = interpZ - mc.getRenderManager().viewerPosZ;

        double eyeHeight = mc.thePlayer.getEyeHeight() + 0.45 - (mc.thePlayer.isSneaking() ? 0.25 : 0.0);
        double baseY = renderY + eyeHeight;

        var black = new Color(0, 0, 0, 255);
        var orange = new Color(255, 165, 0, 255);

        double x1 = renderX - 0.65;
        double z1 = renderZ - 0.65;

        double x2 = renderX - 0.5;
        double z2 = renderZ - 0.5;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);

        GL11.glLineWidth(2.0f);

        GL11.glColor4f(black.getRed() / 255.0f,
                black.getGreen() / 255.0f,
                black.getBlue() / 255.0f,
                black.getAlpha() / 255.0f);
        drawOutlinedBoundingBox(new AxisAlignedBB(x1, baseY - 2, z1, x1 + 1.3, baseY - 2, z1 + 1.3));
        drawOutlinedBoundingBox(new AxisAlignedBB(x2, baseY - 2, z2, x2 + 1, baseY - 2, z2 + 1));

        if (mc.gameSettings.keyBindJump.isKeyDown() || mc.thePlayer.moveForward != 0) {
            GL11.glColor4f(orange.getRed() / 255.0f,
                    orange.getGreen() / 255.0f,
                    orange.getBlue() / 255.0f,
                    orange.getAlpha() / 255.0f);

            drawOutlinedBoundingBox(new AxisAlignedBB(x1, baseY - 2, z1, x1 + 1.3, baseY - 2, z1 + 1.3));
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }


    public void drawOutlinedBoundingBox(final AxisAlignedBB aa) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }



    private Vec3 getRenderPos() {
        return new Vec3(Minecraft.getMinecraft().getRenderManager().viewerPosX, Minecraft.getMinecraft().getRenderManager().viewerPosY, Minecraft.getMinecraft().getRenderManager().viewerPosZ);
    }

}
