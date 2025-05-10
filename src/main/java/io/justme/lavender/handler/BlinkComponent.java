package io.justme.lavender.handler;

import io.justme.lavender.La;
import io.justme.lavender.events.game.EventWorldReload;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.network.PacketUtility;
import net.lenni0451.asmevents.event.EventTarget;
import net.lenni0451.asmevents.event.enums.EnumEventPriority;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class BlinkComponent {

    public final ConcurrentLinkedQueue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
    public boolean blinking;
    public ArrayList<Class<?>> exemptedPackets = new ArrayList<>();
    public TimerUtility exemptionWatch = new TimerUtility();

    public void setExempt(Class<?>... packets) {
        exemptedPackets = new ArrayList<>(Arrays.asList(packets));
        exemptionWatch.reset();
    }
    PacketUtility packetUtility = new PacketUtility();
    @EventTarget(priority = EnumEventPriority.HIGHEST)
    public void onPacketSend(EventPacket event){
        var mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) {
            packets.clear();
            exemptedPackets.clear();
            return;
        }
        
        if(event.getType() == EnumEventType.OUTGOING) {

            if (mc.thePlayer.isDead || mc.isSingleplayer() || !mc.getNetHandler().doneLoadingTerrain) {
                packets.forEach(packetUtility::sendFinalPacket);
                packets.clear();
                blinking = false;
                exemptedPackets.clear();
                return;
            }

            final Packet<?> packet = event.getPacket();

            if (packet instanceof C00Handshake || packet instanceof C00PacketLoginStart ||
                    packet instanceof C00PacketServerQuery || packet instanceof C01PacketPing ||
                    packet instanceof C01PacketEncryptionResponse || packet instanceof C00PacketKeepAlive) {
                return;
            }

            if (blinking) {
                if (!event.isCancelled() && exemptedPackets.stream().noneMatch(packetClass ->
                        packetClass == packet.getClass())) {
                    packets.add(packet);
                    event.setCancelled(true);
                }
            }
        }
    }

    public void release(boolean clear) {
        if(!packets.isEmpty()) {
            packets.forEach(packetUtility::sendFinalPacket);
            if(clear) {
                packets.clear();
                exemptedPackets.clear();
            }
        }
    }

    public void dispatch(boolean releasePackets) {
        if (releasePackets) {
            release(true);
        }
        blinking = false;
    }

    public void dispatch() {
        dispatch(true);
    }

    @EventTarget(priority = EnumEventPriority.HIGHEST)
    public void onWorld(EventWorldReload event) {
        packets.clear();
        La.getINSTANCE().getBlinkComponent().blinking = false;
    }
}
