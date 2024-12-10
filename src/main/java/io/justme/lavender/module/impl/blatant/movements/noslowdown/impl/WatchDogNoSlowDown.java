package io.justme.lavender.module.impl.blatant.movements.noslowdown.impl;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.game.EventWorldReload;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventSlowDown;
import io.justme.lavender.module.impl.blatant.movements.noslowdown.AbstractNoSlowDown;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

/**
 * @author JustMe.
 * @since 2024/12/8
 **/
public class WatchDogNoSlowDown extends AbstractNoSlowDown {

    private boolean sendEating;
    private int offGroundTicks;

    public WatchDogNoSlowDown() {
        super("WatchDog");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onMotionUpdate(EventMotionUpdate event) {
        if (isUsingFood()) {
            if (offGroundTicks == 4 && sendEating) {
                sendEating = false;
                if (ViaLoadingBase.getInstance().getTargetVersion().getVersion() > 47) {
                    getPacketUtility().sendFinalPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                }

            } else if (mc.thePlayer.isUsingItem()) {
                event.setY(event.getY() + 1E-14);
            }
        }
    }

    @Override
    public void onPacket(EventPacket event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        switch (event.getEnumEventType()) {
            case OUTGOING -> {
                //吃东西的c08
                if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null) {
                    if (Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemFood) {
                        if (event.getPacket() instanceof C08PacketPlayerBlockPlacement blockPlacement && !Minecraft.getMinecraft().thePlayer.isUsingItem()) {

                            if (blockPlacement.getPlacedBlockDirection() == 255 && offGroundTicks < 2) {
                                if (Minecraft.getMinecraft().thePlayer.onGround) {
                                    Minecraft.getMinecraft().thePlayer.jump();
                                }
                                sendEating = true;
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }

            case INCOMING -> {

            }
        }
    }

    @Override
    public void onWorldReload(EventWorldReload event) {

    }

    @Override
    public void onTick(EventTick event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (Minecraft.getMinecraft().thePlayer.onGround) {
            offGroundTicks = 0;
        } else {
            offGroundTicks++;
        }
    }

    @Override
    public void onNoSlowDown(EventSlowDown eventSlowDown) {
        eventSlowDown.setCancelled(true);
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
