package io.justme.lavender;

import io.justme.lavender.events.render.Event2DRender;
import lombok.Getter;
import net.lenni0451.asmevents.EventManager;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;

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

    @EventTarget
    public void on2D(Event2DRender event2DRender) {
        Minecraft.getMinecraft().fontRendererObj.drawString("Lavender",0,0,-1);
    }
}
