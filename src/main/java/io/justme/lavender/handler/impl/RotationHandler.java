package io.justme.lavender.handler.impl;

import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.render.RotationUpdateEvent;
import io.justme.lavender.handler.AbstractHandler;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.lenni0451.asmevents.event.enums.EnumEventPriority;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;

/**
 * @author JustMe.
 * @since 2024/12/15
 **/
@Getter
@Setter
public class RotationHandler extends AbstractHandler {

    private float yaw;
    public float yawOffset;
    private float pitch;
    private float prevYaw;
    public float preYawOffset;
    private float prevPitch;
    private boolean isRotating;
    private final Minecraft mc = Minecraft.getMinecraft();

    @EventTarget(priority = EnumEventPriority.HIGHEST)
    public void onTick(EventTick event) {
        this.prevYaw = this.yaw;
        this.preYawOffset = this.yawOffset;
        this.prevPitch = this.pitch;
    }


    @EventTarget(priority = EnumEventPriority.HIGHEST)
    public void onMotionUpdate(EventMotionUpdate event) {


    }



    @EventTarget(priority = EnumEventPriority.HIGHEST)
    public void onRotationUpdate(RotationUpdateEvent event) {
        if ( mc.currentScreen != null) return;
        Entity entity = event.getEntity();
        float partialTicks = event.getPartialTicks();
        if (entity instanceof EntityPlayerSP && entity.ridingEntity == null && partialTicks != 1.0F && this.isRotating) {
            event.setRenderYawOffset(this.interpolateAngle(partialTicks, this.preYawOffset, this.yawOffset));
            event.setRenderHeadYaw(this.interpolateAngle(partialTicks, this.prevYaw, this.yaw) - event.getRenderYawOffset());
            event.setRenderHeadPitch(this.lerp(partialTicks, this.prevPitch, this.pitch));
        }
    }

    public void renderYawOffset(float yaw) {
        double currentPosX = mc.thePlayer.posX;
        double prevPosX = mc.thePlayer.prevPosX;
        double currentPosZ = mc.thePlayer.posZ;
        double prevPosZ = mc.thePlayer.prevPosZ;

        this.preYawOffset = this.yawOffset;

        double deltaX = currentPosX - prevPosX;
        double deltaZ = currentPosZ - prevPosZ;
        float movementMagnitude = (float) (deltaX * deltaX + deltaZ * deltaZ);

        float targetYawOffset = this.yawOffset;
        if (movementMagnitude > 0.0025F) {
            targetYawOffset = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0F;
        }

//        // 如果正在挥动，优先使用yaw值覆盖
//        if (mc.thePlayer.swingProgress > 0.0F) {
//            targetYawOffset = yaw;
//        }

        // 平滑插值到目标yaw偏移值
        float yawDifference = MathHelper.wrapAngleTo180_float(targetYawOffset - this.yawOffset);
        this.yawOffset += yawDifference * 0.3F;

        // 限制yaw偏移差值的范围
        float clampedDifference = MathHelper.wrapAngleTo180_float(yaw - this.yawOffset);
        clampedDifference = Math.max(-75.0F, Math.min(75.0F, clampedDifference));

        // 应用限制后的差值到yaw偏移值
        this.yawOffset = yaw - clampedDifference;

        // 对较大的yaw偏移差值额外平滑处理
        if (clampedDifference * clampedDifference > 2500.0F) {
            this.yawOffset += clampedDifference * 0.2F;
        }

        mc.thePlayer.prevRenderYawOffset = mc.thePlayer.renderYawOffset;
        mc.thePlayer.renderYawOffset = this.yawOffset;
    }

    public float interpolateAngle(float factor, float startAngle, float endAngle) {
        return startAngle + factor * MathHelper.wrapAngleTo180_float(endAngle - startAngle);
    }

    public float lerp(float factor, float start, float end) {
        return start + factor * (end - start);
    }
}
