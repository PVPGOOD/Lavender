package io.justme.lavender.module.impl.blatant.misc;

import io.justme.lavender.La;
import io.justme.lavender.events.player.EventSendUseItem;
import io.justme.lavender.events.render.Event3DRender;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import io.justme.lavender.value.impl.BoolValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

/**
 * 投射物轨迹预测模块
 */
@Getter
@ModuleInfo(name = "ProjectilePrediction", description = "Predicts the trajectory of projectiles.", category = Category.VISUAL)
public class ProjectilePrediction extends Module {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final BoolValue voidCheck = new BoolValue("VoidCheck", true);

    @EventTarget
    public void onRightClick(EventSendUseItem eventSendUseItem) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (mc.thePlayer.getHeldItem() == null) return;
        if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemEnderPearl)) return;

        Vec3 startPos = mc.thePlayer.getPositionVector().addVector(0, mc.thePlayer.getEyeHeight(), 0);
        Vec3 velocity = getInitialVelocity();

        Vec3 landingPoint = simulateTrajectoryAndGetLandingPoint(startPos, velocity);
        if (landingPoint == null) {
            // 没有有效落点，取消事件
            La.getINSTANCE().getNotificationsManager().push(
                    "ProjectilePrediction (Validation)",
                    "预测到这不是一个有效的落点",
                    NotificationsEnum.WARNING,
                    3000
            );
            eventSendUseItem.setCancelled(true);
            return;
        }

        BlockPos belowBlockPos = new BlockPos(landingPoint.xCoord, landingPoint.yCoord - 1, landingPoint.zCoord);
        boolean isVoid = true;

        for (int i = 0; i < 255; i++) {
            BlockPos currentPos = belowBlockPos.down(i);
            if (mc.theWorld.getBlockState(currentPos).getBlock().getMaterial() != net.minecraft.block.material.Material.air) {
                isVoid = false;
                break;
            }
        }

        if (landingPoint.yCoord <= 0 || isVoid) {
            if (getVoidCheck().getValue()) {
                La.getINSTANCE().getNotificationsManager().push(
                        "ProjectilePrediction (VoidCheck)",
                        "预测到这颗珍珠将会导致你掉落到虚空",
                        NotificationsEnum.WARNING,
                        3000
                );
                eventSendUseItem.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void on3D(Event3DRender event3DRender) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (mc.thePlayer.getHeldItem() == null) return;

        if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemEnderPearl)) return;

        Vec3 startPos = mc.thePlayer.getPositionVector().addVector(0, mc.thePlayer.getEyeHeight(), 0);
        Vec3 velocity = getInitialVelocity();

        simulateTrajectory(startPos, velocity);
    }

    private Vec3 getInitialVelocity() {
        float yaw = mc.thePlayer.rotationYaw;
        float pitch = mc.thePlayer.rotationPitch;

        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);

        float velocityMultiplier = mc.thePlayer.getHeldItem().getItem() instanceof ItemBow ? 3.0F : 1.5F;

        return new Vec3(f1 * f2 * velocityMultiplier, f3 * velocityMultiplier, f * f2 * velocityMultiplier);
    }

    private void simulateTrajectory(Vec3 startPos, Vec3 velocity) {
        Vec3 currentPos = startPos;
        Vec3 currentVelocity = velocity;
        Vec3 landingPoint = null;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2.0F);
        GL11.glBegin(GL11.GL_LINE_STRIP);

        for (int i = 0; i < 300; i++) {
            GL11.glColor4f(0.0F, 1.0F, 0.0F, 0.7F);
            GL11.glVertex3d(currentPos.xCoord - mc.getRenderManager().viewerPosX,
                    currentPos.yCoord - mc.getRenderManager().viewerPosY,
                    currentPos.zCoord - mc.getRenderManager().viewerPosZ);

            Vec3 nextPos = currentPos.addVector(currentVelocity.xCoord, currentVelocity.yCoord, currentVelocity.zCoord);
            if (mc.theWorld.rayTraceBlocks(currentPos, nextPos) != null) {
                landingPoint = currentPos;
                break;
            }

            currentPos = nextPos;
            currentVelocity = applyPhysics(currentVelocity);
        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();

        if (landingPoint != null) {
            drawLandingPoint(landingPoint);
        }
    }

    private void drawLandingPoint(Vec3 landingPoint) {
        BlockPos blockPos = new BlockPos(landingPoint.xCoord, landingPoint.yCoord, landingPoint.zCoord);

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2.0F);
        GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.7F); // 设置颜色为红色

        GL11.glBegin(GL11.GL_LINES);

        double x = blockPos.getX() - mc.getRenderManager().viewerPosX;
        double y = blockPos.getY() - mc.getRenderManager().viewerPosY;
        double z = blockPos.getZ() - mc.getRenderManager().viewerPosZ;
        double size = 1.0;

        // 绘制方块边框的12条线
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x + size, y, z);

        GL11.glVertex3d(x + size, y, z);
        GL11.glVertex3d(x + size, y, z + size);

        GL11.glVertex3d(x + size, y, z + size);
        GL11.glVertex3d(x, y, z + size);

        GL11.glVertex3d(x, y, z + size);
        GL11.glVertex3d(x, y, z);

        GL11.glVertex3d(x, y + size, z);
        GL11.glVertex3d(x + size, y + size, z);

        GL11.glVertex3d(x + size, y + size, z);
        GL11.glVertex3d(x + size, y + size, z + size);

        GL11.glVertex3d(x + size, y + size, z + size);
        GL11.glVertex3d(x, y + size, z + size);

        GL11.glVertex3d(x, y + size, z + size);
        GL11.glVertex3d(x, y + size, z);

        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + size, z);

        GL11.glVertex3d(x + size, y, z);
        GL11.glVertex3d(x + size, y + size, z);

        GL11.glVertex3d(x + size, y, z + size);
        GL11.glVertex3d(x + size, y + size, z + size);

        GL11.glVertex3d(x, y, z + size);
        GL11.glVertex3d(x, y + size, z + size);

        GL11.glEnd();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();

    }

    private Vec3 applyPhysics(Vec3 velocity) {
        // 调整重力和空气阻力
        double gravity = 0.03;
        double airResistance = 0.99;

        return new Vec3(
                velocity.xCoord * airResistance,
                (velocity.yCoord - gravity) * airResistance,
                velocity.zCoord * airResistance
        );
    }

    private Vec3 simulateTrajectoryAndGetLandingPoint(Vec3 startPos, Vec3 velocity) {
        Vec3 currentPos = startPos;
        Vec3 currentVelocity = velocity;

        for (int i = 0; i < 300; i++) {
            Vec3 nextPos = currentPos.addVector(currentVelocity.xCoord, currentVelocity.yCoord, currentVelocity.zCoord);
            if (mc.theWorld.rayTraceBlocks(currentPos, nextPos) != null) {
                return currentPos;
            }
            currentPos = nextPos;
            currentVelocity = applyPhysics(currentVelocity);
        }
        return null;
    }
}