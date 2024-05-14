package io.justme.lavender.module.impl.movements.speed;

import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventMove;
import io.justme.lavender.events.player.EventStrafe;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.impl.exploit.disabler.process.PacketProcessor;
import io.justme.lavender.module.impl.movements.speed.impl.NCPHopSpeed;
import io.justme.lavender.module.impl.movements.speed.impl.watchdog.WatchdogLowHopSpeed;
import io.justme.lavender.module.impl.movements.speed.impl.watchdog.WatchdogSpeed;
import io.justme.lavender.utility.interfaces.IMinecraft;
import io.justme.lavender.utility.network.PacketUtility;
import lombok.Getter;

import java.util.HashMap;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

@Getter
public abstract class AbstractSpeed implements IMinecraft {

    private static final HashMap<Class<? extends AbstractSpeed>, AbstractSpeed> speeds = new HashMap<>();
    protected final PacketProcessor dataProcessor = new PacketProcessor();
    protected final PacketUtility packetUtility = new PacketUtility();
    private final String name;

    public AbstractSpeed(String name) {
        this.name = name;
    }

    public static void onInitialization() {
        speeds.put(WatchdogSpeed.class, new WatchdogSpeed());
        speeds.put(NCPHopSpeed.class, new NCPHopSpeed());
        speeds.put(WatchdogLowHopSpeed.class,new WatchdogLowHopSpeed());
    }

    public static AbstractSpeed find(String name) {
        return speeds.values().stream().filter(mode -> mode.getName().equals(name)).findFirst().orElse(null);
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onPacket(EventPacket event);

    public abstract void onMotionUpdate(EventMotionUpdate event);
    public abstract void onUpdate(EventUpdate event);
    public abstract void onMove(EventMove event);
    public abstract void onStrafe(EventStrafe event);
}
