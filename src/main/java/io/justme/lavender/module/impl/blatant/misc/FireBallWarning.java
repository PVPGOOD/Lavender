package io.justme.lavender.module.impl.blatant.misc;

import io.justme.lavender.events.render.Event2DRender;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.gl.RenderUtility;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * @author JustMe.
 * @since 2025/5/10
 **/


@ModuleInfo(name = "FireBallWarning", category = Category.MISC, description = "")
public class FireBallWarning extends Module {

    private final Minecraft mc = Minecraft.getMinecraft();

    private double lastDistance = -1;
    @EventTarget
    public void onRender2D(Event2DRender event) {
        ScaledResolution sr = event.getScaledResolution();
        int width = sr.getScaledWidth(), height = sr.getScaledHeight();

        for (Object entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityFireball fireball)) continue;

            double mineX = mc.thePlayer.posX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks;
            double mineZ = mc.thePlayer.posZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks;
            double targetX = fireball.posX + (fireball.posX - fireball.lastTickPosX) * mc.timer.renderPartialTicks;
            double targetZ = fireball.posZ + (fireball.posZ - fireball.lastTickPosZ) * mc.timer.renderPartialTicks;

            double xDiff = (targetX - mineX);
            double zDiff = (targetZ - mineZ);

            double cos = Math.cos(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
            double sin = Math.sin(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));

            double rotY = -(zDiff * cos - xDiff * sin);
            double rotX = -(xDiff * cos + zDiff * sin);

            double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);

            float angle = (float) (Math.atan2(rotY, rotX) * 180 / Math.PI);
            float x = (float) ((120 * Math.cos(Math.toRadians(angle))) + width / 2f - 10);
            float y = (float) ((120 * Math.sin(Math.toRadians(angle))) + height / 2f);

            GlStateManager.pushMatrix();
            GlStateManager.translate(x - 8, y - 8, 0); // 调整位置以使贴图居中
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/items/fireball.png")); // 原版火球贴图路径
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 32, 32, 32, 32); // 使用原版方法绘制32x32的贴图
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();

            int color = lastDistance != -1 && distance < lastDistance ? Color.RED.getRGB() : Color.GREEN.getRGB();
            lastDistance = distance;

            String distanceText = String.format("[%.1f]", distance);
            drawText(distanceText, (int) x + 30, (int) y + 5, color);
        }
    }

    private double[] project2D(double x, double y, double z, int screenWidth, int screenHeight) {
        // 将 3D 坐标转换为 2D 屏幕坐标
        float scale = 0.5F / (float) Math.tan(Math.toRadians(mc.gameSettings.fovSetting / 2.0F));
        double aspectRatio = (double) screenWidth / screenHeight;

        double px = x * scale / aspectRatio;
        double py = y * scale;

        if (z > 0) {
            double screenX = screenWidth / 2.0 + px * screenWidth / 2.0;
            double screenY = screenHeight / 2.0 - py * screenHeight / 2.0;
            return new double[]{screenX, screenY};
        }
        return null;
    }

    private void drawText(String text, int x, int y, int color) {
        mc.fontRendererObj.drawStringWithShadow(text, x, y, color);
    }

}
