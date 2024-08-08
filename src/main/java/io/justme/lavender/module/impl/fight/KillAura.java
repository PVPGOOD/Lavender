package io.justme.lavender.module.impl.fight;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
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
    private int index;

    private final BoolValue
            autoBlock = new BoolValue("AutoBlock",false),
            syncCurrentPlayItem = new BoolValue("SyncItem",false),
            attackTargetEntityWithCurrentItem = new BoolValue("AttackTargetEntityWithCurrentItem",false),
            ray_cast = new BoolValue("Ray cast",false);

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

    private final ModeValue blockTimingModeValue = new ModeValue("Block Timing", new String[]{"Post", "Pre","BeforeAttack","AfterAttack"}, "Pre");

    private final ModeValue blockModeValue = new ModeValue("Block Mode", new String[]{"Watchdog", "BlocksMC","Key","Visual"}, "Key");

    private boolean attacking,blocking;

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
            La.getINSTANCE().print(String.format("unBlock (%s)" , "resetStatus"));
            doUnblock();
        }

        cleanTargets();
        resetKillAuraTimer(true, true);
        setIndex(0);
    }

    @EventTarget
    public void onTick(EventTick eventTick) {

        if (mc.theWorld == null || mc.thePlayer == null) return;

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

        if (getTarget() != null) {
            event.setYaw(RotationUtility.getRotationToEntity(getTarget())[0]);
            event.setPitch(RotationUtility.getRotationToEntity(getTarget())[1]);

            var shouldAttack = getAttackTimer().hasTimeElapsed(cpsMs);
            setAttacking(shouldAttack);

            var shouldBlock = getAutoBlock().getValue() && getTarget().getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= getBlockRange().getValue() && PlayerUtility.isHoldingSword();

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
                        if (shouldBlock) {
                            La.getINSTANCE().print(String.format("Block (%s)" , getBlockTimingModeValue().getValue()));
                            doBlock();
                        }
                    }

                }

                case POST -> {

                    if (getBlockTimingModeValue().getValue().equalsIgnoreCase("Post")) {
                        if (shouldBlock) {
                            La.getINSTANCE().print(String.format("Block (%s)" , getBlockTimingModeValue().getValue()));
                            doBlock();
                        }
                    }

                }
            }
        } else {
            if (isBlocking()) {
                La.getINSTANCE().print(String.format("unBlock (%s)" , "on the end"));
                doUnblock();
            }
        }
    }

    @EventTarget
    public void onAttack(EventAttack eventAttack) {

        var shouldBlock = getAutoBlock().getValue() && getTarget().getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= getBlockRange().getValue() && PlayerUtility.isHoldingSword();

        switch (eventAttack.getTypes()) {
            case PRE -> {
                La.getINSTANCE().print(String.format("Attack (%s)" ,eventAttack.getTypes()));

                if (getBlockTimingModeValue().getValue().equalsIgnoreCase("BeforeAttack")) {
                    if (shouldBlock) {
                        La.getINSTANCE().print(String.format("Block (%s)" , getBlockTimingModeValue().getValue()));
                        doBlock();
                    }
                }
            }

            case POST -> {
                La.getINSTANCE().print(String.format("Attack (%s)" ,eventAttack.getTypes()));

                if (getBlockTimingModeValue().getValue().equalsIgnoreCase("AfterAttack")) {
                    if (shouldBlock) {
                        La.getINSTANCE().print(String.format("Block (%s)" , getBlockTimingModeValue().getValue()));
                        doBlock();
                    }
                }
            }
        }
    }

    private boolean shouldAttack(double cpsMs) {
        return getAttackTimer().hasTimeElapsed(cpsMs) && (!getRay_cast().getValue() || WorldUtility.ray_castEntity(getTarget(),
                RotationUtility.getRotationToEntity(getTarget())[0],
                RotationUtility.getRotationToEntity(getTarget())[1],
                RotationUtility.getRotationToEntity(getTarget())[0],
                RotationUtility.getRotationToEntity(getTarget())[1],
                getRange().getValue()));
    }

    private void doAttack() {
        if (ViaLoadingBase.getInstance().getTargetVersion().isOlderThanOrEqualTo(ProtocolVersion.v1_8)) {
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

        getPacketUtility().sendPacket(new C02PacketUseEntity(getTarget(), C02PacketUseEntity.Action.ATTACK));

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

                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
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
                || mc.thePlayer.getDistanceToEntity(entity) > ((double) range.getValue().floatValue())
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
