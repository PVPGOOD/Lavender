package io.justme.lavender.module.impl.blatant.misc;

import io.justme.lavender.La;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.render.Event2DRender;
import io.justme.lavender.events.render.Event3DRender;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Set;

/**
 * 火球预警模块
 * @author JustMe.
 * @since 2025/5/10
 **/

@ModuleInfo(name = "FireBallWarning", category = Category.MISC, description = "检测火球是否靠近并推送通知")
public class FireBallWarning extends Module {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final Set<EntityFireball> notifiedFireballs = new HashSet<>();

    @EventTarget
    public void onMotion(EventMotionUpdate eventMotionUpdate) {
        for (Object entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityFireball fireball)) continue;

            BlockPos explosionPos = predictExplosion(fireball, 100);
            if (explosionPos == null) continue;

            double playerX = mc.thePlayer.posX;
            double playerY = mc.thePlayer.posY;
            double playerZ = mc.thePlayer.posZ;

            double distanceToExplosion = explosionPos.distanceSq(playerX, playerY, playerZ);
            if (distanceToExplosion <= 16) { // 玩家在爆炸范围内
                if (!notifiedFireballs.contains(fireball)) {
                    sendNotification("烈焰蛋预警", "你当前的位置会被烈焰蛋炸到 请做好准备！", NotificationsEnum.WARNING, 2700);
                    notifiedFireballs.add(fireball);
                }
                continue;
            }

            double playerMotionX = mc.thePlayer.motionX;
            double playerMotionY = mc.thePlayer.motionY;
            double playerMotionZ = mc.thePlayer.motionZ;

            for (double t = 0; t < 100; t += 0.1) {
                double predictedPlayerX = playerX + playerMotionX * t;
                double predictedPlayerY = playerY + playerMotionY * t;
                double predictedPlayerZ = playerZ + playerMotionZ * t;

                double predictedDistance = explosionPos.distanceSq(predictedPlayerX, predictedPlayerY, predictedPlayerZ);
                if (predictedDistance <= 16) { // 玩家会进入爆炸范围
                    if (!notifiedFireballs.contains(fireball)) {
                        sendNotification("烈焰蛋预警", "你正朝爆炸点移动！", NotificationsEnum.WARNING, 2700);
                        notifiedFireballs.add(fireball);
                    }
                    break;
                }
            }
        }

        // 移除已消失的火球
        notifiedFireballs.removeIf(fireball -> !mc.theWorld.loadedEntityList.contains(fireball));
    }

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

            double xDiff = targetX - mineX;
            double zDiff = targetZ - mineZ;
            double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
            double currentAngle = Math.atan2(zDiff, xDiff);

            double cos = Math.cos(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
            double sin = Math.sin(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
            double rotY = -(zDiff * cos - xDiff * sin);
            double rotX = -(xDiff * cos + zDiff * sin);
            float angle = (float) (Math.atan2(rotY, rotX) * 180 / Math.PI);
            float x = (float) ((120 * Math.cos(Math.toRadians(angle))) + width / 2f - 10);
            float y = (float) ((120 * Math.sin(Math.toRadians(angle))) + height / 2f);

            // 绘制火球图标
            GlStateManager.pushMatrix();
            GlStateManager.translate(x - 8, y - 8, 0);
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/items/fireball.png"));
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 32, 32, 32, 32);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    @EventTarget
    public void onRender3D(Event3DRender event) {
        for (Object entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityFireball fireball)) continue;

            double fireballX = fireball.posX + (fireball.posX - fireball.lastTickPosX) * mc.timer.renderPartialTicks;
            double fireballY = fireball.posY + (fireball.posY - fireball.lastTickPosY) * mc.timer.renderPartialTicks;
            double fireballZ = fireball.posZ + (fireball.posZ - fireball.lastTickPosZ) * mc.timer.renderPartialTicks;

            double motionX = fireball.motionX;
            double motionY = fireball.motionY;
            double motionZ = fireball.motionZ;

            // 发射射线
            double maxDistance = 100; // 最大检测距离
            for (double t = 0; t < maxDistance; t += 0.1) {
                double predictedX = fireballX + motionX * t;
                double predictedY = fireballY + motionY * t;
                double predictedZ = fireballZ + motionZ * t;

                // 检测是否与非空气方块碰撞
                var blockPos = new BlockPos(predictedX, predictedY, predictedZ);
                var block = mc.theWorld.getBlockState(blockPos).getBlock();
                if (block.isCollidable() && !block.getMaterial().isReplaceable()) {
                    // 碰撞点即为预测点
                    drawExplosionRadius(predictedX, predictedY, predictedZ);
                    drawTrajectory(fireballX, fireballY, fireballZ, predictedX, predictedY, predictedZ);
                    break;
                }
            }
        }
    }

    private void drawTrajectory(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        double playerX = mc.getRenderManager().viewerPosX;
        double playerY = mc.getRenderManager().viewerPosY;
        double playerZ = mc.getRenderManager().viewerPosZ;

        double renderStartX = startX - playerX;
        double renderStartY = startY - playerY;
        double renderStartZ = startZ - playerZ;

        double renderEndX = endX - playerX;
        double renderEndY = endY - playerY;
        double renderEndZ = endZ - playerZ;

        // 绘制飞行轨迹
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.color(0.0F, 1.0F, 0.0F, 0.7F); // 半透明绿色

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(renderStartX, renderStartY, renderStartZ); // 起点
        GL11.glVertex3d(renderEndX, renderEndY, renderEndZ); // 终点
        GL11.glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private BlockPos predictExplosion(EntityFireball fireball, double maxDistance) {
        double fireballX = fireball.posX + (fireball.posX - fireball.lastTickPosX) * mc.timer.renderPartialTicks;
        double fireballY = fireball.posY + (fireball.posY - fireball.lastTickPosY) * mc.timer.renderPartialTicks;
        double fireballZ = fireball.posZ + (fireball.posZ - fireball.lastTickPosZ) * mc.timer.renderPartialTicks;

        double motionX = fireball.motionX;
        double motionY = fireball.motionY;
        double motionZ = fireball.motionZ;

        for (double t = 0; t < maxDistance; t += 0.1) {
            double predictedX = fireballX + motionX * t;
            double predictedY = fireballY + motionY * t;
            double predictedZ = fireballZ + motionZ * t;

            BlockPos blockPos = new BlockPos(predictedX, predictedY, predictedZ);
            var block = mc.theWorld.getBlockState(blockPos).getBlock();
            if (block.isCollidable() && !block.getMaterial().isReplaceable()) {
                return blockPos;
            }
        }
        return null;
    }

    private boolean isPlayerInRange(BlockPos pos, double range) {
        double playerX = mc.thePlayer.posX;
        double playerY = mc.thePlayer.posY;
        double playerZ = mc.thePlayer.posZ;

        double distanceSq = pos.distanceSq(playerX, playerY, playerZ);
        return distanceSq <= range * range;
    }

    private void sendNotification(String title, String message, NotificationsEnum type, int duration) {
        La.getINSTANCE().getNotificationsManager().push(title, message, type, duration);
    }

    private void drawExplosionRadius(double x, double y, double z) {
        double playerX = mc.getRenderManager().viewerPosX;
        double playerY = mc.getRenderManager().viewerPosY;
        double playerZ = mc.getRenderManager().viewerPosZ;

        double renderX = x - playerX;
        double renderY = y - playerY;
        double renderZ = z - playerZ;

        // 绘制爆炸范围
        GlStateManager.pushMatrix();
        GlStateManager.translate(renderX, renderY, renderZ);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 0.0F, 0.0F, 0.3F);
        drawSphere(0, 0, 0, 4, 16, 16);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public void drawSphere(double x, double y, double z, float radius, int slices, int stacks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);


        for (int i = 0; i <= stacks; i++) {
            double lat = Math.PI * (-0.5 + (double) i / stacks);
            double z0 = Math.sin(lat) * radius;
            double zr0 = Math.cos(lat) * radius;

            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (int j = 0; j <= slices; j++) {
                double lng = 2 * Math.PI * (double) j / slices;
                double x1 = Math.cos(lng) * zr0;
                double y1 = Math.sin(lng) * zr0;

                GL11.glVertex3d(x1, y1, z0);
            }
            GL11.glEnd();
        }

        for (int j = 0; j <= slices; j++) {
            double lng = 2 * Math.PI * (double) j / slices;

            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (int i = 0; i <= stacks; i++) {
                double lat = Math.PI * (-0.5 + (double) i / stacks);
                double z0 = Math.sin(lat) * radius;
                double zr0 = Math.cos(lat) * radius;

                double x1 = Math.cos(lng) * zr0;
                double y1 = Math.sin(lng) * zr0;

                GL11.glVertex3d(x1, y1, z0);
            }
            GL11.glEnd();
        }

        GL11.glPopMatrix();
    }
}