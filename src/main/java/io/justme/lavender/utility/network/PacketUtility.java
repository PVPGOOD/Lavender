package io.justme.lavender.utility.network;

import io.justme.lavender.La;
import io.justme.lavender.module.impl.exploit.disabler.Disabler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

/**
 * @author JustMe.
 * @since 2024/4/14
 **/
public class PacketUtility {

    public void sendPacketFromLa(Packet<?> packet) {
        try {


            var disabler = ((Disabler) La.getINSTANCE().getModuleManager().getModuleByName("Disabler"));

            if (disabler.isToggle()) {
                disabler.onInvokePacket(packet);
            } else {
                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(packet);
            }

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean sendPacketWithRemoveFromLa(Packet<?> packet) {
        try {

            var disabler = ((Disabler) La.getINSTANCE().getModuleManager().getModuleByName("Disabler"));

            if (disabler.isToggle()) {
                disabler.onInvokePacket(packet);
            } else {
                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(packet);
            }
            return true;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean sendFinalPacket(Packet<?> packet) {
        try {
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendFinalPacket(packet);
            return true;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
