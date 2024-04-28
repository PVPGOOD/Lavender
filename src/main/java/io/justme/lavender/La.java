package io.justme.lavender;

import io.justme.lavender.events.render.Event2DRender;
import io.justme.lavender.fonts.FontManager;
import lombok.Getter;
import net.lenni0451.asmevents.EventManager;
import net.lenni0451.asmevents.event.EventTarget;

/**
 * @author JustMe.
 * @since 2024/4/28
 **/
@Getter
public class La {

    private final static La INSTANCE = new La();

    //客户端相关
    private final String La = "Lavender";
    private final String author = "JustMe";
    private final String version = "1.0";

    private EventManager eventManager;
    private FontManager fontManager;

    public void initialization() {
        eventManager = new EventManager();
        fontManager = new FontManager();

        eventManager.register(this);
    }

    @EventTarget
    public void on2D(Event2DRender event2DRender) {
        fontManager.getSFBold18().drawString("Lavender",0,0,-1);
    }

    public static io.justme.lavender.La getINSTANCE() {
        return INSTANCE;
    }
}
