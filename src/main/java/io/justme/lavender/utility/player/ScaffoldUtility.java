package io.justme.lavender.utility.player;

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

    public BlockData getBlockData(BlockPos pos) {
        if (isPosValid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (isPosValid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (isPosValid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (isPosValid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (isPosValid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (isPosValid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos29 = pos.add(2, 0, 0);
        if (isPosValid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos39 = pos.add(0, 0, 2);
        if (isPosValid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos49 = pos.add(0, 0, -2);
        if (isPosValid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (isPosValid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (isPosValid(pos6.add(0, -1, 0))) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos6.add(-1, 0, 0))) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos6.add(1, 0, 0))) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos6.add(0, 0, 1))) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos6.add(0, 0, -1))) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (isPosValid(pos7.add(0, -1, 0))) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos7.add(-1, 0, 0))) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos7.add(1, 0, 0))) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos7.add(0, 0, 1))) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos7.add(0, 0, -1))) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (isPosValid(pos8.add(0, -1, 0))) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos8.add(-1, 0, 0))) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos8.add(1, 0, 0))) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos8.add(0, 0, 1))) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos8.add(0, 0, -1))) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (isPosValid(pos9.add(0, -1, 0))) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosValid(pos9.add(-1, 0, 0))) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosValid(pos9.add(1, 0, 0))) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosValid(pos9.add(0, 0, 1))) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosValid(pos9.add(0, 0, -1))) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }

    @Getter
    @Setter
    public class BlockData {
        public BlockPos pos;
        public EnumFacing facing;
        private Vec3 vec3;

        public BlockData(BlockPos pos, EnumFacing facing) {
            this.pos = pos;
            this.facing = facing;
        }

        public BlockPos getBlockPos() {
            return pos;
        }

        public EnumFacing getEnumFacing() {
            return facing;
        }

        public Vec3 getVec3() {
            double x = (double) pos.getX() + 0.5;
            double y = (double) pos.getY() + 0.5;
            double z = (double) pos.getZ() + 0.5;
            if (getFacing() == EnumFacing.UP || getFacing() == EnumFacing.DOWN) {
                x += randomNumber(0.3, -0.3);
                z += randomNumber(0.3, -0.3);
            } else {
                y += randomNumber(0.3, -0.3);
            }
            if (getFacing() == EnumFacing.WEST || getFacing() == EnumFacing.EAST) {
                z += randomNumber(0.3, -0.3);
            }
            if (getFacing() == EnumFacing.SOUTH || getFacing() == EnumFacing.NORTH) {
                x += randomNumber(0.3, -0.3);
            }
            return new Vec3(x, y, z);
        }
    }
}
