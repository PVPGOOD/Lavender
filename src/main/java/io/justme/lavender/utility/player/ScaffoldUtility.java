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


    public BlockData getBlockData(double y) {
        final BlockPos belowBlockPos = new BlockPos(mc.thePlayer.posX, y - 1, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(belowBlockPos).getBlock() instanceof BlockAir) {
            for (int x = 0; x < 4; x++) {
                for (int z = 0; z < 4; z++) {
                    for (int i = 1; i > -3; i -= 2) {
                        final BlockPos blockPos = belowBlockPos.add(x * i, 0, z * i);
                        if (mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockAir) {
                            for (EnumFacing direction : EnumFacing.values()) {
                                final BlockPos block = blockPos.offset(direction);
                                final Material material = mc.theWorld.getBlockState(block).getBlock().getMaterial();
                                if (material.isSolid() && !material.isLiquid()) {
                                    return new BlockData(block, direction.getOpposite());
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
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

    private Vec3 getVec3ByBlockData(BlockData data) {
        BlockPos pos = data.getBlockPos();
        double rand = .5 + (PlayerUtility.moving() ? (1) : 0);
        double x = pos.getX() + rand;
        double y = pos.getY() + rand;
        double z = pos.getZ() + rand;
        return new Vec3(x, y, z);
    }



    public void swap(int currentSlot, int targetSlot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, currentSlot, targetSlot, 2, mc.thePlayer);
    }

    @Getter
    @Setter
    public class BlockData {
        private final BlockPos pos;
        private final EnumFacing facing;

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
    }
}
