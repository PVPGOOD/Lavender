package io.justme.lavender.utility.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

/**
 * @author JustMe.
 * @since 2024/4/14
 **/
public class PacketUtility {

    public void sendPacket(Packet<?> packet) {
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(packet);
    }

}
