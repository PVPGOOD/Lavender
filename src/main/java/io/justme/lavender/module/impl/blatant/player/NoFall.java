package io.justme.lavender.module.impl.blatant.player;

import io.justme.lavender.La;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.render.Event2DRender;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
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

    private final ModeValue ModeValue = new ModeValue("Mode", new String[]{"jump", "spoofGround","Watchdog","Watchdog Blink"}, "jump");
    private final NumberValue distance = new NumberValue("minDistance", 3, 0, 8, 1);

    private boolean jumped;
    private boolean shouldJumped;
    private boolean prevOnGround = false;
    private double fallDistance = 0;
    private boolean timed = false;
    private boolean blinked;

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (blinked) {
            La.getINSTANCE().getBlinkComponent().dispatch();
            blinked = false;
        }
    }

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
            if (blinked) {
                La.getINSTANCE().getBlinkComponent().dispatch();
                blinked = false;
            }
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
            case "Watchdog Blink" -> {
                if (La.getINSTANCE().getBlinkComponent().packets.size() > 20) {
                    La.getINSTANCE().getBlinkComponent().dispatch();
                }

                if (mc.thePlayer.onGround) {
                    if (blinked) {
                        La.getINSTANCE().getBlinkComponent().dispatch();
                        blinked = false;
                    }

                    this.prevOnGround = true;
                } else if (this.prevOnGround) {

                    if (shouldBlink()) {
                        if (!La.getINSTANCE().getBlinkComponent().blinking)
                            La.getINSTANCE().getBlinkComponent().blinking = true;
                        blinked = true;
                    }

                    prevOnGround = false;
                } else if (PlayerUtility.isBlockUnder() && La.getINSTANCE().getBlinkComponent().blinking && (this.fallDistance - mc.thePlayer.motionY) >= distance.getValue()) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    this.fallDistance = 0.0F;
                    La.getINSTANCE().print("Spoof");
                }
            }
        }
    }



    @EventTarget
    public void onRender2D(Event2DRender event) {

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
        return true;
    }

    private boolean shouldBlink() {
        return !mc.thePlayer.onGround && !PlayerUtility.isBlockUnder((int) Math.floor(distance.getValue().intValue())) && PlayerUtility.isBlockUnder() ;
    }
}
