package io.justme.lavender.events.player;

import io.justme.lavender.La;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.IEvent;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.lenni0451.asmevents.event.types.ITypedEvent;

/**
 * @author JustMe.
 * @since 2024/4/28
 **/
@Getter
@Setter
public class EventMotionUpdate implements IEvent, ITypedEvent {

    private double x, y, z;
    private float yaw, pitch;
    private float prevYaw;
    public float preYawOffset;
    private float prevPitch;
    private boolean onGround;
    private final EnumEventType type;
    private boolean isRotate;

    public EventMotionUpdate(double x, double y, double z, float yaw, float pitch, boolean onGround, EnumEventType type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.type = type;
    }

    @Override
    public EnumEventType getType() {
        return this.type;
    }

    public void setYaw(float yaw) {
        La.getINSTANCE().getHandlerManager().getRotationHandler().renderYawOffset(yaw);
        this.yaw = yaw;
        this.isRotate = true;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.isRotate = true;
    }

}
