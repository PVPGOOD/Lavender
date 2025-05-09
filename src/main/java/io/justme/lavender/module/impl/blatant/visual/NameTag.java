package io.justme.lavender.module.impl.blatant.visual;

import io.justme.lavender.La;
import io.justme.lavender.events.render.Event2DRender;
import io.justme.lavender.events.render.Event3DRender;
import io.justme.lavender.events.render.EventNameRender;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import org.lwjglx.input.Keyboard;

import java.awt.*;


@Getter
@ModuleInfo(name = "NameTag", description = "IDK.", category = Category.VISUAL,key = Keyboard.KEY_H)
public class NameTag extends Module {

    private final NumberValue scaleValue = new NumberValue("NameTags", 1.0D, 0.1D, 1.0D, 0.1D);


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
            if (entity != mc.thePlayer) {
                final double yOffset = entity.isSneaking() ? -0.25 : 0.0;

                final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosX();
                final double posY = (entity.lastTickPosY + yOffset) + ((entity.posY + yOffset) - (entity.lastTickPosY + yOffset)) * mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosY();
                final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosZ();

                mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);

                renderNameTag(entity, posX, posY, posZ, event.getPartialTicks());
            }
        }
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double) delta;
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y + 0.7D;

        Entity camera = mc.getRenderViewEntity();
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = interpolate(camera.prevPosZ, camera.posZ, delta);


        double distance = camera.getDistance(x + mc.getRenderManager().viewerPosX, y + mc.getRenderManager().viewerPosY, z + mc.getRenderManager().viewerPosZ);

        float width = mc.fontRendererObj.getStringWidth(getDisplayName(player)) / 2F;

        double scale = (0.004 * scaleValue.getValue().doubleValue()) * distance;

        if (scale < 0.01)
            scale = 0.01;

        GlStateManager.pushMatrix();

        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, -1500000.0F);

        GlStateManager.disableLighting();

        GlStateManager.translate((float) x, (float) tempY + 1.4F, (float) z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        float var10001 = mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
        GlStateManager.rotate(mc.getRenderManager().playerViewX, var10001, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);

        drawRect3D(-width - 2, -12, width + 2.0F, 0, (float) scale, new Color(0, 0, 0, 200).getRGB(), new Color(0, 0, 0, 180).getRGB());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        GL11.glDepthMask(false);
        var font = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        font.drawStringWithOutline(getDisplayName(player), -width, -9.5F, getDisplayColour(player));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDepthMask(true);

        GlStateManager.disableLighting();

        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;

        GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
        GlStateManager.disablePolygonOffset();

        GlStateManager.popMatrix();
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

        drawTag =   "[" + (int) entity.getDistanceToEntity(mc.thePlayer) + "m] " + drawTag + " " + color + (int) entity.getHealth();
        return drawTag;
    }

    public void drawRect3D(float x, float y, float x1, float y1, float lineWidth, int inside, int border) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        RenderUtility.drawRect(x, y, x1, y1, inside);
        float alpha = (float) (border >> 24 & 255) / 255.0f;
        float red = (float) (border >> 16 & 255) / 255.0f;
        float green = (float) (border >> 8 & 255) / 255.0f;
        float blue = (float) (border & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
}
