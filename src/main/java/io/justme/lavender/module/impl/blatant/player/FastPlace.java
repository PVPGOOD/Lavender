package io.justme.lavender.module.impl.blatant.player;

import io.justme.lavender.La;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.math.TimerUtility;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.item.ItemBlock;

/**
 * @author JustMe.
 * @since 2024/4/14
 **/

@Getter
@Setter
@ModuleInfo(name = "FastPlace", description = "fast place.", category = Category.PLAYER)
public class FastPlace extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.setRightClickDelayTimer(4);
    }

    private TimerUtility timerUtility = new TimerUtility();
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (mc.thePlayer == null) {
            return;
        }

        var heldItem = mc.thePlayer.getHeldItem();

        var blockPrediction = La.getINSTANCE().getModuleManager().getModuleByName("BlockPrediction").isToggle();
        if (blockPrediction) {
            if (heldItem == null) {
                mc.setRightClickDelayTimer(0);
            }
        }

        if (heldItem != null && heldItem.getItem() instanceof ItemBlock) {
            mc.setRightClickDelayTimer(0);
        }
    }
}
