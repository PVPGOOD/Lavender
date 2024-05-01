package io.justme.lavender.events.player;

import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.wrapper.CancellableEvent;

;


@Setter
@Getter
public class EventSlowDown extends CancellableEvent {

    private float strafe, forward;

    public EventSlowDown(float strafe, float forward) {
        this.strafe = strafe;
        this.forward = forward;
    }
}
