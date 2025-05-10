package io.justme.lavender.module.impl.blatant.fight;

import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.math.MathUtility;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.NumberRangeValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import org.lwjglx.input.Mouse;

/**
 * @author JustMe.
 * @since 2024/12/10
 **/
@Getter
@ModuleInfo(name = "AutoClicker", description = "autoClicker.", category = Category.FIGHT)
public class AutoClicker extends Module {


    private final NumberRangeValue delay = new NumberRangeValue("CPS", 10, 15, 5, 30,1);
    private final BoolValue blockingCheck = new BoolValue("Block Disabled", true);
    private final BoolValue breakingCheck = new BoolValue("Disable on Break", true);
    private final BoolValue displayingGuiCheck = new BoolValue("Disable in GUI", true);

    private boolean wasHoldingMouse;
    private boolean clickingTick;
    private final TimerUtility timerUtility = new TimerUtility();

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (mc.thePlayer != null) {
           Minecraft.getMinecraft().leftClickCounter = 0;
        }
    }

    @EventTarget
    public void onMotion(EventUpdate eventMotionUpdate) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (getDisplayingGuiCheck().getValue() && mc.currentScreen != null) return;
        if (mc.thePlayer.isBlocking() && getBlockingCheck().getValue()) return;
        if (mc.objectMouseOver == null) return;

        var pos = mc.objectMouseOver.getBlockPos();
        boolean finishedBreaking = false;
        if (pos != null) {
            float progress = mc.playerController.curBlockDamageMP;
            if (progress >= 1.0f) {
                finishedBreaking = true;
            }
        }

        if (finishedBreaking) {
            timerUtility.reset();
            clickingTick = true;
            wasHoldingMouse = true;
        }

        if (Mouse.isButtonDown(0)) {
            if (wasHoldingMouse && clickingTick) {
                Minecraft.getMinecraft().leftClickCounter = 0;
                Minecraft.getMinecraft().clickMouse();
                clickingTick = false;
            }
            wasHoldingMouse = true;
        } else {
            Minecraft.getMinecraft().leftClickCounter = 1;
            wasHoldingMouse = false;
        }

        if (wasHoldingMouse) {
            long minDelay = (long) (1000.0 / delay.getUpperValue());
            long maxDelay = (long) (1000.0 / delay.getLowerValue());
            long delayValue = (long) MathUtility.getRandomDouble(minDelay, maxDelay);

            if (timerUtility.getTime() >= delayValue) {
                timerUtility.reset();
                clickingTick = true;
            }
        }
    }
}
