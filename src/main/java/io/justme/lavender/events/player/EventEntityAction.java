package io.justme.lavender.events.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.IEvent;

@Getter
@Setter
@AllArgsConstructor
public class EventEntityAction implements IEvent {

    private boolean sprinting, sneaking;

}
