package io.justme.lavender.module.impl.blatant.world;

import io.justme.lavender.La;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.handler.BlinkComponent;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.network.PacketUtility;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;

/**
 * @author JustMe.
 * @since 2025/5/10
 **/

@Getter
@ModuleInfo(name = "AntiVoid", description = "AntiVoid.", category = Category.World)
public class AntiVoid extends Module {

    private double groundX = 0.0;
    private double groundY = 0.0;
    private double groundZ = 0.0;
    private boolean universalStarted = false;
    private boolean universalFlag = false;

    PacketUtility packetUtility = new PacketUtility();
    @EventTarget
    public void onUpdate(EventUpdate event) {

        if (universalStarted) {
            if (mc.thePlayer.onGround || mc.thePlayer.fallDistance > 8f) {
                La.getINSTANCE().getBlinkComponent().dispatch();
                universalStarted = false;
                universalFlag = false;
            } else if (mc.thePlayer.fallDistance > 6f && !universalFlag) {
                universalFlag = true;
                packetUtility.sendPacketFromLa(new C03PacketPlayer.C04PacketPlayerPosition(groundX, groundY + 1, groundZ, false));
            }
        } else if (mc.thePlayer.fallDistance > 0f && !mc.thePlayer.onGround && mc.thePlayer.motionY < 0) {
            if (isOverVoid()) {
                 universalStarted = true;
                universalFlag = false;
                La.getINSTANCE().getBlinkComponent().blinking = true;
                groundX = mc.thePlayer.posX;
                groundY = mc.thePlayer.posY;
                groundZ = mc.thePlayer.posZ;
            }
        }
    }

    @EventTarget
    public void onPacket(EventPacket event) {

        if (event.getPacket() instanceof S08PacketPlayerPosLook s08PacketPlayerPosLook) {
            if (s08PacketPlayerPosLook.getX() == groundX && s08PacketPlayerPosLook.getY() == groundY && s08PacketPlayerPosLook.getZ() == groundZ) {
                La.getINSTANCE().getBlinkComponent().blinking = false;
                mc.thePlayer.setPosition(groundX, groundY, groundZ);
                universalFlag = false;
                universalStarted = false;
            }
        }
    }

    private boolean isOverVoid() {
        return mc.theWorld.rayTraceBlocks(
                new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 40, mc.thePlayer.posZ),
                true, true, false) == null;
    }


}
