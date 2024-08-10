package io.justme.lavender.module.impl.movements;

import io.justme.lavender.La;
import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventSlowDown;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.network.PacketUtility;
import io.justme.lavender.utility.player.PlayerUtility;
import io.justme.lavender.value.impl.ModeValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

@Getter
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

        switch (getMode().getValue()) {
            case "Normal" -> event.setCancelled(true);
            case "Watchdog" -> event.setCancelled(true);
        }
    }


    @EventTarget
    public void onTicks(EventTick eventTick) {


        if (mc.thePlayer == null) {
            return;
        }

        if (mc.thePlayer.onGround) {
            offGroundTicks = 0;
        } else {
            offGroundTicks++;
        }
    }

    private boolean send;
    private int offGroundTicks;

    @EventTarget
    public void onMotionEvent(EventMotionUpdate event) {


        if (mc.thePlayer == null) {
            return;
        }

        switch (getMode().getValue()) {
            case "Normal" -> {}
            case "Watchdog" -> {

                switch (event.getType()) {
                    case PRE -> {


                        if (mc.thePlayer.getHeldItem() != null) {
                            if (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
                                if (offGroundTicks == 2 && send) {
                                    La.getINSTANCE().print("fake eat");

                                    getPacketUtility().sendPacketFromLa(
                                            new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1),
                                                    255, mc.thePlayer.getHeldItem(),
                                                    0, 0, 0));


                                    send = false;
                                } else if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.isUsingItem()) {

                                    if (mc.thePlayer.getHeldItem() != null) {
                                        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
                                            event.setY(event.getY() + 1E-14);
                                        }
                                    }
                                }
                            }
                        }


                    }

                    case POST -> {

                    }
                }
            }
        }
    }

    public final PacketUtility packetUtility = new PacketUtility();
    @EventTarget
    public void onPacket(EventPacket eventPacket) {

        if (mc.thePlayer == null) {
            return;
        }

        switch (getMode().getValue()) {
            case "Normal" -> {
            }
            case "Watchdog" -> {
                //跑吃
                if (mc.thePlayer.getHeldItem() != null) {
                    if (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
                        if (eventPacket.getPacket() instanceof C08PacketPlayerBlockPlacement blockPlacement && !mc.thePlayer.isUsingItem()) {

                            if (blockPlacement.getPlacedBlockDirection() == 255 && offGroundTicks < 2) {
                                if (mc.thePlayer.onGround) {
                                    mc.thePlayer.setJumping(false);
                                    mc.thePlayer.jump();
                                }
                                send = true;
                                eventPacket.setCancelled(true);
                            }
                        }
                    }

                    //拦截来自 KillAura 释放的爱
                    if (PlayerUtility.isHoldingSword()) {
                        if (eventPacket.getPacket() instanceof C07PacketPlayerDigging packet) {
                            if (packet.getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                                eventPacket.setCancelled(true);
                                int slot = mc.thePlayer.inventory.currentItem;
                                getPacketUtility().sendPacketFromLa(new C09PacketHeldItemChange(slot < 8 ? slot + 1 : 0));
                                getPacketUtility().sendPacketFromLa(new C09PacketHeldItemChange(slot));
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isUsingFood() {

        if (mc.thePlayer == null) {
            return false;
        }

        if (mc.thePlayer.getItemInUse() == null) {
            return false;
        }
        Item usingItem = mc.thePlayer.getItemInUse().getItem();
        return mc.thePlayer.isUsingItem() && (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion);
    }
}
