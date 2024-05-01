package io.justme.lavender.events.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.lenni0451.asmevents.event.types.ICancellableEvent;
import net.lenni0451.asmevents.event.types.ITypedEvent;
import net.minecraft.network.Packet;

/**
 * @author JustMe.
 * @since 2024/5/1
 **/

@Getter
@Setter
public class EventPacket implements ICancellableEvent , ITypedEvent {

    private boolean cancel;
    private Packet<?> packet;
    private EnumEventType enumEventType;

    public EventPacket(Packet<?> packet, EnumEventType enumEventType) {
        this.packet = packet;
        this.enumEventType = enumEventType;
    }

    @Override
    public boolean isCancelled() {
        return isCancel();
    }

    @Override
    public void setCancelled(boolean cancelled) {
        setCancel(cancelled);
    }

    @Override
    public EnumEventType getType() {
        return getEnumEventType();
    }
}
