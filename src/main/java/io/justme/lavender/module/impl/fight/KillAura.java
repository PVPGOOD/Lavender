package io.justme.lavender.module.impl.fight;

import com.viaversion.viarewind.protocol.protocol1_8to1_9.Protocol1_8To1_9;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.interfaces.IMinecraft;
import io.justme.lavender.utility.math.MathUtility;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.network.PacketUtility;
import io.justme.lavender.utility.player.PlayerUtility;
import io.justme.lavender.utility.player.RotationUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.MultiBoolValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author JustMe.
 * @since 2024/3/23
 **/

@Getter
@Setter
@ModuleInfo(name = "KillAura", description = "IDK.", category = Category.FIGHT)
public class KillAura extends Module implements IMinecraft {

    private final TimerUtility attackTimer = new TimerUtility(), switchTimer = new TimerUtility();
    private final ArrayList<EntityLivingBase> targets = new ArrayList<>();
    private final PacketUtility packetUtility = new PacketUtility();
    private EntityLivingBase target, blockingTarget;
    private int index;

    private final BoolValue
            autoBlock = new BoolValue("AutoBlock",false);

    private final ModeValue
            attackMode = new ModeValue("Mode", new String[]{"Single", "Switch"}, "Switch");

    private final NumberValue
            cps = new NumberValue("CPS", 15.0, 1, 20.0, 1),
            switchDelay = new NumberValue("Switch Ms", 15.0, 0.1, 20.0, 0.1, () -> attackMode.getValue().equalsIgnoreCase("Switch")),
            range = new NumberValue("Attack Range", 3.8, 2.5, 6.0, 0.1),
            blockRange = new NumberValue("Block Range", 2.5, 2.5, 6.0, 0.1);

    private final MultiBoolValue targetSelections = new MultiBoolValue("Targets",
            new BoolValue("Players", true),
            new BoolValue("Monster", false),
            new BoolValue("Animals", false)
    );

    private final ModeValue attackTimingModeValue = new ModeValue("Attack Timing", new String[]{"Post", "Pre"}, "Pre");
    private final ModeValue blockTimingModeValue = new ModeValue("Block Timing", new String[]{"Post", "Pre"}, "Pre");

    private final ModeValue blockModeValue = new ModeValue("Block Mode", new String[]{"Watchdog", "Key"}, "Key");

    private boolean blocking;
    private boolean attacking, entityInBlockRange;

    @Override
    public void onDisable() {
        resetStatus();
        super.onDisable();
    }

    @Override
    public void onEnable() {
        resetStatus();
        super.onEnable();
    }

    private void resetStatus() {
        if (Minecraft.getMinecraft().thePlayer == null) return;

        if (isBlocking()) {
            doUnblock();
        }

        cleanTargets();
        resetKillAuraTimer(true, true);
        setIndex(0);
    }

    @EventTarget
    public void onTick(EventTick eventTick) {

        if (mc.theWorld == null || mc.thePlayer == null) return;

        if (getBlockingTarget() != null) {
            if (isEntityInBlockRange()) {
                doBlock();
            } else {
                if (isBlocking()) doUnblock();
            }
        } else doUnblock();
    }

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {
        double cpsMs = MathUtility.getRandomDouble(
                1000 / getCps().getValue() + MathUtility.getRandomDouble(5,10),
                1000 / getCps().getValue() - MathUtility.getRandomDouble(3,5));

        cleanTargets();

        getTargetsInWorld();

        if (getSwitchTimer().hasTimeElapsed(getSwitchDelay().getValue().intValue() * 100L) && getTargets().size() > 1) {
            resetKillAuraTimer(false, true);
            ++index;
        }

        if (getIndex() >= getTargets().size()) {
            setIndex(0);
        }

        if (!getTargets().isEmpty()) {
            setTarget(getTargets().get(getAttackMode().getValue().equalsIgnoreCase("Switch") ? getIndex() : 0));
        }


        switch (event.getType()) {
            case PRE -> {

                if (getTarget() != null) {
                    setAttacking(!getTargets().isEmpty());

                    setEntityInBlockRange(getAutoBlock().getValue() && PlayerUtility.isHoldingSword() &&
                            getBlockingTarget().getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= getBlockRange().getValue());



                    event.setYaw(RotationUtility.getRotationToEntity(getTarget())[0]);
                    event.setPitch(RotationUtility.getRotationToEntity(getTarget())[1]);

                    if (getAttackTimer().hasTimeElapsed(cpsMs) && Objects.equals(getAttackTimingModeValue().getValue(), "Pre")) {
                        doAttack();
                        resetKillAuraTimer(true, false);
                    }

                }

            }
            case POST -> {
                if (getTarget() != null) {
                    if (getAttackTimer().hasTimeElapsed(cpsMs) && Objects.equals(getAttackMode().getValue(), "Post")) {
                        doAttack();
                        resetKillAuraTimer(true, false);
                    }
                }
            }
        }
    }

    private void doAttack() {
        Minecraft.getMinecraft().thePlayer.swingItem();
        getPacketUtility().sendPacket(new C02PacketUseEntity(getTarget(), C02PacketUseEntity.Action.ATTACK));
    }

    private void doBlock(){
        setBlocking(true);

        switch (getBlockModeValue().getValue()) {
            case "Watchdog" -> {
                mc.thePlayer.itemInUseCount = 114514;
                if (mc.isSingleplayer()) return;
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
                PacketWrapper useItemOff = PacketWrapper.create(29, null, Via.getManager().getConnectionManager().getConnections().iterator().next());
                useItemOff.write(Type.VAR_INT, 1);
                com.viaversion.viarewind.utils.PacketUtil.sendToServer(useItemOff, Protocol1_8To1_9.class, true, true);

            }
            case "Key" -> {
                mc.gameSettings.keyBindUseItem.pressed = true;
            }

        }
    }

    private void doUnblock(){
        setBlocking(false);
        switch (getBlockModeValue().getValue()) {
            case "Watchdog" -> {
                mc.thePlayer.itemInUseCount = 0;
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
            case "Key" -> {
                mc.gameSettings.keyBindUseItem.pressed = false;
            }
        }
        mc.thePlayer.itemInUseCount = 0;
    }

    private void cleanTargets() {
        setTarget(null);
        getTargets().clear();
        setBlockingTarget(null);
    }

    private void resetKillAuraTimer(boolean attack, boolean switchTi) {
        if (attack) getAttackTimer().reset();
        if (switchTi) getSwitchTimer().reset();
    }

    public void getTargetsInWorld() {

        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase livingBase) {
                if (!valid(livingBase, false)) continue;
                getTargets().add(livingBase);
                break;
            }
        }


        if (!getTargets().isEmpty()) blockingTarget = getTargets().get(0);
    }

    private boolean valid(EntityLivingBase entity, boolean block) {
        if (!mc.thePlayer.isEntityAlive()
                || mc.thePlayer.isPlayerSleeping()
                || mc.thePlayer.isDead
                || mc.thePlayer.getHealth() <= 0
                || mc.thePlayer.getDistanceToEntity(entity) > (block ? blockRange.getValue() : range.getValue().floatValue())
                || !entity.isEntityAlive()
                || entity.isDead
                || entity.getHealth() <= 0
                || entity instanceof EntityArmorStand ||
                entity == mc.thePlayer) {
            return false;
        }
        if (entity instanceof EntityPlayer player) {
            if (!getTargetSelections().find("players").getValue()) return false;
            if (player.isPlayerSleeping()) return false;
        }

        if (entity instanceof EntityAnimal) {
            return getTargetSelections().find("animals").getValue();
        }

        if (entity instanceof EntityMob) {
            return getTargetSelections().find("monster").getValue();
        }
        return true;
    }
}
