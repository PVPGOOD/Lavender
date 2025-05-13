package io.justme.lavender.module.impl.blatant.misc;

import io.justme.lavender.La;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventRightClick;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import io.justme.lavender.utility.network.PacketUtility;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2025/5/11
 **/

@Setter
@Getter
@ModuleInfo(name = "BlockPrediction", category = Category.MISC, description = "blockWarning")
public class BlockPrediction extends Module {

    private boolean notified = false;
    private boolean switchingBlock;
    private int realInUsingSlot;
    private int lastedSlot;


    private ArrayList<Integer> validSlots = new ArrayList<>();
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (switchingBlock) {
            if (mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null) {
                var blockPos = mc.objectMouseOver.getBlockPos();
                if (mc.theWorld.getBlockState(blockPos).getBlock().getMaterial() == Material.air) {
                    La.getINSTANCE().print("switch back");
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(lastedSlot));
                    switchingBlock = false;
                }
            }
        }
    }

    private PacketUtility packetUtility = new PacketUtility();
    @EventTarget
    public void onRightClick(EventRightClick eventRightClick) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        var heldItem = mc.thePlayer.getHeldItem();

        if (heldItem != null && heldItem.getItem() instanceof ItemBlock) {
            int stackSize = heldItem.stackSize;

            if (stackSize < 16 && !notified) {
                La.getINSTANCE().getNotificationsManager().push("BlockPrediction" ,"请注意 你当前的方块小于16个", NotificationsEnum.WARNING,2000);
                notified = true;
            }
        }

        if (heldItem == null) {
            validSlots.clear();
            for (int i = 0; i < 9; i++) {
                ItemStack hotbarItem = mc.thePlayer.inventory.mainInventory[i];
                if (hotbarItem != null && hotbarItem.stackSize > 1 && hotbarItem.getItem() instanceof ItemBlock) {
                    validSlots.add(i);
                }
            }

            if (!validSlots.isEmpty()) {
                var slotToUse = validSlots.getFirst();
                if (realInUsingSlot != slotToUse) {
                    realInUsingSlot = slotToUse;
                    ItemStack hotbarItem = mc.thePlayer.inventory.mainInventory[slotToUse];
                    eventRightClick.setHeldStack(hotbarItem);
                    packetUtility.sendFinalPacket(new C09PacketHeldItemChange(slotToUse));
                    La.getINSTANCE().print("silent switch to " + slotToUse);
                    setLastedSlot(mc.thePlayer.inventory.currentItem);
                    setSwitchingBlock(true);
                }

                ItemStack hotbarItem = mc.thePlayer.inventory.mainInventory[slotToUse];
                eventRightClick.setHeldStack(hotbarItem);
            }
        }
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {

        if (eventPacket.getEnumEventType() == EnumEventType.OUTGOING) {
            if (eventPacket.getPacket() instanceof C09PacketHeldItemChange) {
                notified = false;
                switchingBlock = false;
                realInUsingSlot = mc.thePlayer.inventory.currentItem;
            }
        }

    }
}
