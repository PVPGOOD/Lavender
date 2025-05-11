package io.justme.lavender.module.impl.blatant.visual;

import io.justme.lavender.La;
import io.justme.lavender.events.render.Event3DRender;
import io.justme.lavender.events.render.EventNameRender;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.player.ValidEntityUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.NumberRangeValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;


@Getter
@ModuleInfo(name = "NameTag", description = "IDK.", category = Category.VISUAL)
public class NameTag extends Module {

    private final BoolValue showDistance = new BoolValue("Show Distance", true);
    private final BoolValue showHealth = new BoolValue("Show Health", true);
    private final BoolValue showArmor = new BoolValue("Show Armor", true);
    private final NumberRangeValue scaleValue = new NumberRangeValue("Scale Size", 0.001D, 1, 0.001D, 1,0.001);
    private final NumberValue backgroundAlpha = new NumberValue("Background Alpha", 200, 32, 255, 1);

    @EventTarget
    public void onRenderNameTag(EventNameRender event) {
        if (event.getEntity() instanceof EntityPlayer) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onRender3DEvent(Event3DRender event) {
        if (mc.theWorld == null || mc.thePlayer == null || mc.theWorld.getLoadedEntityList().isEmpty() || mc.theWorld.playerEntities.isEmpty())
            return;

        for (EntityPlayer entity : mc.theWorld.playerEntities) {

            final double yOffset = entity.isSneaking() ? -0.25 : 0.0;

            final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosX();
            final double posY = (entity.lastTickPosY + yOffset) + ((entity.posY + yOffset) - (entity.lastTickPosY + yOffset)) * mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosY();
            final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosZ();

            mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);

            renderNameTag(entity, posX, posY, posZ, event.getPartialTicks());
        }
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double) delta;
    }
    private int getDisplayColour(EntityPlayer player) {
        int colour = new Color(0xFFFFFF).getRGB();

        if (player.isInvisible()) {
            colour = -1113785;
        }

        return colour;
    }

    private String getDisplayName(EntityLivingBase entity) {
        String drawTag = entity.getDisplayName().getFormattedText();
        EnumChatFormatting color;

        if ((int) entity.getHealth() >= 6.0) {
            color = EnumChatFormatting.GREEN;
        } else if ((int) entity.getHealth() >= 2.0) {
            color = EnumChatFormatting.YELLOW;
        } else {
            color = EnumChatFormatting.RED;
        }

        StringBuilder tagBuilder = new StringBuilder();

        if (!ValidEntityUtility.isOnSameTeam(entity)) {
            tagBuilder.append(EnumChatFormatting.AQUA).append("[").append("Team").append("] ").append(EnumChatFormatting.RESET);
        }

        if (showDistance.getValue()) {
            tagBuilder.append("[").append((int) entity.getDistanceToEntity(mc.thePlayer)).append("m] ");
        }

        tagBuilder.append(drawTag);

        if (showHealth.getValue()) {
            tagBuilder.append(" ").append(color).append((int) entity.getHealth());
        }

        drawTag = tagBuilder.toString();
        return drawTag;
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y + 0.7D;

        Entity camera = mc.getRenderViewEntity();
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;

        setupCameraAndTransform(camera, delta, x, tempY + 1.4F, z);

        double distance = camera.getDistance(x + mc.getRenderManager().viewerPosX, y + mc.getRenderManager().viewerPosY, z + mc.getRenderManager().viewerPosZ);
        var font = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        float width = font.getStringWidth(getDisplayName(player)) / 2F;

        double scale = calculateScale(distance);
        GlStateManager.scale(-scale, -scale, scale);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        RenderUtility.drawRoundRect(
                -width - 2,
                -12,
                font.getStringWidth(getDisplayName(player)) + 5,
                font.getHeight(),
                8,
                new Color(0, 0, 0, getBackgroundAlpha().getValue().intValue()));

        font.drawString(getDisplayName(player), -width, -9.5F, getDisplayColour(player));

        if (getShowArmor().getValue()) {
            renderArmor(player);
        }

        resetCameraPosition(camera, originalPositionX, originalPositionY, originalPositionZ);
    }


    private void renderArmor(EntityPlayer player) {
        int yPos = -40;

        int armorCount = 0;
        for (int i = 3; i >= 0; --i) {
            if (player.inventory.armorInventory[i] != null) {
                armorCount++;
            }
        }
        boolean hasHeld = player.getHeldItem() != null;
        if (armorCount == 0) return;
        int totalCount = armorCount + (hasHeld ? 1 : 0);

        int halfWidth = totalCount * 8 + 20;
        int rectX = -halfWidth;
        int rectWidth = totalCount * 16 + 32;

        int maxLines = 0;
        ItemStack heldItem = player.getHeldItem();
        if (heldItem != null) {
            maxLines = Math.max(maxLines, getMaxLines(heldItem));
        }
        for (int i = 3; i >= 0; --i) {
            ItemStack armor = player.inventory.armorInventory[i];
            if (armor != null) {
                maxLines = Math.max(maxLines, getMaxLines(armor));
            }
        }
        int rectHeight = 30 + (maxLines - 1) * 8;
        switch (maxLines) {
            case 0 -> {
                yPos -= 0 + 15;
            }

            case 1 -> {
                yPos -= maxLines * 2 ;
            }
            case 2 -> {
                yPos -= maxLines * 3;
            }
            case 3 -> {
                yPos -= maxLines * 4 + 5;
            }
            case 4 -> {
                yPos -= maxLines * 5 + 10;
            }

            case 5 -> {
                yPos -= maxLines * 6 + 15;
            }
        }

        RenderUtility.drawRoundRect(
                rectX,
                yPos - 4,
                rectWidth,
                rectHeight,
                5, new Color(0, 0, 0, ((int) getBackgroundAlpha().getValue().intValue())));


        int posX = -halfWidth;


        if (heldItem != null) {
            ItemStack itemCopy = heldItem.copy();
            if (itemCopy.hasEffect() &&
                    (itemCopy.getItem() instanceof ItemTool || itemCopy.getItem() instanceof ItemArmor)) {
                itemCopy.stackSize = 1;
            }
            renderItemStack(itemCopy, posX, yPos);
            posX += 22;
        }

        for (int i = 3; i >= 0; --i) {
            ItemStack armor = player.inventory.armorInventory[i];
            if (armor != null) {
                renderItemStack(armor, posX, yPos);
                posX += 22;
            }
        }
    }


    private void renderItemStack(ItemStack stack, int xPos, int yPos) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(xPos, yPos, 0); // 使用 translate 调整位置
        mc.getRenderItem().zLevel = -150.0F;
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0); // 原点渲染
        mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.scale(0.8, 0.8, 0.6);
        renderText(stack, 0, 20); // 调整为相对原点
        GlStateManager.scale(2, 2, 2);
        GlStateManager.popMatrix();
    }

    private void renderText(ItemStack stack, int xPos, int yPos) {
        int newYPos = yPos;
        int remainingDurability = stack.getMaxDamage() - stack.getItemDamage();
//        fontRendererObj.drawString(String.valueOf(remainingDurability), (float) (xPos * 2), (float) yPos, 16777215, false);

        var fontRendererObj = La.getINSTANCE().getFontManager().getPingFang_Bold22();
        if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() < 6 ) {
            if (stack.getItem() instanceof ItemArmor) {
                int protection = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
                int projectileProtection = EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack);
                int blastProtectionLvL = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
                int fireProtection = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
                int thornsLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
                int unbreakingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);

                if (protection > 0) {
                    fontRendererObj.drawString("prot" + protection, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (projectileProtection > 0) {
                    fontRendererObj.drawString("proj" + projectileProtection, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (blastProtectionLvL > 0) {
                    fontRendererObj.drawString("bp" + blastProtectionLvL, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (fireProtection > 0) {
                    fontRendererObj.drawString("frp" + fireProtection, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (thornsLvl > 0) {
                    fontRendererObj.drawString("th" + thornsLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (unbreakingLvl > 0) {
                    fontRendererObj.drawString("ub" + unbreakingLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                }
            }
            else if (stack.getItem() instanceof ItemBow) {
                int powerLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
                int punchLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
                int flameLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
                int unbreakingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                if (powerLvl > 0) {
                    fontRendererObj.drawString("pow" + powerLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (punchLvl > 0) {
                    fontRendererObj.drawString("pun" + punchLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (flameLvl > 0) {
                    fontRendererObj.drawString("flame" + flameLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (unbreakingLvl > 0) {
                    fontRendererObj.drawString("ub" + unbreakingLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                }
            }
            else if (stack.getItem() instanceof ItemSword) {
                int sharpnessLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
                int knockbackLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
                int fireAspectLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
                int unbreakingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                if (sharpnessLvl > 0) {
                    fontRendererObj.drawString("sh" + sharpnessLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (knockbackLvl > 0) {
                    fontRendererObj.drawString("kb" + knockbackLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (fireAspectLvl > 0) {
                    fontRendererObj.drawString("fire" + fireAspectLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (unbreakingLvl > 0) {
                    fontRendererObj.drawString("ub" + unbreakingLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                }
            }
            else if (stack.getItem() instanceof ItemTool) {
                int unbreakingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                int efficiencyLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
                int fortuneLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
                int silkTouchLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
                if (efficiencyLvl > 0) {
                    fontRendererObj.drawString("eff" + efficiencyLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (fortuneLvl > 0) {
                    fontRendererObj.drawString("fo" + fortuneLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (silkTouchLvl > 0) {
                    fontRendererObj.drawString("silk" + silkTouchLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                    newYPos += 8;
                }

                if (unbreakingLvl > 0) {
                    fontRendererObj.drawString("ub" + unbreakingLvl, (float) (xPos * 2), (float) newYPos, -1, false);
                }
            }
        }
        if (!(stack.getItem() instanceof ItemSword) && !(stack.getItem() instanceof ItemBow) && !(stack.getItem() instanceof ItemTool) && !(stack.getItem() instanceof ItemArmor)) {
            fontRendererObj.drawString(stack.stackSize + "x", (float) (xPos * 2), (float) yPos, -1, false);
        }
    }

    private int getMaxLines(ItemStack stack) {
        int lineCount = 0;

        if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() < 6) {
            if (stack.getItem() instanceof ItemArmor) {
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) > 0) lineCount++;
            } else if (stack.getItem() instanceof ItemBow) {
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) > 0) lineCount++;
            } else if (stack.getItem() instanceof ItemSword) {
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) > 0) lineCount++;
            } else if (stack.getItem() instanceof ItemTool) {
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack) > 0) lineCount++;
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) > 0) lineCount++;
            }
        }

        if (!(stack.getItem() instanceof ItemSword) && !(stack.getItem() instanceof ItemBow) && !(stack.getItem() instanceof ItemTool) && !(stack.getItem() instanceof ItemArmor)) {
            lineCount++;
        }

        return lineCount;
    }

    private void setupCameraAndTransform(Entity camera, float delta, double x, double y, double z) {
        camera.posX = interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = interpolate(camera.prevPosZ, camera.posZ, delta);

        GlStateManager.pushMatrix();
        GlStateManager.enablePolygonOffset();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        float thirdPersonView = mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
        GlStateManager.rotate(mc.getRenderManager().playerViewX, thirdPersonView, 0.0F, 0.0F);
    }

    private double calculateScale(double distance) {
        double scale = (0.004 * scaleValue.getUpperValue()) * distance;
        return Math.max(scale, scaleValue.getLowerValue());
    }

    private void resetCameraPosition(Entity camera, double originalX, double originalY, double originalZ) {
        camera.posX = originalX;
        camera.posY = originalY;
        camera.posZ = originalZ;
        GlStateManager.popMatrix();
    }

}
