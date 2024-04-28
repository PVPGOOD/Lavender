package io.justme.lavender;

import lombok.Getter;
import net.lenni0451.asmevents.EventManager;

/**
 * @author JustMe.
 * @since 2024/4/28
 **/
@Getter
public class La {
    @Getter
    private final static La INSTANCE = new La();
    private EventManager eventManager;

    public void initialization() {
        eventManager = new EventManager();

        eventManager.register(this);
    }
}
