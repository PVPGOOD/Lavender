package io.justme.lavender.module.impl.blatant.movements;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import io.justme.lavender.La;
import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.*;
import io.justme.lavender.events.render.Event2DRender;
import io.justme.lavender.events.render.Event3DRender;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.network.PacketUtility;
import io.justme.lavender.utility.player.PlayerUtility;
import io.justme.lavender.utility.player.RotationUtility;
import io.justme.lavender.utility.player.ScaffoldUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JustMe.
 * @since 2024/4/6
 **/

@Getter
@Setter
@ModuleInfo(name = "Scaffold", description = "idk.", category = Category.MOVEMENTS)
public class Scaffold extends Module {

    public final ModeValue
            rotationModeValue = new ModeValue("Rotation", new String[]{"Normal"}, "Normal"),
            keepYModeValue = new ModeValue("keepY", new String[]{"Always", "None", "OnlySpeed"}, "None"),
            markMode = new ModeValue("Place Mark", new String[]{"Zeroday", "Basic"}, "Basic");

    private final NumberValue
            placeDelay = new NumberValue("place Delay (Ms)", 250, 0.0, 1000, 5),
            towerTimesValue = new NumberValue("tower Delay (Ms)", 8.0, 1.0, 15.0, 1.0);

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

    private BlockPos targetBlock;
    public ScaffoldUtility.PlaceData data;
    private boolean placing;
    private boolean canPlace = true;
    private int blocksPlaced;

    private double posY;
    private int towerTick;

    private ArrayList<Integer> validSlots = new ArrayList<>();
    private ItemStack inUsingItem;
    private boolean switchingBlock;
    private int realInUsingSlot;

    private PacketUtility packetUtility = new PacketUtility();
    private final TimerUtility placeTimer = new TimerUtility(), towerTimer = new TimerUtility();
    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void onEnable() {
        super.onEnable();
        if (Minecraft.getMinecraft().thePlayer == null) return;
        startedLowhop = false;
        realInUsingSlot = -1;
        setPosY(mc.thePlayer.posY);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        La.getINSTANCE().getHandlerManager().getRotationHandler().setRotating(false);
        if (Minecraft.getMinecraft().thePlayer == null) return;

        Minecraft.getMinecraft().playerController.syncCurrentPlayItem();
    }


    private boolean shouldPlaceBlock = false;
    @EventTarget
    public void onTick(EventTick event) {

        if (placeTimer.hasTimeElapsed(getPlaceDelay().getValue().longValue() + (long) ScaffoldUtility.randomNumber(10, 25), true)) {
            if (getData() != null && getInUsingItem() != null) {
                place(data.blockPos, data.facing, ScaffoldUtility.getVec3(data));
                shouldPlaceBlock = false;
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        var posX = mc.thePlayer.posX;
        var posZ = mc.thePlayer.posZ;
        var posY = getPosY();

        targetBlock = new BlockPos(posX, posY, posZ).offset(EnumFacing.DOWN);

        setData(ScaffoldUtility.getPlaceData(targetBlock));

        if (getData() == null || getData().blockPos == null) {
            La.getINSTANCE().print("no data");
            return;
        }

        validSlots.clear();
        for (int i = 0; i < 9; i++) {
            ItemStack hotbarItem = mc.thePlayer.inventory.mainInventory[i];
            if (hotbarItem != null && hotbarItem.stackSize > 2 && hotbarItem.getItem() instanceof ItemBlock) {
                validSlots.add(i);
            }
        }

        if (!validSlots.isEmpty()) {
            var slotShouldUseTo = validSlots.getFirst();

            if (realInUsingSlot != slotShouldUseTo) {
                realInUsingSlot = slotShouldUseTo;
                ItemStack hotbarItem = mc.thePlayer.inventory.mainInventory[slotShouldUseTo];
                setInUsingItem(hotbarItem);
                La.getINSTANCE().print("silent switch to " + slotShouldUseTo);
                packetUtility.sendFinalPacket(new C09PacketHeldItemChange(slotShouldUseTo));
                switchingBlock = true;
            }
        }

        if (getData() != null && getInUsingItem() != null) {
            shouldPlaceBlock = true;
        }
    }

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {
        if (getData() != null) {
            rotation(event);
            La.getINSTANCE().getHandlerManager().getRotationHandler().setRotating(true);
        } else {
            La.getINSTANCE().getHandlerManager().getRotationHandler().setRotating(false);
        }

        switch(event.getType()) {
            case PRE -> {

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

                            if (++towerTick <= towerTimesValue.getValue().intValue()) {

                                mc.thePlayer.jump();
//
//                                if (getBlockData() != null && getPositionValue().getValue()) {
//                                    mc.thePlayer.setPosition(blockData.getBlockPos().getX() + .5, mc.thePlayer.posY, blockData.getBlockPos().getZ() + .5);
//                                }

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
            case "Normal": {
                BlockPos underPos = new BlockPos(
                        MathHelper.floor_double(mc.thePlayer.posX),
                        MathHelper.floor_double(mc.thePlayer.posY - 0.1),
                        MathHelper.floor_double(mc.thePlayer.posZ)
                );

                var under = mc.theWorld.getBlockState(underPos).getBlock();

                if (under == Blocks.air || !under.isFullBlock()) {


                    var hitVec = ScaffoldUtility.getVec3(data);

                    if (lastHitVec == null || !lastHitVec.equals(hitVec)) {
                        hasSentPlacePacket = false;  // hitVec变了，允许重新发送放置包
                        lastHitVec = hitVec;
                    }


                    float[] rotations = RotationUtility.getRotationsToPosition(hitVec.xCoord, hitVec.yCoord, hitVec.zCoord);

                    float yaw = rotations[0];
                    float pitch = rotations[1];

                    pitch = Math.max(70, Math.min(90, pitch));

                    setYaw(yaw);
                    setPitch(pitch);
                }

                event.setYaw(getYaw());
                event.setPitch(getPitch());

                break;
            }
        }
    }

    @EventTarget
    public void onEntityAction(EventEntityAction event) {
        event.setSprinting(false);
    }
    private boolean startedLowhop;
    @EventTarget
    public void onMove(EventMove event) {
        if(!PlayerUtility.moving()) {
            startedLowhop = false;
        }

        if(mc.thePlayer.onGround) {
            if(PlayerUtility.moving()) {
                if(startedLowhop && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    if(!PlayerUtility.isAirOrLiquid(new BlockPos(mc.thePlayer.posX + event.getX(), mc.thePlayer.posY - 1, mc.thePlayer.posZ + event.getZ()))) {
                        event.setY(mc.thePlayer.motionY = 0.0005);
                    }
                } else {
                    PlayerUtility.jump(event);
                }
            }
        } else {
            if(event.getY() > 0.3) {
                startedLowhop = true;
                }
            }
    }

    private final Deque<Vec3> hitVecHistory = new LinkedList<>();

    @EventTarget
    public void on3D(Event3DRender event) {
        if (data == null || data.blockPos == null) return;

        RenderUtility.drawZeroDayMark();

        var player = mc.thePlayer;
        Vec3 eyePos = new Vec3(
                player.prevPosX + (player.posX - player.prevPosX) * event.getPartialTicks(),
                player.prevPosY + (player.posY - player.prevPosY) * event.getPartialTicks() + player.getEyeHeight(),
                player.prevPosZ + (player.posZ - player.prevPosZ) * event.getPartialTicks()
        );

        float yaw = getYaw();
        float pitch = getPitch();

        double maxDistance = 3.0;

        if (mc.gameSettings.thirdPersonView != 0) {
            Vec3 lookVec = getVectorForRotation(pitch, yaw);
            Vec3 targetVec = eyePos.addVector(lookVec.xCoord * maxDistance, lookVec.yCoord * maxDistance, lookVec.zCoord * maxDistance);
            var rayTraceResult = mc.theWorld.rayTraceBlocks(eyePos, targetVec, false, false, false);

            if (rayTraceResult != null && rayTraceResult.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                targetVec = rayTraceResult.hitVec;
            }

            RenderUtility.drawLine(eyePos, targetVec, 1.0f, 0.0f, 0.0f, 1.0f); // 红色线
        }

        var pos = data.blockPos;
        var block = mc.theWorld.getBlockState(pos).getBlock();
        var box = block.getSelectedBoundingBox(mc.theWorld, pos);

        RenderUtility.drawBox(box, 0.3f, 0.3f, 1f, 0.4f);

        var hitVec = ScaffoldUtility.getVec3(data);
        RenderUtility.drawPoint(hitVec, 0.0f, 1.0f, 0.0f, 1.0f);

        float alphaStep = 0.15f;
        int index = 0;
        for (Vec3 historyVec : hitVecHistory) {
            float alpha = 0.15f + index * alphaStep;
            RenderUtility.drawPoint(historyVec, 0.0f, 0.0f, 1.0f, alpha);
            index++;
        }
    }

    private boolean tryingToPlace = false;

    @EventTarget
    public void on2D(Event2DRender event) {
        if (getMarkMode().getValue().equals("Zeroday")) {
            if (data != null && data.blockPos != null) {

                var scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
                var font = La.getINSTANCE().getFontManager().getPingFang_Medium18();
                float x = scaledResolution.getScaledWidth() / 2f + 10;
                float y = scaledResolution.getScaledHeight() / 2f + 10;
                font.drawString("Facing: " + data.facing.name(), x, y, 0xFFFFFF);
                //trying
                font.drawString("Trying to place: " + (tryingToPlace ? "Yes" : "No"), x, y + 20, 0xFFFFFF);

            }
        }
    }


    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3(f1 * f2, f3, f * f2);
    }


    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getEnumEventType() == EnumEventType.OUTGOING) {

            if (eventPacket.getPacket() instanceof C09PacketHeldItemChange) {
                La.getINSTANCE().print("c09 packet");
            }
        }
    }


    private Vec3 lastHitVec = null;
    private boolean hasSentPlacePacket = false;

    private void place(BlockPos pos, EnumFacing facing, Vec3 hitVec) {
        // 判断 hitVec 是否变化
        if (lastHitVec == null || !lastHitVec.equals(hitVec)) {
            hasSentPlacePacket = false; // hitVec 改变，重置标志
            lastHitVec = hitVec;
        }

        // 尝试放置方块
        if (onPlayerRightClick(mc.thePlayer, mc.theWorld, getInUsingItem(), pos, facing, hitVec)) {
            mc.thePlayer.swingItem();
            placing = true;
            blocksPlaced++;

            hitVecHistory.addLast(hitVec);
            if (hitVecHistory.size() > 5) {
                hitVecHistory.removeFirst();
            }

            hasSentPlacePacket = true;
        }
    }


    public boolean onPlayerRightClick(EntityPlayerSP player, WorldClient worldIn, ItemStack originalHeldStack, BlockPos originalHitPos, EnumFacing originalSide, Vec3 originalHitVec)
    {

        var event = new EventRightClick(player, worldIn, originalHeldStack, originalHitPos, originalSide, originalHitVec);
        La.getINSTANCE().getEventManager().call(event);

        if (event.isCancelled()) {
            tryingToPlace = false;
            return false;
        }



        var heldStack = event.getHeldStack();
        var hitPos = event.getHitPos();
        var hitVec = event.getHitVec();
        var side = event.getSide();

        Minecraft.getMinecraft().playerController.syncCurrentPlayItem();
        float f = (float)(hitVec.xCoord - (double)hitPos.getX());
        float f1 = (float)(hitVec.yCoord - (double)hitPos.getY());
        float f2 = (float)(hitVec.zCoord - (double)hitPos.getZ());
        boolean flag = false;

        if (!this.mc.theWorld.getWorldBorder().contains(hitPos))
        {
            tryingToPlace = false;
            return false;
        }
        else
        {
            if (Minecraft.getMinecraft().playerController.currentGameType != WorldSettings.GameType.SPECTATOR)
            {
                IBlockState iblockstate = worldIn.getBlockState(hitPos);

                if ((!player.isSneaking() || player.getHeldItem() == null) && iblockstate.getBlock().onBlockActivated(worldIn, hitPos, iblockstate, player, side, f, f1, f2))
                {
                    tryingToPlace = false;
                    flag = true;
                }

                if (!flag && heldStack != null && heldStack.getItem() instanceof ItemBlock itemblock)
                {

                    if (!canPlaceBlockOnSide(itemblock,worldIn, hitPos, side, player, heldStack))
                    {
                        tryingToPlace = false;
                        return false;
                    }
                }
            }

            if (!hasSentPlacePacket) {
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(hitPos, side.getIndex(), player.inventory.getCurrentItem(), f, f1, f2));
                hasSentPlacePacket = true;
                tryingToPlace = true;
            }

            if (!flag && Minecraft.getMinecraft().playerController.currentGameType != WorldSettings.GameType.SPECTATOR)
            {
                if (heldStack == null)
                {
                    return false;
                }
                else if (Minecraft.getMinecraft().playerController.currentGameType.isCreative())
                {
                    int i = heldStack.getMetadata();
                    int j = heldStack.stackSize;
                    boolean flag1 = heldStack.onItemUse(player, worldIn, hitPos, side, f, f1, f2);
                    heldStack.setItemDamage(i);
                    heldStack.stackSize = j;
                    return flag1;
                }
                else
                {
                    return heldStack.onItemUse(player, worldIn, hitPos, side, f, f1, f2);
                }
            }
            else
            {
                return true;
            }
        }
    }

    public boolean canPlaceBlockOnSide(ItemBlock itemBlock,World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == Blocks.snow_layer)
        {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(side);
        }

        return canBlockBePlaced(itemBlock.getBlock(), pos, false, side, (Entity)null, stack);
    }

    public boolean canBlockBePlaced(Block blockIn, BlockPos pos, boolean ignoreCollision, EnumFacing side, Entity entityIn, ItemStack itemStackIn) {
        World world = Minecraft.getMinecraft().theWorld;
        Block existingBlock = world.getBlockState(pos).getBlock();
        AxisAlignedBB boundingBox = ignoreCollision ? null : blockIn.getCollisionBoundingBox(world, pos, blockIn.getDefaultState());

        boolean noEntityCollision = true;
        boolean isSpecialCase = existingBlock.getMaterial() == Material.circuits && blockIn == Blocks.anvil;
        boolean canReplace = existingBlock.getMaterial().isReplaceable() && blockIn.canReplace(world, pos, side, itemStackIn);

        return noEntityCollision && (isSpecialCase || canReplace);
    }

    public boolean checkNoEntityCollision(AxisAlignedBB bb, Entity entityIn)
    {
        List<Entity> list = getEntitiesWithinAABBExcludingEntity((Entity)null, bb);

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity = (Entity)list.get(i);

            if (!entity.isDead && entity.preventEntitySpawning && entity != entityIn && (entityIn == null || entityIn.ridingEntity != entity && entityIn.riddenByEntity != entity))
            {
                return false;
            }
        }

        return true;
    }

    public List<Entity> getEntitiesWithinAABBExcludingEntity(Entity entityIn, AxisAlignedBB bb)
    {
        return getEntitiesInAABBexcluding(entityIn, bb, EntitySelectors.NOT_SPECTATING);
    }

    public List<Entity> getEntitiesInAABBexcluding(Entity entityIn, AxisAlignedBB boundingBox, Predicate<? super Entity > predicate)
    {
        List<Entity> list = Lists.newArrayList();
        int i = MathHelper.floor_double((boundingBox.minX - 2.0D) / 10032);
        int j = MathHelper.floor_double((boundingBox.maxX + 2.0D) / 13002);
        int k = MathHelper.floor_double((boundingBox.minZ - 2.0D) / 13002);
        int l = MathHelper.floor_double((boundingBox.maxZ + 2.0D) / 10032);

        for (int i1 = i; i1 <= j; ++i1)
        {
            for (int j1 = k; j1 <= l; ++j1)
            {
                if (Minecraft.getMinecraft().theWorld.isChunkLoaded(i1, j1, true))
                {
                    Minecraft.getMinecraft().theWorld.getChunkFromChunkCoords(i1, j1).getEntitiesWithinAABBForEntity(entityIn, boundingBox, list, predicate);
                }
            }
        }

        return list;
    }



    private boolean canPlace() {
        return !getRayCast_Value().getValue() || raytrace();
    }

    private boolean raytrace() {
//        var movingObjectPosition = WorldUtility.raytrace(getYaw(),getPitch());
//
//        //合法的hitVec
//        if(movingObjectPosition != null && movingObjectPosition.sideHit == getBlockData().getFacing() && movingObjectPosition.getBlockPos().equals(getBlockData().getPos())) {
//           getBlockData().setVec3(movingObjectPosition.hitVec);
//            return true;
//        }

        return false;
    }

    @EventTarget
    public void onSafeWalk(EventSafeWalk eventSafeWalk) {
        eventSafeWalk.setCancel(getSafeWalkValue().getValue());
    }

}
