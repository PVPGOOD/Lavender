package io.justme.lavender.utility.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.Arrays;
import java.util.List;

/**
 * @author JustMe.
 * @since 2024/4/6
 **/

@Getter
@Setter
@UtilityClass
public class ScaffoldUtility {

    private final Minecraft mc = Minecraft.getMinecraft();

    public final List<Block> blacklisted = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava,
            Blocks.flowing_lava, Blocks.enchanting_table, Blocks.ender_chest, Blocks.yellow_flower, Blocks.carpet,
            Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.crafting_table,
            Blocks.snow_layer, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore,
            Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.gold_ore,
            Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.redstone_ore,
            Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate,
            Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.cactus,
            Blocks.lever, Blocks.activator_rail, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail,
            Blocks.furnace, Blocks.ladder, Blocks.oak_fence, Blocks.redstone_torch, Blocks.iron_trapdoor,
            Blocks.trapdoor, Blocks.tripwire_hook, Blocks.hopper, Blocks.acacia_fence_gate, Blocks.birch_fence_gate,
            Blocks.dark_oak_fence_gate, Blocks.jungle_fence_gate, Blocks.spruce_fence_gate, Blocks.oak_fence_gate,
            Blocks.dispenser, Blocks.sapling, Blocks.tallgrass, Blocks.deadbush, Blocks.web, Blocks.red_flower,
            Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.nether_brick_fence, Blocks.vine, Blocks.double_plant,
            Blocks.flower_pot, Blocks.beacon, Blocks.pumpkin, Blocks.lit_pumpkin);

    public int getBestBlockSlotHotBar() {
        int slot = -1;
        int size = 0;
        for (int i = 36; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item instanceof ItemBlock && isValid(item)) {
                    if (size < 1 && (is.stackSize > size)) {
                        size = is.stackSize;
                        slot = i;
                    }
                }
            }
        }
        return slot;
    }

    public int getBestBlockSlotInventory() {
        int slot = -1;
        int size = 0;
        for (int i = 9; i < 36; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item instanceof ItemBlock && isValid(item)) {
                    if (size < 1 && (is.stackSize > size)) {
                        size = is.stackSize;
                        slot = i;
                    }
                }
            }
        }
        return slot;
    }

    private boolean isPosValid(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isVisuallyOpaque() || block instanceof BlockLadder || block instanceof BlockCarpet
                || block instanceof BlockSnow || block instanceof BlockSkull)
                && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }

    public boolean isValid(Item item) {
        if (!(item instanceof ItemBlock iBlock)) {
            return false;
        } else {
            Block block = iBlock.getBlock();
            return !blacklisted.contains(block);
        }
    }

    public int getAllBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; i++) {
            blockCount += (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock) ? mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize : 0;
        }
        return blockCount;
    }

    public void swap(int currentSlot, int targetSlot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, currentSlot, targetSlot, 2, mc.thePlayer);
    }

    public double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    public int getSlot() {
        int slot = -1;
        int size = 0;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
            if (stack != null && stack.stackSize > 0 && stack.getItem() instanceof ItemBlock && ScaffoldUtility.isValid(( stack.getItem())) && size < stack.stackSize) {
                size = stack.stackSize;
                slot = i;
            }
        }
        return slot;
    }

    public PlaceData getPlaceData(final BlockPos pos) {
        EnumFacing[] facings = {EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.UP};

        // 1 of the 4 directions around player
        for (EnumFacing facing : facings) {
            final BlockPos blockPos = pos.add(facing.getOpposite().getDirectionVec());
            if (canBePlacedOn(blockPos)) {
                return new PlaceData(blockPos, facing);
            }
        }

        // 2 Blocks Under e.g. When jumping
        final BlockPos posBelow = pos.add(0, -1, 0);
        if (canBePlacedOn(posBelow)) {
            return new PlaceData(posBelow, EnumFacing.UP);
        }

        // 2 Block extension & diagonal
        for (EnumFacing facing : facings) {
            final BlockPos blockPos = pos.add(facing.getOpposite().getDirectionVec());
            for (EnumFacing facing1 : facings) {
                final BlockPos blockPos1 = blockPos.add(facing1.getOpposite().getDirectionVec());
                if (canBePlacedOn(blockPos1)) {
                    return new PlaceData(blockPos1, facing1);
                }
            }
        }
        return null;
    }

    public static boolean canBePlacedOn(final BlockPos blockPos) {
        final Material material = mc.theWorld.getBlockState(blockPos).getBlock().getMaterial();

        return (material.blocksMovement() && material.isSolid() && !(mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockAir));
    }


    public Vec3 getVec3(PlaceData data) {
        var pos = data.blockPos;
        var face = data.facing;

        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;

        x += face.getFrontOffsetX() / 2.0D;
        y += face.getFrontOffsetY() / 2.0D;
        z += face.getFrontOffsetZ() / 2.0D;

        double offsetRange = 0.04D;
        x += (Math.random() - 0.5) * 2 * offsetRange;
        y += (Math.random() - 0.5) * 2 * offsetRange;
        z += (Math.random() - 0.5) * 2 * offsetRange;

        return new Vec3(x, y, z);
    }


    @AllArgsConstructor
    public static class PlaceData {
        public BlockPos blockPos;
        public EnumFacing facing;
    }

}
