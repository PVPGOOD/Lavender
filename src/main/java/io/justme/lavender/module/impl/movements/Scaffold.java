package io.justme.lavender.module.impl.movements;

import io.justme.lavender.La;
import io.justme.lavender.events.game.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.player.PlayerUtility;
import io.justme.lavender.utility.player.RotationUtility;
import io.justme.lavender.utility.player.ScaffoldUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

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
    public ScaffoldUtility.BlockData blockData;
    private final TimerUtility timer = new TimerUtility(), towerTimer = new TimerUtility();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final NumberValue
            delayValue = new NumberValue("delayValue", 0.5, 0.0, 10.0, 0.5),
            boostValue = new NumberValue("boost", 1.0, 0.0, 5.0, 0.1),
            timesValue = new NumberValue("times", 8.0, 1.0, 15.0, 1.0),
            timerSpeed = new NumberValue("timer", 1.0, 1.0, 1.3, 0.05);

    private final ModeValue picks = new ModeValue("Pick Mode", new String[]{"Silent", "SwitchTo"}, "Silent");

    public final ModeValue
            markMode = new ModeValue("MarkMode", new String[]{"Zeroday", "Basic"}, "Basic");
    public final ModeValue
            rotationModeValue = new ModeValue("RotationMode", new String[]{"NCP", "Direction"}, "Direction");
    public final ModeValue
            keepYModeValue = new ModeValue("keepYMode", new String[]{"Always", "None", "OnlySpeed"}, "None");

    private final BoolValue
            towerValue = new BoolValue("towerValue",false),
            towerMoveValue = new BoolValue("towerMoveValue",false),
            positionValue = new BoolValue("positionValue",false),
            safeWalkValue = new BoolValue("safeWalkValue",false),
            sprintValue = new BoolValue("sprintValue",false),
            swingValue = new BoolValue("swingValue",false),
            eagleValue = new BoolValue("eagleValue",false),
            swing = new BoolValue("swing",false);


    @Override
    public void onEnable() {
        if (Minecraft.getMinecraft().thePlayer != null) posY = mc.thePlayer.posY;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            switch (picks.getValue()) {
                case "Silent": {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    break;
                }
                case "SwitchTo": {
                    mc.thePlayer.inventory.currentItem = prevSlot;
                    break;
                }
            }
            slot = -1;
            prevSlot = mc.thePlayer.inventory.currentItem;
        }

        super.onDisable();
    }

    private float[] rotations;
    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {

        switch(event.getType()) {
            case PRE -> {
                setBlockData(ScaffoldUtility.getBlockData(posY));

                event.setYaw(RotationUtility.getMoveYaw(event.getYaw()) - 180);
                event.setPitch(75.7F);

                if (getBlockData() == null) return;

                slot = getSlot();

                if (slot == -1) {
                    return;
                }


                if (picks.getValue().equalsIgnoreCase("Silent")) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(slot));
                } else if (picks.getValue().equalsIgnoreCase("SwitchTo")) {
                    mc.thePlayer.inventory.currentItem = slot;
                }

                if (!timer.hasTimeElapsed(delayValue.getValue().intValue() * 100L)) return;


                if (mc.playerController.onPlayerRightClick(mc.thePlayer,
                        mc.theWorld, mc.thePlayer.inventory.mainInventory[slot], getBlockData().getBlockPos(), getBlockData().getEnumFacing(), getVec3(getBlockData().getBlockPos(), getBlockData().getEnumFacing()))) {

                    if (swing.getValue()) mc.thePlayer.swingItem();
                    else mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                }

                timer.reset();
            }
            case POST -> {
                switch (keepYModeValue.getValue()) {
                    case "None":
                        posY = mc.thePlayer.posY;
                        break;

                    case "Normal":
                        if (posY > mc.thePlayer.posY || mc.thePlayer.fallDistance > 1.5) posY = mc.thePlayer.posY;

                        if (PlayerUtility.isOnGround(1.15) && !PlayerUtility.moving() &&
                                !PlayerUtility.isOnGround(-2) && mc.gameSettings.keyBindJump.isPressed() && towerValue.getValue()) {
                            posY = mc.thePlayer.posY;
                        }
                        break;

                    case "OnlySpeed":
                        if (La.getINSTANCE().getModuleManager().getModuleByName("Speed").isToggle() && !mc.gameSettings.keyBindJump.isPressed()){
                            if (posY > mc.thePlayer.posY || mc.thePlayer.fallDistance > 1.5) posY = mc.thePlayer.posY;

                            if (PlayerUtility.isOnGround(1.15) && !PlayerUtility.moving() && !PlayerUtility.isOnGround(-2) && mc.gameSettings.keyBindJump.isPressed() && towerValue.getValue()) {
                                posY = mc.thePlayer.posY;
                            }
                        } else {
                            posY = mc.thePlayer.posY;
                        }
                        break;
                }


                if (PlayerUtility.isOnGround(1.15) && !PlayerUtility.isOnGround(-2) && mc.gameSettings.keyBindJump.isPressed() && towerValue.getValue()) {
                    if (PlayerUtility.moving()) {
                        if (towerMoveValue.getValue() && keepYModeValue.getValue().equals("OnlySpeed") || keepYModeValue.getValue().equals("None")) {

                            if (PlayerUtility.isOnGround(.76) && !PlayerUtility.isOnGround(.75) && mc.thePlayer.motionY > .23 && mc.thePlayer.motionY < .25)
                                mc.thePlayer.motionY = (double) Math.round(mc.thePlayer.posY) - mc.thePlayer.posY;
                            if (PlayerUtility.isOnGround(1.0E-4)) {
                                mc.thePlayer.motionY = .41999998688698;
                                mc.thePlayer.motionX *= .9;
                                mc.thePlayer.motionZ *= .9;
                            } else if (mc.thePlayer.posY >= (double) Math.round(mc.thePlayer.posY) - 1.0E-4 && mc.thePlayer.posY <= (double) Math.round(mc.thePlayer.posY) + 1.0E-4)
                                mc.thePlayer.motionY = 0;
                        }

                        if (mc.timer.timerSpeed == 1 + (boostValue.getValue().floatValue() == 0 ? 0 : boostValue.getValue().floatValue() + .015555F)) {
                            mc.timer.timerSpeed = 1;
                        }
                        towerTick = 0;
                    } else {
                        if (PlayerUtility.isAirUnder(mc.thePlayer)) {

                            PlayerUtility.setSpeed(0);
                            mc.thePlayer.setPosition(mc.thePlayer.prevPosX, mc.thePlayer.posY, mc.thePlayer.prevPosZ);

                            if (++towerTick <= timesValue.getValue().intValue()) {

                                mc.thePlayer.jump();

                                if (blockData != null && positionValue.getValue()) {
                                    mc.thePlayer.setPosition(blockData.getBlockPos().getX() + .5, mc.thePlayer.posY, blockData.getBlockPos().getZ() + .5);
                                }

                                if (towerTick > 0) {
                                    mc.timer.timerSpeed = 1 + (boostValue.getValue().floatValue() == 0 ? 0 : boostValue.getValue().floatValue() + .015555F);
                                }
                            } else {

                                towerTick = 0;
                                if (mc.timer.timerSpeed == 1 + (boostValue.getValue().floatValue() == 0 ? 0 : boostValue.getValue().floatValue() + .015555F)) {
                                    mc.timer.timerSpeed = 1;
                                }
                            }
                        }
                    }
                } else {

                    if (mc.timer.timerSpeed == 1 + (boostValue.getValue().floatValue() == 0 ? 0 : boostValue.getValue().floatValue() + .015555F)) {
                        mc.timer.timerSpeed = 1;
                    }
                    towerTick = 0;
                }
            }
        }
    }


    private int getSlot() {
        int slot = -1;
        int size = 0;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
            if (stack != null && stack.stackSize > 0 && stack.getItem() instanceof ItemBlock && ScaffoldUtility.isValid(((ItemBlock) stack.getItem())) && size < stack.stackSize) {
                size = stack.stackSize;
                slot = i;
            }
        }
        return slot;
    }
    public Vec3 getVec3(BlockPos pos, EnumFacing face) {
        double x = (double) pos.getX() + 0.5;
        double y = (double) pos.getY() + 0.5;
        double z = (double) pos.getZ() + 0.5;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += randomNumber(0.3, -0.3);
            z += randomNumber(0.3, -0.3);
        } else {
            y += randomNumber(0.3, -0.3);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += randomNumber(0.3, -0.3);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += randomNumber(0.3, -0.3);
        }
        return new Vec3(x, y, z);
    }

    public double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

}
