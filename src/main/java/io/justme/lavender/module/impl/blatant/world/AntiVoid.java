package io.justme.lavender.module.impl.blatant.world;

import io.justme.lavender.La;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.events.render.Event3DRender;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import io.justme.lavender.utility.network.PacketUtility;
import io.justme.lavender.utility.player.PlayerUtility;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Improved AntiVoid: record last safe position and use it for rescue
 */
@Getter
@ModuleInfo(name = "AntiVoid", description = "AntiVoid.", category = Category.World)
public class AntiVoid extends Module {

    private double safeX, safeY, safeZ;
    private double backX, backY, backZ;
    private boolean universalStarted = false;
    private boolean universalFlag = false;
    private boolean sent = false;
    private final PacketUtility packetUtility = new PacketUtility();
    private final List<Float> fallDistances = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();
        universalStarted = false;
        sent = false;
    }

    private boolean backing;
    @Override
    public void onDisable() {
        super.onDisable();
        sent = false;
        La.getINSTANCE().getHandlerManager().getBlinkHandler().dispatch();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.ticksExisted < 50) return;
        if (mc.thePlayer.isInvisible()) return;


        if (mc.thePlayer.onGround) {
            backing = false;
            BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
            if (!mc.theWorld.isAirBlock(pos)) {
                safeX = pos.getX() + 0.5;
                safeY = pos.getY();
                safeZ = pos.getZ() + 0.5;
            }
        }

        if (fallDistances.size() >= 25) fallDistances.remove(0);
        fallDistances.add(mc.thePlayer.fallDistance);

        if (!universalStarted && !PlayerUtility.isOnGround() && mc.thePlayer.motionY < -0.05 && !sent && !backing) {
            Vec3 landing = simulateLandingPoint(40);
            if (landing == null) {
                universalStarted = true;
                universalFlag = false;
                sent = false;
                backX = safeX;
                backY = safeY - 1;
                backZ = safeZ;
                La.getINSTANCE().getHandlerManager().getBlinkHandler().blinking = true;
                La.getINSTANCE().print("已记录", "[AntiVoid]");
            }
        }


        // blink
        if (universalStarted) {
            if (mc.thePlayer.onGround || mc.thePlayer.motionY > -0.01) {
                La.getINSTANCE().getHandlerManager().getBlinkHandler().dispatch();
                universalStarted = false;
                universalFlag = false;
                sent = false;
            } else if (!universalFlag) {
                universalFlag = true;

                for (int i = 0; i <= 5; i++) {
                    packetUtility.sendPacketFromLa(new C03PacketPlayer.C04PacketPlayerPosition(backX, backY, backZ, false));
                    La.getINSTANCE().print("发送回包 " + i,"[AntiVoid]");
                }

                La.getINSTANCE().getNotificationsManager().push(
                        "已发送回包",
                        "[AntiVoid]",
                        NotificationsEnum.SUCCESS,
                        2000
                );

                backing = true;
                sent = true;
            }
        }
    }

    @EventTarget
    public void on3DRender(Event3DRender event) {
        Vec3 landing = simulateLandingPoint(40);
        if (landing != null) {
            drawBoxAt(landing.xCoord, landing.yCoord, landing.zCoord, 0f, 1f, 0f, 0.5f);
        }
        drawBoxAt(safeX, safeY, safeZ, 0f, 0f, 1f, 0.5f);
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook pkt) {
            if (pkt.getX() == backX && pkt.getY() == backY - 1 && pkt.getZ() == backZ) {
                La.getINSTANCE().getHandlerManager().getBlinkHandler().blinking = false;
                mc.thePlayer.setPosition(backX, backY - 1, backZ);
                universalFlag = false;
                universalStarted = false;
                backing = false;
            }
        }
    }

    private Vec3 simulateLandingPoint(int maxTicks) {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        double vx = mc.thePlayer.motionX;
        double vy = mc.thePlayer.motionY;
        double vz = mc.thePlayer.motionZ;

        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            int amp = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
            vy += 0.1 * amp;
        }
        float yawRad = (float) Math.toRadians(mc.thePlayer.rotationYaw);

        for (int tick = 0; tick < maxTicks; tick++) {
            // 重力与阻力
            vy -= 0.08;
            vy *= 0.98;
            double friction = mc.thePlayer.onGround ? 0.6 : 0.91;
            vx *= friction;
            vz *= friction;

            float forward = mc.thePlayer.movementInput.moveForward;
            float strafe = mc.thePlayer.movementInput.moveStrafe;
            if (mc.thePlayer.isSneaking()) {
                forward = 0;
                strafe = 0;
                vx *= 0.3;
                vz *= 0.3;
            }
            double speedFac = mc.thePlayer.onGround ? 0.1 : 0.02;
            vx += forward * speedFac * -Math.sin(yawRad) + strafe * speedFac * Math.cos(yawRad);
            vz += forward * speedFac * Math.cos(yawRad) - strafe * speedFac * -Math.sin(yawRad);

            x += vx;
            y += vy;
            z += vz;
            if (y <= 0) return null;

            MovingObjectPosition hit = mc.theWorld.rayTraceBlocks(
                    new Vec3(x, y, z), new Vec3(x, y - 256, z), true, true, false);
            if (hit != null) return hit.hitVec;
        }
        return null;
    }


    private void drawBoxAt(double x, double y, double z, float red, float green, float blue, float alpha) {
        AxisAlignedBB bb = new AxisAlignedBB(x - 0.5, y, z - 0.5, x + 0.5, y + 1, z + 0.5);
        GlStateManager.pushMatrix();
        GlStateManager.translate(
                -mc.getRenderManager().viewerPosX,
                -mc.getRenderManager().viewerPosY,
                -mc.getRenderManager().viewerPosZ);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2.0F);
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        // Bottom edges
        addLine(wr, bb.minX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.minZ, red, green, blue, alpha);
        addLine(wr, bb.maxX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.maxZ, red, green, blue, alpha);
        addLine(wr, bb.maxX, bb.minY, bb.maxZ, bb.minX, bb.minY, bb.maxZ, red, green, blue, alpha);
        addLine(wr, bb.minX, bb.minY, bb.maxZ, bb.minX, bb.minY, bb.minZ, red, green, blue, alpha);
        // Top edges
        addLine(wr, bb.minX, bb.maxY, bb.minZ, bb.maxX, bb.maxY, bb.minZ, red, green, blue, alpha);
        addLine(wr, bb.maxX, bb.maxY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, red, green, blue, alpha);
        addLine(wr, bb.maxX, bb.maxY, bb.maxZ, bb.minX, bb.maxY, bb.maxZ, red, green, blue, alpha);
        addLine(wr, bb.minX, bb.maxY, bb.maxZ, bb.minX, bb.maxY, bb.minZ, red, green, blue, alpha);
        // Vertical edges
        addLine(wr, bb.minX, bb.minY, bb.minZ, bb.minX, bb.maxY, bb.minZ, red, green, blue, alpha);
        addLine(wr, bb.maxX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.minZ, red, green, blue, alpha);
        addLine(wr, bb.maxX, bb.minY, bb.maxZ, bb.maxX, bb.maxY, bb.maxZ, red, green, blue, alpha);
        addLine(wr, bb.minX, bb.minY, bb.maxZ, bb.minX, bb.maxY, bb.maxZ, red, green, blue, alpha);
        tess.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private void addLine(WorldRenderer wr,
                         double x1, double y1, double z1,
                         double x2, double y2, double z2,
                         float r, float g, float b, float a) {
        wr.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        wr.pos(x2, y2, z2).color(r, g, b, a).endVertex();
    }
}
