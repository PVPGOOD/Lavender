package io.justme.lavender.events.player;

import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.IEvent;

/**
 * @author JustMe.
 * @since 2024/5/1
 **/
public class EventMove implements IEvent {

    @Getter
    @Setter
    private double x, y, z;

    public EventMove(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
