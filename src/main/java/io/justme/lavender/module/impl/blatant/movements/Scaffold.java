package io.justme.lavender.module.impl.blatant.movements;

import io.justme.lavender.La;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventSafeWalk;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.network.PacketUtility;
import io.justme.lavender.utility.player.PlayerUtility;
import io.justme.lavender.utility.player.RotationUtility;
import io.justme.lavender.utility.player.ScaffoldUtility;
import io.justme.lavender.utility.world.WorldUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;

/**
 * @author JustMe.
 * @since 2024/4/6
 **/

@Getter
@Setter
@ModuleInfo(name = "Scaffold", description = "idk.", category = Category.MOVEMENTS)
public class Scaffold extends Module {

    private double posY;
    private int slot, prevSlot, towerTick;

    public final ModeValue
            rotationModeValue = new ModeValue("Rotation", new String[]{"Normal"}, "Normal"),
            keepYModeValue = new ModeValue("keepY", new String[]{"Always", "None", "OnlySpeed"}, "None"),
            picks = new ModeValue("PickBlock", new String[]{"Silent", "SwitchTo"}, "Silent"),
            markMode = new ModeValue("Mark", new String[]{"Zeroday", "Basic"}, "Basic");

    private final NumberValue
            delayValue = new NumberValue("delayValue", 0.5, 0.0, 10.0, 0.5),
            timesValue = new NumberValue("timesValue", 8.0, 1.0, 15.0, 1.0),
            timerSpeed = new NumberValue("timerSpeed", 1.0, 1.0, 1.3, 0.05);

    private final BoolValue
            towerValue = new BoolValue("Tower",false),
            towerMoveValue = new BoolValue("Tower move",false),
            positionValue = new BoolValue("Position",false),
            safeWalkValue = new BoolValue("SafeWalk",false),
            sprintValue = new BoolValue("Sprint",false),
            swingValue = new BoolValue("Swing",false),
            eagleValue = new BoolValue("Eagle",false),
            rayCast_Value = new BoolValue("ray cast",false),
            swing = new BoolValue("Swing",false);

    public ScaffoldUtility.BlockData blockData;
    private PacketUtility packetUtility = new PacketUtility();
    private final TimerUtility placeTimer = new TimerUtility(), towerTimer = new TimerUtility();
    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void onEnable() {
        if (Minecraft.getMinecraft().thePlayer != null) setPosY(mc.thePlayer.posY);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {

            switch (getPicks().getValue()) {
                case "Silent": {
                    getPacketUtility().sendPacketFromLa(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    break;
                }
                case "SwitchTo": {
                    mc.thePlayer.inventory.currentItem = getPrevSlot();
                    break;
                }
            }

            setSlot(-1);
            setPrevSlot(mc.thePlayer.inventory.currentItem);
        }

        super.onDisable();
    }

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {

        switch(event.getType()) {
            case PRE -> {
                setBlockData(ScaffoldUtility.getBlockData(
                        new BlockPos(mc.thePlayer.posX, posY - 1, mc.thePlayer.posZ)) == null ?
                        ScaffoldUtility.getBlockData(new BlockPos(mc.thePlayer.posX, posY - 1, mc.thePlayer.posZ).down()) :
                        ScaffoldUtility.getBlockData(new BlockPos(mc.thePlayer.posX, posY - 1, mc.thePlayer.posZ)));

                if (getBlockData() == null) return;

                rotation(event);

                setSlot(ScaffoldUtility.getSlot());

                if (getSlot() == -1) return;

                switch (getPicks().getValue()) {
                    case "Silent" : {
                        getPacketUtility().sendPacketFromLa(new C09PacketHeldItemChange(getSlot()));
                    }
                    case "SwitchTo" : {
                        mc.thePlayer.inventory.currentItem = getSlot();
                    }
                }

                if (getPlaceTimer().hasTimeElapsed(getDelayValue().getValue() * 100L)) {
                    if (canPlace()) {
                        place();
                    }
                }

                getPlaceTimer().reset();
            }
            case POST -> {
                switch (getKeepYModeValue().getValue()) {
                    case "None":
                        setPosY(mc.thePlayer.posY);
                       break;
                    case "Normal":
                        if (getPosY() > mc.thePlayer.posY || mc.thePlayer.fallDistance > 1.5) {
                            setPosY(mc.thePlayer.posY);
                        }

                        if (PlayerUtility.isOnGround(1.15) &&
                                !PlayerUtility.moving() &&
                                !PlayerUtility.isOnGround(-2) &&
                                mc.gameSettings.keyBindJump.pressed &&
                                getTowerMoveValue().getValue()) {

                            setPosY(mc.thePlayer.posY);
                        }
                        break;

                    case "OnlySpeed":
                        if (La.getINSTANCE().getModuleManager().getModuleByName("Speed").isToggle() && !mc.gameSettings.keyBindJump.pressed){
                            if (getPosY() > mc.thePlayer.posY || mc.thePlayer.fallDistance > 1.5) posY = mc.thePlayer.posY;

                            if (PlayerUtility.isOnGround(1.15) && !PlayerUtility.moving() && !PlayerUtility.isOnGround(-2) && mc.gameSettings.keyBindJump.pressed && towerValue.getValue()) {
                                setPosY(mc.thePlayer.posY);
                            }
                        } else {
                            setPosY(mc.thePlayer.posY);
                        }
                        break;
                }


                if (PlayerUtility.isOnGround(1.15) && !PlayerUtility.isOnGround(-2) && mc.gameSettings.keyBindJump.pressed && towerValue.getValue()) {
                    if (PlayerUtility.moving()) {
                        if (getTowerMoveValue().getValue() && getKeepYModeValue().getValue().equals("OnlySpeed") || getKeepYModeValue().getValue().equals("None")) {

                            if (PlayerUtility.isOnGround(.76) && !PlayerUtility.isOnGround(.75) && mc.thePlayer.motionY > .23 && mc.thePlayer.motionY < .25)
                                mc.thePlayer.motionY = (double) Math.round(mc.thePlayer.posY) - mc.thePlayer.posY;
                            if (PlayerUtility.isOnGround(1.0E-4)) {
                                mc.thePlayer.motionY = .41999998688698;
                                mc.thePlayer.motionX *= .9;
                                mc.thePlayer.motionZ *= .9;
                            } else if (mc.thePlayer.posY >= (double) Math.round(mc.thePlayer.posY) - 1.0E-4 && mc.thePlayer.posY <= (double) Math.round(mc.thePlayer.posY) + 1.0E-4)
                                mc.thePlayer.motionY = 0;
                        }

                       setTowerTick(0);
                    } else {
                        if (PlayerUtility.isAirUnder(mc.thePlayer)) {

                            PlayerUtility.setSpeed(0);
                            mc.thePlayer.setPosition(mc.thePlayer.prevPosX, mc.thePlayer.posY, mc.thePlayer.prevPosZ);

                            if (++towerTick <= timesValue.getValue().intValue()) {

                                mc.thePlayer.jump();

                                if (getBlockData() != null && getPositionValue().getValue()) {
                                    mc.thePlayer.setPosition(blockData.getBlockPos().getX() + .5, mc.thePlayer.posY, blockData.getBlockPos().getZ() + .5);
                                }

                            } else {
                              setTowerTick(0);
                            }
                        }
                    }
                } else {
                   setTowerTick(0);
                }
            }
        }
    }

    private float yaw,pitch;
    private void rotation(EventMotionUpdate event) {
        switch (getRotationModeValue().getValue()) {
            case "Normal" : {
                Block under = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - .5F, mc.thePlayer.posZ)).getBlock();
                if (under == Blocks.air || !under.isFullBlock()) {

                    var vec = getBlockData().getVec3();

                    float[] rotations = RotationUtility.getRotationsToPosition(vec.xCoord, vec.yCoord, vec.zCoord);

                    var yaw = rotations[0];
                    var pitch = 80f;

                    if (getBlockData().getFacing() == EnumFacing.UP) {
                        pitch = 90f;
                    }

                    for(float i = 75; i <= 85; i += 0.1F) {
                        MovingObjectPosition movingObjectPosition = WorldUtility.raytrace(yaw, i);
                        if(movingObjectPosition != null && movingObjectPosition.sideHit == getBlockData().getFacing()) {
                            pitch = i;
                            break;
                        }
                    }

                    if (yaw < 0) yaw += 360;

                    setYaw(yaw);
                    setPitch(pitch);
                }

                event.setPitch(getPitch());
                event.setYaw(getYaw());
            }
            case "Back" : {
//                event.setYaw(RotationUtility.getMoveYaw(event.getYaw()) - 180);
//                event.setPitch(75.7F);
            }
        }
    }

    private void place() {
        if (mc.playerController.onPlayerRightClick(
                mc.thePlayer,
                mc.theWorld,
                mc.thePlayer.inventory.mainInventory[slot],
                getBlockData().getBlockPos(),
                getBlockData().getEnumFacing(),
                getBlockData().getVec3())) {

            if (getSwing().getValue()) {
                mc.thePlayer.swingItem();
            } else {
                getPacketUtility().sendPacketFromLa(new C0APacketAnimation());
            }
        }
    }

    private boolean canPlace() {
        return !getRayCast_Value().getValue() || raytrace();
    }

    private boolean raytrace() {
        var movingObjectPosition = WorldUtility.raytrace(getYaw(),getPitch());

        //合法的hitVec
        if(movingObjectPosition != null && movingObjectPosition.sideHit == getBlockData().getFacing() && movingObjectPosition.getBlockPos().equals(getBlockData().getPos())) {
           getBlockData().setVec3(movingObjectPosition.hitVec);
            return true;
        }

        return false;
    }

    @EventTarget
    public void onSafeWalk(EventSafeWalk eventSafeWalk) {
        eventSafeWalk.setCancel(getSafeWalkValue().getValue());
    }

}
