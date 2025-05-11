package io.justme.lavender.module.impl.blatant.world;

import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.ModeValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * @author JustMe.
 * @since 2025/5/11
 **/

@Getter
@ModuleInfo(name = "FastMine", description = "AntiVoid.", category = Category.World)
public class FastMine extends Module {

    private BlockPos blockPos;
    private EnumFacing facing;
    private boolean digging;
    private float damage;


    private final ModeValue
            mode = new ModeValue("Mode", new String[]{"Packet","Legit"}, "Packet");



    @EventTarget
    public void onUpdate(EventMotionUpdate event) {

        var mc = Minecraft.getMinecraft();

        if (event.getType() == EnumEventType.POST) {

            switch (mode.getValue()) {
                case "Packet" -> {
                    mc.playerController.blockHitDelay = 0;
                    if (mc.playerController.curBlockDamageMP >= .65) {
                        mc.playerController.curBlockDamageMP = 1;
                    }
                }

                case "Legit" -> {
                    mc.playerController.blockHitDelay = 0;
                    if (digging && !mc.playerController.isInCreativeMode()) {
                        Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                        damage += block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, blockPos) * 1.4f;
                        if (damage >= 1.0f) {
                            mc.theWorld.setBlockState(blockPos, Blocks.air.getDefaultState(), 11);
                            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, facing));
                            damage = 0.0f;
                            digging = false;
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    public void onPacket(EventPacket event) {

        var p = event.getPacket();

        if (event.getEnumEventType() == EnumEventType.OUTGOING) {
            switch (mode.getValue()) {
                case "Packet" -> {
                    if (p instanceof C07PacketPlayerDigging c07PacketPlayerDigging && !Minecraft.getMinecraft().playerController.isInCreativeMode()) {
                        if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                            digging = true;
                            blockPos = c07PacketPlayerDigging.getPosition();
                            facing = c07PacketPlayerDigging.getFacing();
                            damage = 0.0f;
                        } else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                            digging = false;
                            blockPos = null;
                            facing = null;
                        }
                    }
                }
            }
        }
    }
}
