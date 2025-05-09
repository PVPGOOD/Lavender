package io.justme.lavender.events.render;

import lombok.Getter;
import net.lenni0451.asmevents.event.IEvent;

/**
 * @author JustMe.
 * @since 2025/5/10
 **/
@Getter
public class Event3DRender implements IEvent {

    public final float partialTicks;

    public Event3DRender(float partialTicks) {
        this.partialTicks = partialTicks;
    }

}
