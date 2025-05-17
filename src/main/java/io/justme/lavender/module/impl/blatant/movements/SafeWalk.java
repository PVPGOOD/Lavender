package io.justme.lavender.module.impl.blatant.movements;

import io.justme.lavender.events.player.EventSafeWalk;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.MovingObjectPosition;
import org.lwjglx.input.Mouse;

/**
 * @author JustMe.
 * @since 2024/4/29
 **/
@ModuleInfo(name = "SafeWalk", description = "SafeWalk.", category = Category.MOVEMENTS)
public class SafeWalk extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
    }
    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onSafeWalk(EventSafeWalk walk) {
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        boolean movingBackward = mc.thePlayer.moveForward < 0;

        boolean placingBlock = false;
        var ray = mc.thePlayer.rayTrace(5.0D, 0.0F);
        if (ray != null && ray.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            var held = mc.thePlayer.getHeldItem();
            if (held != null && held.getItem() instanceof ItemBlock && Mouse.isButtonDown(1)) {
                placingBlock = true;
            }
        }

        if (movingBackward || placingBlock) {
            walk.setCancel(true);
        }
    }
}
