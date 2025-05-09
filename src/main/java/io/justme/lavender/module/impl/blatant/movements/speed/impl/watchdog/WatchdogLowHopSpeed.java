package io.justme.lavender.module.impl.blatant.movements.speed.impl.watchdog;

import io.justme.lavender.La;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventMove;
import io.justme.lavender.events.player.EventStrafe;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.module.impl.blatant.movements.speed.AbstractSpeed;
import io.justme.lavender.module.impl.blatant.movements.speed.Speed;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.player.MovementUtility;
import io.justme.lavender.utility.player.PlayerUtility;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
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
        if (mc.thePlayer.onGround && PlayerUtility.moving()) {
            mc.thePlayer.jump();

            MovementUtility.strafe(0.47 + PlayerUtility.getSpeedEffect() * 0.042);
            couldStrafe = true;
        }

    }

    @Override
    public void onMove(EventMove event) {

    }
    public int offGroundTicks = 0;
    
    private double speed;
    private boolean recentlyCollided;
    private boolean disable;
    private boolean disable3;
    private int boostTicks;
    private boolean slab;
    public boolean couldStrafe;
    @EventTarget
    public void onStrafe(EventStrafe event) {


        var wdFastMode = ((Speed) La.getINSTANCE().getModuleManager().getModuleByName("Speed")).getStrafeMode();
        var wdMode = ((Speed) La.getINSTANCE().getModuleManager().getModuleByName("Speed")).getJumpMode();
        var fastFall = ((Speed) La.getINSTANCE().getModuleManager().getModuleByName("Speed")).getFastFall();
        var extraStrafe = ((Speed) La.getINSTANCE().getModuleManager().getModuleByName("Speed")).getExtraStrafe();
        boolean disable = false;

 

            switch (wdMode.getValue()) {
                case "Glide":
                    if (PlayerUtility.moving() && mc.thePlayer.onGround) {
                        MovementUtility.strafe(PlayerUtility.getAllowedHorizontalDistance());
                        mc.thePlayer.jump();
                    }

                    if (mc.thePlayer.onGround) {
                        speed = 1.0F;
                    }

                    final int[] allowedAirTicks = new int[]{10, 11, 13, 14, 16, 17, 19, 20, 22, 23, 25, 26, 28, 29};

                    if (!(mc.theWorld.getBlockState(mc.thePlayer.getPosition().add(0, -0.25, 0)).getBlock() instanceof BlockAir)) {
                        for (final int allowedAirTick : allowedAirTicks) {
                            if (offGroundTicks == allowedAirTick && allowedAirTick <= 11) {
                                mc.thePlayer.motionY = 0;
                                MovementUtility.strafe(PlayerUtility.getAllowedHorizontalDistance() * speed);
                                couldStrafe = true;

                                speed *= 0.98F;

                            }
                        }
                    }
                    break;
                case "Fast":
                    
                    if (!disable && fastFall.getValue()) {

                        switch (wdFastMode.getValue()) {
                            case "7 Tick", "8 Tick Strafe":
                                switch (offGroundTicks) {
                                    case 1:
                                        mc.thePlayer.motionY += 0.057f;
                                        break;
                                    case 3:
                                        mc.thePlayer.motionY -= 0.1309f;
                                        break;
                                    case 4:
                                        mc.thePlayer.motionY -= 0.2;
                                        break;
                                }
                                break;

                            case "8 Tick Fast":
                                switch (offGroundTicks) {
                                    case 3:
                                        mc.thePlayer.motionY = mc.thePlayer.motionY - 0.02483;
                                        break;
                                    case 5:
                                        mc.thePlayer.motionY = mc.thePlayer.motionY - 0.1913;
                                        break;
                                }
                                break;
                            case "9 Tick":
                                switch (offGroundTicks) {
                                    case 3:
                                        mc.thePlayer.motionY = mc.thePlayer.motionY - 0.02483;
                                        break;
                                    case 5:
                                        mc.thePlayer.motionY = mc.thePlayer.motionY - 0.16874;
                                        break;
                                }
                                break;
                        }

                    }

                    if (offGroundTicks == 1 && !disable) {
//                        if (isEnabled(Scaffold.class)) {
//                            if (getModule(Scaffold.class).towerMoving())
//                                MovementUtils.strafe(0.3);
//                        } else {
                            MovementUtility.strafe(Math.max(MovementUtility.getSpeed(), 0.33f + PlayerUtility.getSpeedEffect() * 0.075));
                            couldStrafe = true;
//                        }
                    }

                    if (offGroundTicks == 2 && !disable && extraStrafe.getValue()) {
                        double motionX3 = mc.thePlayer.motionX;
                        double motionZ3 = mc.thePlayer.motionZ;
                        mc.thePlayer.motionZ = (mc.thePlayer.motionZ * 1 + motionZ3 * 2) / 3;
                        mc.thePlayer.motionX = (mc.thePlayer.motionX * 1 + motionX3 * 2) / 3;
                    }

                    if (offGroundTicks == 6 && wdFastMode.getValue().equalsIgnoreCase("8 Tick Strafe") &&
                            !disable && PlayerUtility.blockRelativeToPlayer(0, mc.thePlayer.motionY * 3, 0) != Blocks.air &&
                            PlayerUtility.blockRelativeToPlayer(0, mc.thePlayer.motionY * 3, 0).isFullBlock()) {
                        mc.thePlayer.motionY += 0.0754;
                        MovementUtility.strafe();
                        couldStrafe = true;
                    }

                    if ((mc.thePlayer.motionX == 0 || mc.thePlayer.motionZ == 0) && !disable && (!recentlyCollided && mc.thePlayer.isPotionActive(Potion.moveSpeed)) ) {
                        MovementUtility.strafe();
                        couldStrafe = true;
                    }

                    if (offGroundTicks < 7 && (PlayerUtility.blockRelativeToPlayer(0, mc.thePlayer.motionY, 0) != Blocks.air) && mc.thePlayer.isPotionActive(Potion.moveSpeed) && !slab) {
                        boostTicks = mc.thePlayer.ticksExisted + 9;
                        recentlyCollided = true;
                    }

                    if (offGroundTicks == 7 && !disable && (PlayerUtility.blockRelativeToPlayer(0, mc.thePlayer.motionY * 2, 0) != Blocks.air)) {
                        MovementUtility.strafe(MovementUtility.getSpeed());
                        couldStrafe = true;
                    }

                    if (PlayerUtility.blockRelativeToPlayer(0, mc.thePlayer.motionY, 0) != Blocks.air && offGroundTicks > 5 && !disable3) {
                        MovementUtility.strafe();
                        couldStrafe = true;
                        disable3 = true;
                    }

//                    double speed2 = Math.hypot((mc.thePlayer.motionX - (mc.thePlayer.lastTickPosX - mc.thePlayer.lastLastTickPosX)), (mc.thePlayer.motionZ - (mc.thePlayer.lastTickPosZ - mc.thePlayer.lastLastTickPosZ)));
//                    if (speed2 < .0125 && frictionOverride.get()) {
//                        MovementUtils.strafe();
//                        couldStrafe = true;
//                    }

                    break;

            }
        
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
