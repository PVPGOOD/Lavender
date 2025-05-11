package io.justme.lavender.module.impl.blatant.visual;

import io.justme.lavender.La;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.render.Event2DRender;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import io.justme.lavender.utility.player.PlayerUtility;
import io.justme.lavender.value.impl.NumberValue;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "FreeCam", description = "IDK.", category = Category.VISUAL)
public class FreeCam extends Module {
    private NumberValue speed = new NumberValue("Speed",1.0D, 0.01, 5.0D, 0.01D);

    private EntityOtherPlayerMP freeCamEntity;
    private final double[] originalPos = new double[3];
    private final float[] originalAngles = new float[2];

    private Minecraft mc = Minecraft.getMinecraft();


    public FreeCam() {

    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.theWorld == null) {
            setStatus(false);
            return;
        }

        if (!PlayerUtility.isOnGround()) {
            La.getINSTANCE().getNotificationsManager().push(
                    "FreeCam",
                    "你必须在地上才能记录原始位置",
                    NotificationsEnum.FAIL,3000);
            setStatus(false);
            return;
        }

        // 保存玩家原始位置和角度
        originalPos[0] = mc.thePlayer.posX;
        originalPos[1] = mc.thePlayer.posY;
        originalPos[2] = mc.thePlayer.posZ;
        originalAngles[0] = mc.thePlayer.rotationYaw;
        originalAngles[1] = mc.thePlayer.rotationPitch;

        // 创建自由视角实体
        freeCamEntity = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        freeCamEntity.copyLocationAndAnglesFrom(mc.thePlayer);
        freeCamEntity.noClip = true;
        freeCamEntity.setInvisible(true); // 设置为不可见

        mc.theWorld.addEntityToWorld(-100, freeCamEntity);
        mc.setRenderViewEntity(freeCamEntity);
        freeCamEntity.setPosition(
                freeCamEntity.posX + freeCamEntity.motionX,
                freeCamEntity.posY + freeCamEntity.motionY,
                freeCamEntity.posZ + freeCamEntity.motionZ
        );
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.theWorld == null) {
            return;
        }

        if (freeCamEntity == null) return;

        freeCamEntity.rotationPitch = mc.thePlayer.rotationPitch;
        freeCamEntity.rotationYaw = mc.thePlayer.rotationYaw;


        // 恢复玩家位置和角度
        mc.setRenderViewEntity(mc.thePlayer);
        mc.thePlayer.setPositionAndRotation(originalPos[0], originalPos[1], originalPos[2], originalAngles[0], originalAngles[1]);
        mc.theWorld.removeEntity(freeCamEntity);
        freeCamEntity = null;
    }

    @EventTarget
    public void on2D(Event2DRender eventUpdate) {
        if (freeCamEntity == null) {
            this.setStatus(false);
            return;
        }

        // 同步视角
        freeCamEntity.rotationYaw = mc.thePlayer.rotationYaw;
        freeCamEntity.rotationPitch = mc.thePlayer.rotationPitch;

        double moveSpeed = speed.getValue();
        freeCamEntity.motionX = freeCamEntity.motionY = freeCamEntity.motionZ = 0;

        if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            freeCamEntity.motionX -= Math.sin(Math.toRadians(freeCamEntity.rotationYaw)) * moveSpeed;
            freeCamEntity.motionZ += Math.cos(Math.toRadians(freeCamEntity.rotationYaw)) * moveSpeed;
        }
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
            freeCamEntity.motionX += Math.sin(Math.toRadians(freeCamEntity.rotationYaw)) * moveSpeed;
            freeCamEntity.motionZ -= Math.cos(Math.toRadians(freeCamEntity.rotationYaw)) * moveSpeed;
        }
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
            freeCamEntity.motionX += Math.cos(Math.toRadians(freeCamEntity.rotationYaw)) * moveSpeed;
            freeCamEntity.motionZ += Math.sin(Math.toRadians(freeCamEntity.rotationYaw)) * moveSpeed;
        }
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
            freeCamEntity.motionX -= Math.cos(Math.toRadians(freeCamEntity.rotationYaw)) * moveSpeed;
            freeCamEntity.motionZ -= Math.sin(Math.toRadians(freeCamEntity.rotationYaw)) * moveSpeed;
        }
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
            freeCamEntity.motionY += moveSpeed;
        }
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
            freeCamEntity.motionY -= moveSpeed;
        }

        freeCamEntity.setPositionAndRotation(
                freeCamEntity.posX + freeCamEntity.motionX,
                freeCamEntity.posY + freeCamEntity.motionY,
                freeCamEntity.posZ + freeCamEntity.motionZ,
                freeCamEntity.rotationYaw,
                freeCamEntity.rotationPitch
        );

// 强制刷新渲染
        mc.getRenderManager().renderEntityStatic(freeCamEntity, mc.timer.renderPartialTicks, true);
    }

    @EventTarget
    public void onSendPacket(EventPacket event) {
        if (event.getPacket() instanceof C07PacketPlayerDigging) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof C02PacketUseEntity) {
            event.setCancelled(true);
        }
    }
}