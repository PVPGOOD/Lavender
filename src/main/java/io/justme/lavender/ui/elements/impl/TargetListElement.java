package io.justme.lavender.ui.elements.impl;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.module.impl.blatant.fight.KillAura;
import io.justme.lavender.ui.elements.AbstractElement;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * @author JustMe.
 * @since 2024/8/9
 **/
@Getter
@Setter
public class TargetListElement extends AbstractElement {

    private float x,y,width,height;
    private boolean dragging;


    public TargetListElement() {
        super("TargetList Element");
    }

    private int playerHealth = 0;
    private int lastPlayerHealth = 0;
    private long lastSystemTime = 0L;
    private long healthUpdateCounter = 0L;
    private int updateCounter;
    private final Random rand = new Random();
    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {
        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),8  , Color.BLACK);

        var ka = (((KillAura) La.getINSTANCE().getModuleManager().getModuleByName("KillAura")));

        var interval = 0;

        EntityLivingBase target = ka.getTarget();
        if (target != null) {

            FontDrawer font = La.getINSTANCE().getFontManager().getPingFang_Bold16();
            font.drawString(
                    target.getName(),
                    getX() + 5,
                    getY() + 5 + interval,
                    new Color(255, 255, 255, 255).getRGB());
            interval += 5;

            float initX = 45;
            var healthBarX = getX() + initX;
            var healthBarY = getY() - getHeight()/2f + font.getHeight();

            int currentHealth = MathHelper.ceiling_float_int(target.getHealth());
            boolean isHealthBlinking = this.healthUpdateCounter > (long)this.updateCounter && (this.healthUpdateCounter - (long)this.updateCounter) / 3L % 2L == 1L;

            if (currentHealth < this.playerHealth && target.hurtResistantTime > 0)
            {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = (this.updateCounter + 20);
            }
            else if (currentHealth > this.playerHealth && target.hurtResistantTime > 0)
            {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = (long)(this.updateCounter + 10);
            }

            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L)
            {
                this.playerHealth = currentHealth;
                this.lastPlayerHealth = currentHealth;
                this.lastSystemTime = Minecraft.getSystemTime();
            }


            this.playerHealth = currentHealth;
            int lastHealth = lastPlayerHealth;
            this.rand.setSeed(updateCounter * 312871L);
            IAttributeInstance maxHealthAttribute = target.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            float maxHealth = (float)maxHealthAttribute.getAttributeValue();
            float absorptionAmount = target.getAbsorptionAmount();
            int totalHealthRows = MathHelper.ceiling_float_int((maxHealth + absorptionAmount) / 2.0F / 10.0F);
            int rowSpacing = Math.max(10 - (totalHealthRows - 2), 3);
            float remainingAbsorption = absorptionAmount;
            int regenHeartIndex = -1;

            if (target.isPotionActive(Potion.regeneration))
            {
                regenHeartIndex = this.updateCounter % MathHelper.ceiling_float_int(maxHealth + 5.0F);
            }

            int maxHearts = MathHelper.ceiling_float_int((maxHealth + absorptionAmount) / 2.0F);
            int healthBarWidth = Math.min(maxHearts, 10) * 8; // 每行最多10个心，单个心宽度为8像素

            for (int heartIndex = MathHelper.ceiling_float_int((maxHealth + absorptionAmount) / 2.0F) - 1; heartIndex >= 0; --heartIndex)
            {
                int iconOffset = 16;

                if (target.isPotionActive(Potion.poison))
                {
                    iconOffset += 36;
                }
                else if (target.isPotionActive(Potion.wither))
                {
                    iconOffset += 72;
                }

                int blinkOffset = 0;

                if (isHealthBlinking)
                {
                    blinkOffset = 1;
                }

                int rowIndex = MathHelper.ceiling_float_int((float)(heartIndex + 1) / 10.0F) - 1;
                var iconX = healthBarX + heartIndex % 10 * 8;
                var iconY = healthBarY - rowIndex * rowSpacing;

                if (currentHealth <= 4)
                {
                    iconY += this.rand.nextInt(2);
                }

                if (heartIndex == regenHeartIndex)
                {
                    iconY -= 2;
                }

                int hardcoreOffset = 0;

                if (target.worldObj.getWorldInfo().isHardcoreModeEnabled())
                {
                    hardcoreOffset = 5;
                }

                var ICONS = new ResourceLocation("textures/gui/icons.png");

                Minecraft.getMinecraft().getTextureManager().bindTexture(ICONS);
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(iconX, iconY, 16 + blinkOffset * 9, 9 * hardcoreOffset, 9, 9);

                if (isHealthBlinking)
                {
                    if (heartIndex * 2 + 1 < lastHealth)
                    {
                        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(iconX, iconY, iconOffset + 54, 9 * hardcoreOffset, 9, 9);
                    }

                    if (heartIndex * 2 + 1 == lastHealth)
                    {
                        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(iconX, iconY, iconOffset + 63, 9 * hardcoreOffset, 9, 9);
                    }
                }

                if (remainingAbsorption <= 0.0F)
                {
                    if (heartIndex * 2 + 1 < currentHealth)
                    {
                        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(iconX, iconY, iconOffset + 36, 9 * hardcoreOffset, 9, 9);
                    }

                    if (heartIndex * 2 + 1 == currentHealth)
                    {
                        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(iconX, iconY, iconOffset + 45, 9 * hardcoreOffset, 9, 9);
                    }
                }
                else
                {
                    if (remainingAbsorption == absorptionAmount && absorptionAmount % 2.0F == 1.0F)
                    {
                        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(iconX, iconY, iconOffset + 153, 9 * hardcoreOffset, 9, 9);
                    }
                    else
                    {
                        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(iconX, iconY, iconOffset + 144, 9 * hardcoreOffset, 9, 9);
                    }

                    remainingAbsorption -= 2.0F;
                }
            }

            setWidth(healthBarWidth + initX + 10);

        } else {
            La.getINSTANCE().getFontManager().getSFBold14().drawString(
                  "No Target Here...",
                    getX() + 5,
                    getY() + 3 + interval,
                    new Color(255,255,255,144).getRGB());
            setWidth(80);
        }

        setX(getPosX());
        setY(getPosY());
        setHeight(15 + interval);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            if (MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY)) {
                setDragging(true);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        setDragging(false);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
