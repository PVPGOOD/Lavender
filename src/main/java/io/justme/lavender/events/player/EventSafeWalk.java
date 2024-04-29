package io.justme.lavender.events.player;

import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.IEvent;
import net.lenni0451.asmevents.event.types.ICancellableEvent;

/**
 * @author JustMe.
 * @since 2024/4/29
 **/
@Getter
@Setter
public class EventSafeWalk implements IEvent, ICancellableEvent {

    private boolean cancel;

    @Override
    public boolean isCancelled() {
        return isCancel();
    }

    @Override
    public void setCancelled(boolean cancelled) {
        setCancel(cancelled);
    }
}
