package io.justme.lavender.events.render;

import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.IEvent;
import net.minecraft.entity.EntityLivingBase;

@Getter
@Setter
public class RotationUpdateEvent implements IEvent {

    private EntityLivingBase entity;
    private float rotationYawHead;
    private float renderYawOffset;
    private float renderHeadYaw;
    private float renderHeadPitch;
    private final float partialTicks;

    public RotationUpdateEvent(EntityLivingBase entity, float renderYawOffset, float rotationYawHead, float renderHeadYaw, float renderHeadPitch, float partialTicks) {
        this.entity = entity;
        this.rotationYawHead = rotationYawHead;
        this.renderYawOffset = renderYawOffset;
        this.renderHeadYaw = renderHeadYaw;
        this.renderHeadPitch = renderHeadPitch;
        this.partialTicks = partialTicks;
    }
}
