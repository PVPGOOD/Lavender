package io.justme.lavender.module.impl.blatant.player;

import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.math.TimerUtility;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

/**
 * @author JustMe.
 * @since 2024/5/5
 **/

@Getter
@ModuleInfo(name = "AutoPot", description = "", category = Category.PLAYER)
public class AutoPot extends Module {

    private final TimerUtility timer = new TimerUtility();

    @Getter @Setter
    private boolean potting;
    private int slot, last;

    @Override
    public void onEnable() {
        potting = false;
        slot = -1;
        last = -1;
        timer.reset();

        super.onEnable();
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate eventMotionUpdate) {
        switch (eventMotionUpdate.getType()) {
            case PRE -> {
                slot = getSlot();

                if (inCage()) return;

                if (inCage()) {
                    timer.reset();
                }

                if (getTimer().hasTimeElapsed(1000,true) && !isPotting()) {

                    int speedId = Potion.moveSpeed.getId();
                    if (!mc.thePlayer.isPotionActive(speedId) && mc.thePlayer.onGround && hasPot(speedId)) {
                        int push = hasPot(speedId, slot);

                        if (push != -1) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, push, slot, 2, mc.thePlayer);
                        }

                        last = mc.thePlayer.inventory.currentItem;
                        mc.thePlayer.inventory.currentItem = slot;

                        setPotting(true);
                    }
                }

                if (isPotting()) {
                    eventMotionUpdate.setPitch(80);
                }
            }

            case POST -> {

                if (isPotting()) {
                    if (mc.thePlayer.inventory.getCurrentItem() != null && mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem())) {
                        mc.entityRenderer.itemRenderer.resetEquippedProgress2();
                    }
                }

                if (last != -1) {
                    mc.thePlayer.inventory.currentItem = last;
                }
                potting = false;
                last = -1;
            }
        }
    }

    private int hasPot(int id, int targetSlot) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemPotion) {
                    ItemPotion pot = (ItemPotion) is.getItem();
                    if (pot.getEffects(is).isEmpty()) continue;
                    PotionEffect effect = pot.getEffects(is).get(0);
                    if (effect.getPotionID() == id) {
                        if (ItemPotion.isSplash(is.getItemDamage()) && isBestPot(pot, is)) {
                            if (36 + targetSlot != i) {
                                return i;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }

    private boolean hasPot(int id) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemPotion) {
                    ItemPotion pot = (ItemPotion) is.getItem();
                    if (pot.getEffects(is).isEmpty()) continue;
                    PotionEffect effect = pot.getEffects(is).get(0);
                    if (effect.getPotionID() == id) {
                        if (ItemPotion.isSplash(is.getItemDamage()) && isBestPot(pot, is)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isBestPot(ItemPotion potion, ItemStack stack) {
        if (potion.getEffects(stack) == null || potion.getEffects(stack).size() != 1)
            return false;
        PotionEffect effect = potion.getEffects(stack).get(0);
        int potionID = effect.getPotionID();
        int amplifier = effect.getAmplifier();
        int duration = effect.getDuration();
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemPotion) {
                    ItemPotion pot = (ItemPotion) is.getItem();
                    if (pot.getEffects(is) != null) {
                        for (PotionEffect potionEffect : pot.getEffects(is)) {
                            int id = potionEffect.getPotionID();
                            int ampl = potionEffect.getAmplifier();
                            int dur = potionEffect.getDuration();
                            if (id == potionID && ItemPotion.isSplash(is.getItemDamage())) {
                                if (ampl > amplifier) {
                                    return false;
                                } else if (ampl == amplifier && dur > duration) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private int getSlot() {
        int spoofSlot = 8;
        for (int i = 36; i < 45; i++) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                spoofSlot = i - 36;
                break;
            } else if (mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemPotion) {
                spoofSlot = i - 36;
                break;
            }
        }
        return spoofSlot;
    }

    private boolean inCage() {
        var block = mc.theWorld.getBlockState( new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock();
        return block.getMaterial() == Material.glass;
    }
}
