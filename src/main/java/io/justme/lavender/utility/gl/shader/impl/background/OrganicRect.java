package io.justme.lavender.utility.gl.shader.impl.background;

import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.shader.Shader;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class OrganicRect extends Shader {

    private float accumulatedTime = 0f;

    public OrganicRect() {
        super("organic.frag");
    }


    public void drawOrganic(float x, float y, float width, float height , float nscaleTL, float namountTL, float nscaleBR, float namountBR, float nscaleBL, float namountBL, float blendZone,float partialTicks, Color colorTL, Color colorBR, Color colorBL, Color bgColor) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        set();

        setUniformf("u_resolution",
                width  * scaledResolution.getScaleFactor(),
                height * scaledResolution.getScaleFactor());
        accumulatedTime += partialTicks;   
        float time = accumulatedTime / 20f;

        setUniformf("u_time", time);
        // 传递噪声参数
        setUniformf("u_nscaleTL",  nscaleTL);
        setUniformf("u_namountTL", namountTL);
        setUniformf("u_nscaleBR",  nscaleBR);
        setUniformf("u_namountBR", namountBR);
        setUniformf("u_nscaleBL",  nscaleBL);
        setUniformf("u_namountBL", namountBL);
        setUniformf("u_blend_zone_uv", blendZone);

        // 传递形状颜色（RGBA）
        setUniformf("u_shapeColorTL",
                colorTL.getRed()   / 255f,
                colorTL.getGreen() / 255f,
                colorTL.getBlue()  / 255f,
                colorTL.getAlpha() / 255f);
        setUniformf("u_shapeColorBR",
                colorBR.getRed()   / 255f,
                colorBR.getGreen() / 255f,
                colorBR.getBlue()  / 255f,
                colorBR.getAlpha() / 255f);
        setUniformf("u_shapeColorBL",
                colorBL.getRed()   / 255f,
                colorBL.getGreen() / 255f,
                colorBL.getBlue()  / 255f,
                colorBL.getAlpha() / 255f);

        // 传递背景色（RGB）
        setUniformf("u_bgColor",
                bgColor.getRed()   / 255f,
                bgColor.getGreen() / 255f,
                bgColor.getBlue()  / 255f);

        // 在目标区域绘制一个全屏四边形，Shader 会根据 uv 计算
        RenderUtility.drawQuads(x, y, width, height);

        // 解绑、恢复状态
        reset();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
