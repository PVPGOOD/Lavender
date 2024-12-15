package io.justme.lavender.module.impl.blatant.movements.speed.impl.watchdog;

import io.justme.lavender.La;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventMove;
import io.justme.lavender.events.player.EventStrafe;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.impl.blatant.movements.speed.AbstractSpeed;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.player.MovementUtility;
import io.justme.lavender.utility.player.PlayerUtility;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;

/**
 * @author JustMe.
 * @since 2024/5/10
 **/
@Getter
@Setter
public class WatchdogLowHopSpeed extends AbstractSpeed {

    private boolean canJump = false;
    private TimerUtility timerUtility = new TimerUtility();

    public WatchdogLowHopSpeed() {
        super("WatchdogLowHop");
    }
    

    @Override
    public void onEnable() {

        getTimerUtility().reset();
        if (!PlayerUtility.isOnGround()) {
            offGroundTicks = -1;
        }
    }

    @Override
    public void onDisable() {
        getTimerUtility().reset();
        if (!PlayerUtility.isOnGround()) {
            offGroundTicks = -1;
        }
    }

    @Override
    public void onPacket(EventPacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            La.getINSTANCE().getNotificationsManager().push("Lag Check","speed was disabled.", NotificationsEnum.WARNING,1);
            La.getINSTANCE().getModuleManager().getModuleByName("Speed").setStatus(false);
        }
    }

    @Override
    public void onMotionUpdate(EventMotionUpdate event) {

    }
    private boolean reset;
    @Override
    public void onUpdate(EventUpdate event) {

        if (PlayerUtility.isOnGround()) {
            offGroundTicks = 0;
        }

        if (offGroundTicks != -1) {
            if (PlayerUtility.isOnGround()) {
                offGroundTicks = 0;
            } else {
                offGroundTicks++;
            }
        }

        if (PlayerUtility.moving()) {
            switch (offGroundTicks) {
                case 0:
                    mc.thePlayer.motionY = jumpBoostMotion(0.42f);

                    if (mc.thePlayer.isCollidedVertically && !PlayerUtility.isInLiquid()) {
//                        if (mc.thePlayer.hurtTime > 4) {
//                            mc.thePlayer.motionX *= 1.007;
//                            mc.thePlayer.motionZ *= 1.007;
//                        }

                        if (mc.thePlayer.hurtTime == 0) {
                            if (mc.thePlayer.motionY < 0.1 && mc.thePlayer.motionY > 0.01) {
                                mc.thePlayer.motionX *= 1.002;
                                mc.thePlayer.motionZ *= 1.002;
                            }

                            if (mc.thePlayer.motionY < 0.005 && mc.thePlayer.motionY > 0) {
                                mc.thePlayer.motionX *= 1.002;
                                mc.thePlayer.motionZ *= 1.002;
                            }

                            if (mc.thePlayer.motionY < 0.001 && mc.thePlayer.motionY > -0.03) {
                                mc.thePlayer.motionX *= 1.002;
                                mc.thePlayer.motionZ *= 1.002;
                            }
                        }
                            if (mc.thePlayer.onGround) {
                                mc.thePlayer.motionY = PlayerUtility.getJumpBoostModifier(.42F);
                                PlayerUtility.setMotion((float) Math.max(PlayerUtility.getBaseMoveSpeed(), .4756F + .04F * PlayerUtility.getSpeedEffect()));
                            }
                        }


                        break;
                    case 1:
                        if (mc.thePlayer.hurtTime == 0) {
                            mc.thePlayer.motionY = 0.39;
                        }
                        break;
                    case 3:
                        if (mc.thePlayer.hurtTime == 0) {
                            mc.thePlayer.motionY -= 0.13;
                        }
                        break;
                    case 4:
                        if (mc.thePlayer.hurtTime == 0) {
                            mc.thePlayer.motionY -= 0.2;
                        }
                        break;
                }
            }
            if (mc.thePlayer.isCollidedHorizontally && PlayerUtility.moving() && mc.thePlayer.onGround) {
                PlayerUtility.setMotion(PlayerUtility.getBaseMoveSpeed());
            }

    }

    @Override
    public void onMove(EventMove event) {

    }
    public int offGroundTicks = 0;
    @Override
    public void onStrafe(EventStrafe event) {
        if (!PlayerUtility.moving() || noAction()) return;


    }

    public double jumpBoostMotion(final double motionY) {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return motionY + (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
        }

        return motionY;
    }



    private boolean noAction() {
        return ((mc.thePlayer.isInWater()
                || mc.thePlayer.isInLava()))
                || (mc.thePlayer.isSneaking());
    }
}
