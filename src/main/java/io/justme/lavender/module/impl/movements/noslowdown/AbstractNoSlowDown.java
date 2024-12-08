package io.justme.lavender.module.impl.movements.noslowdown;

import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.game.EventWorldReload;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventSlowDown;
import io.justme.lavender.events.render.Event2DRender;
import io.justme.lavender.module.impl.exploit.disabler.AbstractDisabler;
import io.justme.lavender.module.impl.exploit.disabler.impl.WatchdogDisabler;
import io.justme.lavender.module.impl.exploit.disabler.process.PacketProcessor;
import io.justme.lavender.module.impl.movements.noslowdown.impl.VanillaNoSlowDown;
import io.justme.lavender.module.impl.movements.noslowdown.impl.WatchDogNoSlowDown;
import io.justme.lavender.utility.network.PacketUtility;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

import java.util.HashMap;

/**
 * @author JustMe.
 * @since 2024/12/8
 **/
@Getter
public abstract class AbstractNoSlowDown {

    public final Minecraft mc = Minecraft.getMinecraft();
    private static final HashMap<Class<? extends AbstractNoSlowDown>, AbstractNoSlowDown> noslowdowns = new HashMap<>();
    protected final PacketProcessor dataProcessor = new PacketProcessor();
    protected final PacketUtility packetUtility = new PacketUtility();
    private final String name;

    public AbstractNoSlowDown(String name) {
        this.name = name;
    }

    public static void onInitialization() {
        noslowdowns.put(WatchDogNoSlowDown.class, new WatchDogNoSlowDown());
        noslowdowns.put(VanillaNoSlowDown.class, new VanillaNoSlowDown());
    }

    public static AbstractNoSlowDown find(String name) {
        return noslowdowns.values().stream().filter(mode -> mode.getName().equals(name)).findFirst().orElse(null);
    }


    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void onPacket(EventPacket event);
    public abstract void onMotionUpdate(EventMotionUpdate event);
    public abstract void onWorldReload(EventWorldReload event);
    public abstract void onTick(EventTick event);
    public abstract void onNoSlowDown(EventSlowDown eventSlowDown);

}
