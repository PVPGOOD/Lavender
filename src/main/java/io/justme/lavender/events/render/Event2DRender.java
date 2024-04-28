package io.justme.lavender.events.render;

import lombok.Getter;
import net.lenni0451.asmevents.event.IEvent;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author JustMe.
 * @since 2024/4/28
 **/
@Getter
public class Event2DRender implements IEvent {

    private final float partialTicks;
    private final int displayHeight ,displayWidth;
    private final ScaledResolution scaledResolution;

    public Event2DRender(ScaledResolution scaledresolution, float partialTicks, int displayWidth, int displayHeight) {
        this.scaledResolution = scaledresolution;
        this.partialTicks = partialTicks;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
    }
}
