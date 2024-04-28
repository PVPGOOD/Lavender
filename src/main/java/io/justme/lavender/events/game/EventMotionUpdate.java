package io.justme.lavender.events.game;

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
    private boolean onGround;
    private final EnumEventType type;

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
}
