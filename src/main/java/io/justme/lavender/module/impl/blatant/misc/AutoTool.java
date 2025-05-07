package io.justme.lavender.module.impl.blatant.misc;

import com.google.common.collect.Multimap;
import io.justme.lavender.La;
import io.justme.lavender.events.player.EventMotionUpdate;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.value.impl.BoolValue;
import net.lenni0451.asmevents.event.EventTarget;
import net.lenni0451.asmevents.event.enums.EnumEventType;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

import java.util.Iterator;

@ModuleInfo(name = "Auto Tool", category = Category.MISC, description = "Auto switch to the best tool for the job.")
public class AutoTool extends Module {

    private final BoolValue
            autoSword = new BoolValue("autoSword",false);

    private final BoolValue
            switchBack = new BoolValue("Switch Back",false);

    private boolean switched;

    private int previousSlot;

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {
        if (event.getType() == EnumEventType.PRE) {
            if (switchBack.getValue() && switched && previousSlot != -1) {
                mc.thePlayer.inventory.currentItem = previousSlot;
                previousSlot = -1;
                switched = false;
            }

            if (autoSword.getValue() && La.getINSTANCE().getModuleManager().getKillAura().getTarget() != null || (isPointedEntity() && mc.gameSettings.keyBindAttack.isKeyDown())) {
                double bestDamage = 1;
                int bestWeaponSlot = -1;

                for (int i = 36; i < 45; i++) {
                    final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                    if (stack != null) {
                        final double damage = getItemDamage(stack);
                        if (damage > bestDamage) {
                            bestDamage = damage;
                            bestWeaponSlot = i;
                        }
                    }
                }

                if (bestWeaponSlot != -1) {
                    mc.thePlayer.inventory.currentItem = bestWeaponSlot - 36;
                    previousSlot = mc.thePlayer.inventory.currentItem;
                    switched = false;
                }
            } else if (isPointedBlock() && mc.gameSettings.keyBindAttack.isKeyDown()) {
                final BlockPos blockPos = mc.objectMouseOver.getBlockPos();

                final Block block = mc.theWorld.getBlockState(blockPos).getBlock();

                double bestToolEfficiency = 1;
                int bestToolSlot = -1;

                for (int i = 36; i < 45; i++) {
                    final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                    if (stack != null && stack.getItem() instanceof ItemTool tool) {

                        final double eff = tool.getStrVsBlock(stack, block);

                        if (eff > bestToolEfficiency) {
                            bestToolEfficiency = eff;
                            bestToolSlot = i;
                        }
                    }
                }

                if (bestToolSlot != -1) {
                    previousSlot = mc.thePlayer.inventory.currentItem;
                    mc.thePlayer.inventory.currentItem = bestToolSlot - 36;
                    switched = true;
                }
            }
        }
    }

    private boolean isPointedEntity() {
        return mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit != null;
    }

    private boolean isPointedBlock() {
        return mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;
    }

    public double getItemDamage(final ItemStack stack) {
        double damage = 0.0;

        final Multimap<String, AttributeModifier> attributeModifierMap = stack.getAttributeModifiers();

        for (final String attributeName : attributeModifierMap.keySet()) {
            if (attributeName.equals("generic.attackDamage")) {
                final Iterator<AttributeModifier> attributeModifiers = attributeModifierMap.get(attributeName).iterator();
                if (attributeModifiers.hasNext())
                    damage += attributeModifiers.next().getAmount();
                break;
            }
        }

        if (stack.isItemEnchanted()) {
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25;
        }

        return damage;
    }

    @Override
    public void onEnable() {
        switched = false;
        previousSlot = -1;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (switched && previousSlot != -1) {
            mc.thePlayer.inventory.currentItem = previousSlot;
            previousSlot = -1;
            switched = false;
        }
        super.onDisable();
    }
}
