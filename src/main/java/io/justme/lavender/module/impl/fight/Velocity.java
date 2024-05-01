package io.justme.lavender.module.impl.fight;

import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

/**
 * @author JustMe.
 * @since 2024/5/1
 **/

@Getter
@ModuleInfo(name = "Velocity", description = "", category = Category.FIGHT)
public class Velocity extends Module {

    public final NumberValue
            vertical = new NumberValue("Vertical", 0, 0, 100, 1),
            horizontal = new NumberValue("Horizontal", 0, 0, 100, 1);

    @Override
    public void onEnable() {
        super.onEnable();
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof S12PacketEntityVelocity s12) {

            if (Minecraft.getMinecraft().thePlayer != null) {
                s12.motionX *= (int) (getHorizontal().getValue() / 100);
                s12.motionZ *= (int) (getHorizontal().getValue() / 100);
                s12.motionY *= (int) (getVertical().getValue() / 100.0);

                if (getVertical().getValue() == 0 && getHorizontal().getValue() == 0) {
                    eventPacket.setCancelled(true);
                }
            }
        }
    }
}
