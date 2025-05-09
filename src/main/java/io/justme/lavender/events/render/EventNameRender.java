package io.justme.lavender.events.render;

import lombok.Getter;
import net.lenni0451.asmevents.event.IEvent;
import net.lenni0451.asmevents.event.wrapper.CancellableEvent;
import net.minecraft.entity.Entity;

/**
 * @author JustMe.
 * @since 2025/5/10
 **/
@Getter
public class EventNameRender extends CancellableEvent implements IEvent {

    public Entity entity;

    public EventNameRender(Entity entity) {
        this.entity = entity;
    }
}
