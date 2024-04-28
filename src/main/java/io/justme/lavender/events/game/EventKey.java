package io.justme.lavender.events.game;

import lombok.Getter;
import net.lenni0451.asmevents.event.IEvent;

/**
 * @author JustMe.
 * @since 2024/4/28
 **/
@Getter
public class EventKey implements IEvent {

    private final int keyCode;

    public EventKey(int keyCode) {
        this.keyCode = keyCode;
    }
}
