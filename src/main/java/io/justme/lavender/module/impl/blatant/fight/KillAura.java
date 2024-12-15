package io.justme.lavender.module.impl.blatant.fight;

import com.mojang.authlib.GameProfile;
import com.viaversion.viarewind.protocol.v1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ServerboundPackets1_9;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import io.justme.lavender.La;
import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.player.EventAttack;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.render.RotationUpdateEvent;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.interfaces.IMinecraft;
import io.justme.lavender.utility.math.MathUtility;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.network.PacketUtility;
import io.justme.lavender.utility.player.PlayerUtility;
import io.justme.lavender.utility.player.RaycastUtility;
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
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings;
import org.lwjglx.util.vector.Vector2f;

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

    //rotation
    public float[] rotating;
    public float[] lastRotation;
    public float tickingYaw, tickingPitch;

    private final TimerUtility attackTimer = new TimerUtility(), switchTimer = new TimerUtility();
    private final ArrayList<EntityLivingBase> targets = new ArrayList<>();
    private final PacketUtility packetUtility = new PacketUtility();
    private EntityLivingBase target;
    public int index;

    private final BoolValue
            autoBlock = new BoolValue("AutoBlock",false),
            syncCurrentPlayItem = new BoolValue("SyncItem",false),
            attackTargetEntityWithCurrentItem = new BoolValue("Atk Target With Item",false),
            ray_cast = new BoolValue("RayCast",false),
            ray_cast_block = new BoolValue("Block_RayCast",false);

    private final ModeValue
            attackMode = new ModeValue("Mode", new String[]{"Single", "Switch"}, "Switch");

    private final ModeValue
            rotationMode = new ModeValue("Rotation Mode", new String[]{"Normal", "Adaptive"}, "Normal");


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
        if (mc.thePlayer == null) return;

        lastRotation = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.renderYawOffset, mc.thePlayer.rotationPitch};
        resetStatus();
        super.onDisable();
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null) return;
        lastRotation = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.renderYawOffset, mc.thePlayer.rotationPitch};
        resetStatus();

        super.onEnable();
    }

    int curringSlot;
    private void resetStatus() {
        if (Minecraft.getMinecraft().thePlayer == null) return;

        //正常重置状态
        if (isBlocking()) {

            if (ViaLoadingBase.getInstance().getTargetVersion().getVersion() > 47) {
                if (La.getINSTANCE().getUserConnection() == null) {
                    La.getINSTANCE().print("UserConnection is null");
                } else {

                    int slot = mc.thePlayer.inventory.currentItem;

                    if (slot != getCurringSlot()) {
                        PacketWrapper switch0 = PacketWrapper.create(ServerboundPackets1_9.SET_CARRIED_ITEM, null, La.getINSTANCE().getUserConnection());
                        switch0.write(Types.SHORT, (short) (slot));
                        setCurringSlot(slot);
                        switch0.scheduleSendToServer(Protocol1_9To1_8.class);
                        La.getINSTANCE().print("set " + slot);
                        setCurringSlot(slot);
                    }

                }
            }
            doUnblock();
        }

        cleanTargets();
        resetKillAuraTimer(true, true);
        setIndex(0);
        La.getINSTANCE().getModuleManager().getRotations().setRotating(false);
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
            La.getINSTANCE().getModuleManager().getRotations().setRotating(true);
        }

        if (getTarget() != null) {
            onRotations(event,getTarget());

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

        getTargets().clear();
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

        if (ViaLoadingBase.getInstance().getTargetVersion().getVersion() > 47) {
            if (La.getINSTANCE().getUserConnection() == null) {
                La.getINSTANCE().print("UserConnection is null");
            } else {
                PacketWrapper wrapper = PacketWrapper.create(ServerboundPackets1_9.INTERACT, null, La.getINSTANCE().getUserConnection());
                wrapper.write(Types.VAR_INT, getTarget().getEntityId());
                wrapper.write(Types.VAR_INT,1);
                wrapper.scheduleSendToServer(Protocol1_9To1_8.class);
            }
        }



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

                    int slot = mc.thePlayer.inventory.currentItem;

                    if (slot != getCurringSlot()) {
                        La.getINSTANCE().print("normal now ab");
                        getPacketUtility().sendPacketFromLa(new C09PacketHeldItemChange(slot));
                        setCurringSlot(slot);
                    }


                    if (La.getINSTANCE().getUserConnection() == null) {
                        La.getINSTANCE().print("UserConnection is null");
                    } else {

                        PacketWrapper useItem = PacketWrapper.create(ServerboundPackets1_9.USE_ITEM, null, La.getINSTANCE().getUserConnection());
                        useItem.write(Types.VAR_INT, 1);
                        useItem.scheduleSendToServer(Protocol1_9To1_8.class);

                        if (mc.thePlayer.onGround) {
                            getPacketUtility().sendFinalPacket(new C08PacketPlayerBlockPlacement(
                                    new BlockPos(-1, -1, -1),
                                    255, mc.thePlayer.getCurrentEquippedItem(),
                                    0, 0, 0
                            ));
                            La.getINSTANCE().print("blocking");
                        }
                    }
                } else {
                    getPacketUtility().sendFinalPacket(new C08PacketPlayerBlockPlacement(
                            new BlockPos(-1, -1, -1),
                            255, mc.thePlayer.getCurrentEquippedItem(),
                            0, 0, 0
                    ));
                }

            }
            case "Key" -> mc.gameSettings.keyBindUseItem.pressed = true;
            case "Visual" ->  mc.thePlayer.itemInUseCount = 1;
        }
    }


    private TimerUtility setSlotTimerUtility = new TimerUtility();
    private void doUnblock(){
        setBlocking(false);

        switch (getBlockModeValue().getValue()) {
            case "Watchdog" -> {
                mc.thePlayer.itemInUseCount = 0;

                int slot = mc.thePlayer.inventory.currentItem;

                if (ViaLoadingBase.getInstance().getTargetVersion().getVersion() > 47) {
                    if (La.getINSTANCE().getUserConnection() == null) {
                        La.getINSTANCE().print("UserConnection is null");
                    } else {
                        PacketWrapper switch0 = PacketWrapper.create(ServerboundPackets1_9.SET_CARRIED_ITEM, null, La.getINSTANCE().getUserConnection());
                        switch0.write(Types.SHORT, (short) (8));
                        setCurringSlot(8);
                        switch0.scheduleSendToServer(Protocol1_9To1_8.class);
                                    La.getINSTANCE().print("set 47");
            //
//                PacketWrapper switch1 = PacketWrapper.create(ServerboundPackets1_9.SET_CARRIED_ITEM, null, La.getINSTANCE().getUserConnection());
//                switch1.write(Types.SHORT, (short) slot);
//                switch1.scheduleSendToServer(Protocol1_9To1_8.class);
                    }
                } else {
                    getPacketUtility().sendPacketFromLa(new C09PacketHeldItemChange(8));
                    setCurringSlot(8);
                    La.getINSTANCE().print("set");

//                    if (getSetSlotTimerUtility().hasTimeElapsed(1000)) {
//                        getSetSlotTimerUtility().reset();
//                        La.getINSTANCE().print("set");
//                        getPacketUtility().sendPacketFromLa(new C09PacketHeldItemChange(slot));
//                    }
                }


                La.getINSTANCE().print("reset");
            }
            case "Key" -> mc.gameSettings.keyBindUseItem.pressed = false;
            case "Visual" ->  mc.thePlayer.itemInUseCount = 0;
        }
    }

    public void onRotations(EventMotionUpdate event, EntityLivingBase target) {
        switch (getRotationMode().getValue()) {
            case "Normal":
                rotating = RotationUtility.getRotationToEntity(target);
                break;
            case "Adaptive":
                rotating = new float[]{RotationUtility.calculate(target, true, range.getValue()).getX(), Math.min(RotationUtility.calculate(target, true, range.getValue()).getY(), 90)};
                randomiseTargetRotations();
                rotating[0] += tickingYaw;
                rotating[1] += tickingPitch;
                if (RaycastUtility.rayCast(new Vector2f(rotating[0], rotating[1]), range.getValue()).typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
                    tickingYaw = tickingPitch = 0;
                }
                break;
        }

        lastRotation[0] = rotating[0];
        lastRotation[2] = Math.min(90, rotating[1]);

        La.getINSTANCE().getModuleManager().getRotations().setRotating(true);
        event.setYaw(lastRotation[0]);
        event.setPitch(lastRotation[2]);
    }

    public void randomiseTargetRotations() {
        tickingYaw += (float) (Math.random() - 0.5f);
        tickingPitch += (float) (Math.random() - 0.5f) * 2;
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
