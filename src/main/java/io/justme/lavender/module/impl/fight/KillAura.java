package io.justme.lavender.module.impl.fight;

import com.mojang.authlib.GameProfile;
import com.viaversion.viarewind.protocol.v1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.Protocol1_8To1_9;
import com.viaversion.viaversion.protocols.v1_9_1to1_9_3.packet.ServerboundPackets1_9_3;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import io.justme.lavender.La;
import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.player.EventAttack;
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
import io.justme.lavender.utility.world.WorldUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.MultiBoolValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
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
import net.minecraft.world.WorldSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private EntityLivingBase target;
    public int index;

    private final BoolValue
            autoBlock = new BoolValue("AutoBlock",false),
            syncCurrentPlayItem = new BoolValue("SyncItem",false),
            attackTargetEntityWithCurrentItem = new BoolValue("AttackTargetEntityWithCurrentItem",false),
            ray_cast = new BoolValue("Attack_RayCast",false),
            ray_cast_block = new BoolValue("Block_RayCast",false);

    private final ModeValue
            attackMode = new ModeValue("Mode", new String[]{"Single", "Switch"}, "Switch");

    private final NumberValue
            cps = new NumberValue("CPS", 15.0, 1, 20.0, 1),
            switchDelay = new NumberValue("Switch Ms", 100.0, 0.1, 10000, 5, () -> attackMode.getValue().equalsIgnoreCase("Switch")),
            lockedRange = new NumberValue("Locked Range", 3.8, 2.5, 6.0, 0.1),
            range = new NumberValue("Attack Range", 3.8, 2.5, 6.0, 0.1),
            blockRange = new NumberValue("Block Range", 2.5, 2.5, 6.0, 0.1);

    private final MultiBoolValue targetSelections = new MultiBoolValue("Targets",
            new BoolValue("Players", true),
            new BoolValue("Monster", false),
            new BoolValue("Animals", false)
    );

    private final ModeValue blockTimingModeValue = new ModeValue("Block Timing", new String[]{"Post", "Pre","BeforeAttack","AfterAttack"}, "Pre");

    private final ModeValue blockModeValue = new ModeValue("Block Mode", new String[]{"Watchdog", "BlocksMC","Key","Visual"}, "Key");

    private boolean blocking;

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

        //正常重置状态
        if (isBlocking()) {
            doUnblock();
        }

        cleanTargets();
        resetKillAuraTimer(true, true);
        setIndex(0);
    }

    @EventTarget
    public void onTick(EventTick eventTick) {

    }

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {
        double cpsMs = MathUtility.getRandomDouble(
                1000 / getCps().getValue() + MathUtility.getRandomDouble(5,10),
                1000 / getCps().getValue() - MathUtility.getRandomDouble(3,5));

        getTargetsInWorld();
        if (!getTargets().isEmpty()) {
            getTargets().removeIf(
                    target -> mc.thePlayer.getDistanceToEntity(target) > ((double) lockedRange.getValue()));
        }

        if (!getTargets().isEmpty()) {
            switch (getAttackMode().getValue()) {
                case "Switch" -> {
                    if (getTargets().size() > 1) {
                        if (getSwitchTimer().hasTimeElapsed(getSwitchDelay().getValue().longValue(),true)) {
                            index++;
                        }
                    }

                    //重置 列表中的对象
                    if (getIndex() >= getTargets().size()) {
                        setIndex(0);
                    }

                    setTarget(getTargets().get(getIndex()));
                }
                case "Single" -> {
                    setIndex(0);
                    setTarget(getTargets().get(0));
                }
            }
        } else {
            setTarget(null);
        }

        if (getTarget() != null) {
            event.setYaw(RotationUtility.getRotationToEntity(getTarget())[0]);
            event.setPitch(RotationUtility.getRotationToEntity(getTarget())[1]);

            switch (event.getType()) {
                case PRE -> {

                    if (shouldAttack(cpsMs)) {

                        final var preAttack = new EventAttack(EnumEventType.PRE);
                        La.getINSTANCE().getEventManager().call(preAttack);

                        doAttack();

                        resetKillAuraTimer(true, false);
                        final var postAttack = new EventAttack(EnumEventType.POST);
                        La.getINSTANCE().getEventManager().call(postAttack);
                    }

                    if (getBlockTimingModeValue().getValue().equalsIgnoreCase("Pre")) {
                        if (shouldBlock()) {
                            doBlock();
                        }
                    }

                }

                case POST -> {
                    if (getBlockTimingModeValue().getValue().equalsIgnoreCase("Post")) {
                        if (shouldBlock()) {
                            doBlock();
                        }
                    }

                }
            }
        }

        //结尾工作
        if (shouldUnBlock()) {
            doUnblock();
        }
    }

    @EventTarget
    public void onAttack(EventAttack eventAttack) {

        switch (eventAttack.getTypes()) {
            case PRE -> {
                if (getBlockTimingModeValue().getValue().equalsIgnoreCase("BeforeAttack")) {
                    if (shouldBlock()) {
                        doBlock();
                    }
                }
            }

            case POST -> {
                if (getBlockTimingModeValue().getValue().equalsIgnoreCase("AfterAttack")) {
                    if (shouldBlock()) {
                        doBlock();
                    }
                }
            }
        }
    }

    private boolean shouldAttack(double cpsMs) {
        return getAttackTimer().hasTimeElapsed(cpsMs) &&
                !(getRay_cast().getValue() && inRayCast()) &&
                getAttackTimer().hasTimeElapsed(cpsMs) &&
                mc.thePlayer.getDistanceToEntity(target) < ((double) range.getValue().floatValue());
    }

    private boolean shouldBlock() {
        return getAutoBlock().getValue() &&
                getTarget().getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= getBlockRange().getValue() &&
                PlayerUtility.isHoldingSword() && !(getRay_cast().getValue() && inRayCast());
    }

    private boolean shouldUnBlock() {
        return getTarget() == null ?
                isBlocking() : (isBlocking() &&
                mc.thePlayer.getDistanceToEntity(getTarget()) > ((double) getBlockRange().getValue().floatValue()));
    }

    private boolean inRayCast() {
        return WorldUtility.ray_castEntity(getTarget(),
                RotationUtility.getRotationToEntity(getTarget())[0],
                RotationUtility.getRotationToEntity(getTarget())[1],
                RotationUtility.getRotationToEntity(getTarget())[0],
                RotationUtility.getRotationToEntity(getTarget())[1],
                getRange().getValue());
    }

    private void doAttack() {
        if (ViaLoadingBase.getInstance().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_8)) {
            mc.thePlayer.swingItem();
            attackPacket();
        } else {
            attackPacket();
            mc.thePlayer.swingItem();
        }
    }

    private void attackPacket() {
        if (getSyncCurrentPlayItem().getValue()) {
            Minecraft.getMinecraft().playerController.syncCurrentPlayItem();
        }

        getPacketUtility().sendPacketFromLa(new C02PacketUseEntity(getTarget(), C02PacketUseEntity.Action.ATTACK));

        if (Minecraft.getMinecraft().playerController.currentGameType != WorldSettings.GameType.SPECTATOR && getAttackTargetEntityWithCurrentItem().getValue())
        {
            Minecraft.getMinecraft().thePlayer.attackTargetEntityWithCurrentItem(getTarget());
        }
    }

    private void doBlock(){
        setBlocking(true);

        switch (getBlockModeValue().getValue()) {
            case "Watchdog" -> {
                mc.thePlayer.itemInUseCount = 1;
                if (mc.isSingleplayer()) return;

                if (ViaLoadingBase.getInstance().getTargetVersion().getVersion() > 47) {
                    PacketWrapper useItem = PacketWrapper.create(29, null, La.getINSTANCE().getUserConnection());
                    useItem.write(Types.VAR_INT, 1);
                    useItem.scheduleSendToServer(Protocol1_9To1_8.class);
                }

            }
            case "Key" -> mc.gameSettings.keyBindUseItem.pressed = true;
            case "Visual" ->  mc.thePlayer.itemInUseCount = 1;
        }
    }

    private void doUnblock(){
        setBlocking(false);

        switch (getBlockModeValue().getValue()) {
            case "Watchdog" -> {
                mc.thePlayer.itemInUseCount = 0;
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
            case "Key" -> mc.gameSettings.keyBindUseItem.pressed = false;
            case "Visual" ->  mc.thePlayer.itemInUseCount = 0;
        }
    }

    private void cleanTargets() {
        setTarget(null);
        getTargets().clear();
    }

    private void resetKillAuraTimer(boolean attack, boolean switchTi) {
        if (attack) getAttackTimer().reset();
        if (switchTi) getSwitchTimer().reset();
    }

    public void getTargetsInWorld() {
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase livingBase) {
                if (!valid(livingBase)) continue;
                getTargets().add(livingBase);
                break;
            }
        }
    }

    private boolean valid(EntityLivingBase entity) {
        if (!mc.thePlayer.isEntityAlive()
                || mc.thePlayer.isPlayerSleeping()
                || mc.thePlayer.isDead
                || mc.thePlayer.getHealth() <= 0
                || mc.thePlayer.getDistanceToEntity(entity) > ((double) lockedRange.getValue().floatValue())
                || !entity.isEntityAlive()
                || targets.contains(entity)
                || entity.isDead
                || entity.getHealth() <= 0
                || entity instanceof EntityArmorStand || entity == mc.thePlayer) {
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

    private List<String> getTablist() {
        return mc.getNetHandler().getPlayerInfoMap().stream()
                .map(NetworkPlayerInfo::getGameProfile)
                .filter(profile -> profile.getId() != mc.thePlayer.getUniqueID())
                .map(GameProfile::getName)
                .collect(Collectors.toList());
    }
}
