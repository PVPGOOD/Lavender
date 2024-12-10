package io.justme.lavender.module.impl.blatant.player;

import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.player.InventoryUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.val;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerChest;

/**
 * @author JustMe.
 * @since 2024/5/1
 **/

@Getter
@ModuleInfo(name = "ChestStealer", description = "", category = Category.PLAYER)
public class ChestStealer extends Module {

    private final NumberValue delay = new NumberValue("Delay", 500, 10, 1000, 10);

    private final BoolValue nameCheck = new BoolValue("Name Check", true);

    private final TimerUtility timerUtility = new TimerUtility();

    @Override
    public void onEnable() {
        super.onEnable();
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.getMinecraft().thePlayer.openContainer instanceof ContainerChest container) {

            val name = container.getLowerChestInventory().getDisplayName().getFormattedText();

            if ((name.equals(I18n.format("container.chest")) || name.equals(I18n.format("container.chestDouble"))) || !nameCheck.getValue()) {
                if (isOpenChestEmpty() || isInventoryFull()) {
                    Minecraft.getMinecraft().thePlayer.closeScreen();
                    return;
                }

                for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
                    if (container.getLowerChestInventory().getStackInSlot(i) == null) continue;

                    if (timerUtility.hasTimeElapsed(delay.getValue().longValue())) {

                        InventoryUtility.windowClick(container.windowId, i, 0, 1, Minecraft.getMinecraft().thePlayer);
                        timerUtility.reset();
                    }
                }
            }
        }
    }

    public boolean isOpenChestEmpty() {
        if (Minecraft.getMinecraft().thePlayer.openContainer instanceof ContainerChest container) {

            for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
                val itemStack = container.getLowerChestInventory().getStackInSlot(i);

                if (itemStack != null && itemStack.getItem() != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isInventoryFull() {
        for (int i = 9; i < 45; i++) {
            if (!Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) return false;
        }

        return true;
    }
}
