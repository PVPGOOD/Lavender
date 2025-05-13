package io.justme.lavender.events.player;

import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.IEvent;
import net.lenni0451.asmevents.event.wrapper.CancellableEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

/**
 * @author JustMe.
 * @since 2025/5/13
 **/
@Getter
@Setter
public class EventRightClick extends CancellableEvent implements IEvent {
    public EntityPlayerSP player;
    public WorldClient worldIn;
    public ItemStack heldStack;
    public BlockPos hitPos;
    public EnumFacing side;
    public Vec3 hitVec;

    public EventRightClick(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 hitVec) {
        this.player = player;
        this.worldIn = worldIn;
        this.heldStack = heldStack;
        this.hitPos = hitPos;
        this.side = side;
        this.hitVec = hitVec;
    }
}
