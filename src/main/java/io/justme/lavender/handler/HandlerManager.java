package io.justme.lavender.handler;

import io.justme.lavender.La;
import io.justme.lavender.handler.impl.BlinkHandler;
import io.justme.lavender.handler.impl.RotationHandler;
import io.justme.lavender.handler.impl.ServerHandler;
import io.justme.lavender.handler.impl.hack.HackDetectionHandler;
import io.justme.lavender.utility.interfaces.Manager;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * @author JustMe.
 * @since 2025/5/16
 **/
@Getter
@Setter
public class HandlerManager extends Manager<AbstractHandler> {

    private BlinkHandler blinkHandler = new BlinkHandler();
    private ServerHandler serverHandler = new ServerHandler();
    private RotationHandler rotationHandler = new RotationHandler();
    private HackDetectionHandler hackDetectionHandler = new HackDetectionHandler();

    public HandlerManager() {

        getElements().addAll(Arrays.asList(
                getBlinkHandler(),
                getServerHandler(),
                getRotationHandler(),
                getHackDetectionHandler()
        ));

        for (AbstractHandler element : getElements()) {
            La.getINSTANCE().getEventManager().register(element);
        }
    }
}
