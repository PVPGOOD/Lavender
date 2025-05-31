package io.justme.lavender.events.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lenni0451.asmevents.event.IEvent;

/**
 * @author JustMe.
 * @since 2025/5/31
 **/
@Getter
@AllArgsConstructor
public class EventGameEnd implements IEvent {

    private GameEndType type;

    public enum GameEndType {
        VICTORY,
        DEFEAT,
    }
}
