package io.justme.lavender.events.player;

import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.lenni0451.asmevents.event.types.ITypedEvent;

/**
 * @author JustMe.
 * @since 2024/5/2
 **/

@Getter
public class EventAttack implements ITypedEvent {

    private final EnumEventType types;

    public EventAttack(EnumEventType types) {
        this.types = types;
    }

    @Override
    public EnumEventType getType() {
        return getTypes();
    }
}
