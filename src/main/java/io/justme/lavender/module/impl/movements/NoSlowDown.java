package io.justme.lavender.module.impl.movements;

import com.viaversion.viarewind.protocol.protocol1_8to1_9.Protocol1_8To1_9;
import com.viaversion.viarewind.utils.PacketUtil;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Type;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventSlowDown;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.ModeValue;
import net.lenni0451.asmevents.event.EventTarget;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "NoSlowDown", description = "", category = Category.MOVEMENTS)
public class NoSlowDown extends Module {

    private final ModeValue mode = new ModeValue("Mode", new String[]{"Watchdog", "BlocksMC", "Normal","Raven"}, "Normal");

    @Override
    public void onEnable() {
        super.onEnable();
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onSlowDown(EventSlowDown event) {
        switch (mode.getValue()) {
            case "Normal":
                event.setCancelled(true);
                break;
            case "Watchdog":
                if (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
                    event.setCancelled(true);
                }
                break;
        }
    }

    @EventTarget
    public void onEvent(EventMotionUpdate event) {
        switch (mode.getValue()) {
            case "Watchdog":
                if (event.getType() == EnumEventType.PRE) {
                    if (mc.thePlayer.getHeldItem() == null) {
                        break;
                    }

                    if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem().getItem() instanceof ItemFood && mc.thePlayer.ticksExisted % 3 == 0) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), EnumFacing.UP.getIndex(), null, 0.0F, 0.0F, 0.0F));
                    }
                    if ((mc.thePlayer.isBlocking() && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && !mc.isSingleplayer() && !ViaLoadingBase.getInstance().getTargetVersion().isOlderThan(ProtocolVersion.v1_12) && !ViaLoadingBase.getInstance().getTargetVersion().isNewerThan(ProtocolVersion.v1_12_2)) {
                        PacketWrapper useItem = PacketWrapper.create(29, null, Via.getManager().getConnectionManager().getConnections().iterator().next());
                        useItem.write(Type.VAR_INT, 1);
                        PacketUtil.sendToServer(useItem, Protocol1_8To1_9.class, true, true);
                    }
                }
                break;
        }
    }
}
