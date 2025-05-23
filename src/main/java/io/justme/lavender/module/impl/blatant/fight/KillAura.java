package io.justme.lavender.module.impl.blatant.fight;

import com.viaversion.viarewind.protocol.protocol1_8to1_9.Protocol1_8To1_9;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Type;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import io.justme.lavender.La;
import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.player.EventAttack;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.events.render.Event3DRender;
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
import io.justme.lavender.utility.player.ValidEntityUtility;
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
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.WorldSettings;
import org.lwjgl.opengl.GL11;
import org.lwjglx.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;

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
    public int index, blockTicks;
    public boolean blinked;
    private boolean blocking;

    private EntityLivingBase target;

    private final BoolValue
            autoBlock = new BoolValue("AutoBlock",false),
            syncCurrentPlayItem = new BoolValue("SyncItem",false),
            attackTargetEntityWithCurrentItem = new BoolValue("atkWithItem",false),
            ray_cast = new BoolValue("RayCast",false),
            ray_cast_block = new BoolValue("Block_RayCast",false);

    private final ModeValue
            switchMode = new ModeValue("Switch Mode", new String[]{"Single", "Switch"}, "Switch");

    private final ModeValue
            rotationMode = new ModeValue("Rotation Mode", new String[]{"Normal", "Adaptive"}, "Normal");

    private final ModeValue autoblockModeValue = new ModeValue("AutoBlock Mode", new String[]{"Watchdog", "BlocksMC","Legit","Visual"}, "Legit");
    private final ModeValue blockTimingModeValue = new ModeValue("AutoBlock Timing", new String[]{"Post", "Pre"}, "Pre");

    private final ModeValue
            blockMode = new ModeValue("Block Mode", new String[]{"Full","Half"}, "Half");

    private final NumberValue
            cps = new NumberValue("CPS", 15.0, 1, 20.0, 1),
            switchDelay = new NumberValue("Switch Ms", 100.0, 0.1, 10000, 5, () -> switchMode.getValue().equalsIgnoreCase("Switch")),
            lockedRange = new NumberValue("Locked Range", 3.8, 2.5, 6.0, 0.1),
            range = new NumberValue("Attack Range", 3.8, 2.5, 6.0, 0.1),
            blockRange = new NumberValue("Block Range", 2.5, 2.5, 6.0, 0.1);

    public int blinkTicks;

    private final MultiBoolValue targetSelections = new MultiBoolValue("Targets",
            new BoolValue("Players", true),
            new BoolValue("Monster", false),
            new BoolValue("Animals", false)
    );

    @Override
    public void onDisable() {
        if (mc.thePlayer == null) return;
        resetStatus();
        super.onDisable();
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null) return;
        resetStatus();

        super.onEnable();
    }


    private void resetStatus() {

        cleanTargets();
        resetKillAuraTimer(true, true);
        setIndex(0);

        if (isBlocking()) {
            doUnblock();
        }

        mc.thePlayer.itemInUseCount = 0;
    }

    @EventTarget
    public void onTick(EventTick eventTick) {

    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        double cpsMs = MathUtility.getRandomDouble(
                1000 / getCps().getValue() + MathUtility.getRandomDouble(5,10),
                1000 / getCps().getValue() - MathUtility.getRandomDouble(3,5));

        getTargetsInWorld();

        if (!getTargets().isEmpty()) {
            getTargets().removeIf(
                    target -> mc.thePlayer.getDistanceToEntity(target) > ((double) lockedRange.getValue()));
        }

        if (!getTargets().isEmpty()) {
            blockTicks++;

            switch (getSwitchMode().getValue()) {
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
                    setTarget(getTargets().getFirst());
                }
            }
        } else {
            if (shouldUnBlock()) {
                doUnblock();
            }
            setTarget(null);
        }

        if (getTarget() != null) {
            executeAttack(cpsMs);
        } else {
            if (shouldUnBlock()) {
                doUnblock();
            }
        }

        //结尾工作
        if (shouldUnBlock()) {
            doUnblock();
        }

        getTargets().clear();
    }

    @EventTarget
    public void onMotion(EventMotionUpdate eventMotionUpdate) {
        if (mc.thePlayer == null || getTarget() == null)  {
            return;
        }

        onRotations(eventMotionUpdate,getTarget());
        mc.thePlayer.itemInUseCount = isBlocking() ? 1 : 0;
    }

    @EventTarget
    public void onAttack(EventAttack eventAttack) {
        if (getBlockTimingModeValue().getValue().equalsIgnoreCase(eventAttack.getTypes().name())) {
            if (shouldBlock()) {
                executeBlockOrUnblock();
            }
        }
    }

    private void executeAttack(double cpsMs) {
        if (shouldAttack(cpsMs)) {
            final var preAttack = new EventAttack(EnumEventType.PRE);
            La.getINSTANCE().getEventManager().call(preAttack);

            doAttack();

            resetKillAuraTimer(true, false);
            final var postAttack = new EventAttack(EnumEventType.POST);
            La.getINSTANCE().getEventManager().call(postAttack);
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
                PlayerUtility.isHoldingSword() &&
                !(getRay_cast().getValue() && inRayCast());
    }

    private boolean shouldUnBlock() {
        return getTarget() == null ? isBlocking() : (isBlocking() && mc.thePlayer.getDistanceToEntity(getTarget()) > ((double) getBlockRange().getValue()));
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

        getPacketUtility().sendFinalPacket(new C02PacketUseEntity(getTarget(), C02PacketUseEntity.Action.ATTACK));

        if (Minecraft.getMinecraft().playerController.currentGameType != WorldSettings.GameType.SPECTATOR && getAttackTargetEntityWithCurrentItem().getValue())
        {
            Minecraft.getMinecraft().thePlayer.attackTargetEntityWithCurrentItem(getTarget());
        }
    }

    private void executeBlockOrUnblock() {

        switch (getBlockMode().getValue()) {
            case "Full" -> {
                if (shouldBlock()) {
                    doBlock();
                }
            }
            case "Half" -> {
                if (shouldBlock()) {
                    doBlock();
                }

                if (getBlockTicks() % 3 != 0 && isBlocking()) {
                    doUnblock();
                }
            }
        }

    }


    private void doBlock(){
        setBlocking(true);

        La.getINSTANCE().print("block");
        switch (getAutoblockModeValue().getValue()) {
            case "Watchdog" -> {
                getPacketUtility().sendPacketFromLa(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT));

                if (ViaLoadingBase.getInstance().getTargetVersion().getVersion() > 47) {

                    if (La.getINSTANCE().getUserConnection() == null) {
                        La.getINSTANCE().print("UserConnection is null");
                    } else {
                        getPacketUtility().sendPacketFromLa(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                        PacketWrapper useItem = PacketWrapper.create(29, null, Via.getManager().getConnectionManager().getConnections().iterator().next());
                        useItem.write(Type.VAR_INT, 1);
                        com.viaversion.viarewind.utils.PacketUtil.sendToServer(useItem, Protocol1_8To1_9.class, true, true);
                    }
                }

            }

            case "Legit" -> mc.gameSettings.keyBindUseItem.pressed = true;
            case "Visual" ->  mc.thePlayer.itemInUseCount = 1;
        }
    }


    private void doUnblock(){
        La.getINSTANCE().print("unBlock","AutoBlock");
        setBlocking(false);

        switch (getAutoblockModeValue().getValue()) {
            case "Watchdog" -> {
                if (ViaLoadingBase.getInstance().getTargetVersion().getVersion() > 47) {
                    if (La.getINSTANCE().getUserConnection() == null) {
                        La.getINSTANCE().print("UserConnection is null");
                    } else {
                        getPacketUtility().sendPacketFromLa(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                }
            }
            case "Legit" -> mc.gameSettings.keyBindUseItem.pressed = false;
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

        if (lastRotation == null) {
            lastRotation = new float[]{event.getYaw(), mc.thePlayer.renderYawOffset,event.getPitch()};
        }

        lastRotation[0] = rotating[0];
        lastRotation[2] = Math.min(90, rotating[1]);

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


        if (ValidEntityUtility.isOnSameTeam(entity)) return false;

        if (!ValidEntityUtility.getTablist().contains(entity.getName())) {
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


    @EventTarget
    public void on3D(Event3DRender event3DRender) {
        if (target == null || mc.theWorld == null) return;

        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (entity == target) {
                int color = new Color(255,0,0, 64).getRGB();

                drawBox(entity, color);
            }
        }
    }


    private void drawBox(Entity entity, int color) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosX();
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosY();
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosZ();

        var box = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x, y, z);

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);

        drawFilledBox(box, color);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1.5F);
        drawBoxOutline(box, color);

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private void drawBoxOutline(AxisAlignedBB box, int color) {
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        float alpha = (color >> 24 & 255) / 255.0F;

        GL11.glColor4f(red, green, blue, alpha);

        var tessellator = Tessellator.getInstance();
        var worldRenderer = tessellator.getWorldRenderer();

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();

        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();

        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();

        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();

        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();

        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();

        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();

        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();

        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();

        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();

        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();

        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();

        tessellator.draw();
    }

    public void drawFilledBox(AxisAlignedBB box, int color) {
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        float alpha = (color >> 24 & 255) / 255.0F;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glColor4f(red, green, blue, alpha);

        Tessellator tessellator = Tessellator.getInstance();
        var worldRenderer = tessellator.getWorldRenderer();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();

        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();

        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();

        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();

        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();

        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();

        tessellator.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
