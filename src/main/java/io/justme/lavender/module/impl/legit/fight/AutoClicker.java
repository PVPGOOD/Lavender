package io.justme.lavender.module.impl.legit.fight;

import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.math.MathUtility;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.value.impl.BoolValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import org.lwjglx.input.Mouse;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author JustMe.
 * @since 2024/12/10
 **/
@Getter
@ModuleInfo(name = "AutoClicker", description = "autoClicker.", category = Category.FIGHT)
public class AutoClicker extends Module {

    private final BoolValue blockingCheck = new BoolValue("blocking Check", true);
    private final BoolValue breakingCheck = new BoolValue("breaking Check", true);
    private final BoolValue displayingGuiCheck = new BoolValue("displaying gui Check", true);


    private boolean wasHoldingMouse;
    private boolean clickingTick;
    private final TimerUtility timerUtility = new TimerUtility();


    @EventTarget
    public void onMotion(EventMotionUpdate eventMotionUpdate) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (getDisplayingGuiCheck().getValue() && mc.currentScreen != null) return;
        if (mc.thePlayer.isBlocking() && getBlockingCheck().getValue()) return;


        if (eventMotionUpdate.getType() == EnumEventType.PRE) {
            var breakingBlock = false;
            var blockpos = mc.objectMouseOver.getBlockPos();

            if (blockpos != null) {
                if (mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                    if (getBlockingCheck().getValue()) return;
                }
            }


            if(Mouse.isButtonDown(0) && !breakingBlock) {
                if(wasHoldingMouse && clickingTick) {
                    Minecraft.getMinecraft().leftClickCounter = 0;
                    Minecraft.getMinecraft().clickMouse();

                    clickingTick = false;
                }

                wasHoldingMouse = true;
            } else {
                Minecraft.getMinecraft().leftClickCounter = 1;
                wasHoldingMouse = false;
            }

            if(wasHoldingMouse) {
                long maxDelay = (long) (1000.0 / MathUtility.getRandomDouble(2,4));
                long minDelay = (long) (1000.0 /  MathUtility.getRandomDouble(12,16));

                long delay =minDelay;

                if(timerUtility.getTime() >= delay) {
                    timerUtility.reset();
                    clickingTick = true;
                }
            }
        }
    }
}
