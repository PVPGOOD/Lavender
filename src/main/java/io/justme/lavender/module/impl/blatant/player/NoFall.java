package io.justme.lavender.module.impl.blatant.player;

import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.player.MovementUtility;
import io.justme.lavender.utility.player.PlayerUtility;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

/**
 * @author JustMe.
 * @since 2024/5/2
 **/

@Getter
@Setter
@ModuleInfo(name = "NoFall", description = "", category = Category.PLAYER)
public class NoFall extends Module {

    private final ModeValue ModeValue = new ModeValue("Mode", new String[]{"jump", "spoofGround","Watchdog"}, "jump");
    private final NumberValue distance = new NumberValue("minDistance", 3, 0, 8, 1);

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private boolean jumped;
    private boolean shouldJumped;
    private boolean prevOnGround = false;
    private double fallDistance = 0;
    private boolean timed = false;


    @EventTarget
    public void onMotion(EventMotionUpdate event) {

        if (mc.thePlayer.onGround)
            fallDistance = 0;
        else {
            fallDistance += (float) Math.max(mc.thePlayer.lastTickPosY - event.getY(), 0);

            fallDistance -= PlayerUtility.predictedMotionY(mc.thePlayer.motionY, 1);
        }

        if (mc.thePlayer.capabilities.allowFlying) return;

        if (isVoid()) {
            return;
        }

        switch (getModeValue().getValue()) {
            case "jump" -> {
                double blockY = distanceFromGround();
                if (mc.thePlayer.fallDistance > 5 && !jumped) {
                    //差值为1
                    if (mc.thePlayer.posY - blockY <= 1) {
                        setShouldJumped(true);
                    }
                }

                if (isShouldJumped()) {
                    mc.thePlayer.motionY = 0.42f;
                    setJumped(true);
                    setShouldJumped(false);
                }

                if (PlayerUtility.isOnGround()) {
                    setJumped(false);
                }
            }

            case "spoofGround" -> {
                if (mc.thePlayer.fallDistance > 3.0f) {
                    event.setOnGround(true);
                    mc.thePlayer.fallDistance = 0.0f;
                }
            }
            case "Watchdog" -> {
                if (fallDistance >= getDistance().getValue()) {
                    mc.timer.timerSpeed = (float) 0.5;
                    timed = true;
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                    fallDistance = 0;
                } else if (timed) {
                    mc.timer.timerSpeed = 1;
                    timed = false;
                }
            }
        }
    }

    private double distanceFromGround() {
        for (double y = mc.thePlayer.posY; y >= 0; y--) {
            if (!mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ))) {
                return y;
            }
        }
        return 0;
    }

    private boolean isVoid() {
        for (double y = mc.thePlayer.posY; y >= 0; y--) {
            if (!mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ))) {
                return false; // 找到非空气方块，说明不是虚空
            }
        }
        return true; // 全是空气，说明是虚空
    }
}
