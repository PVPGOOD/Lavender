package io.justme.lavender.utility.gl;

import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author JustMe.
 * @since 2024/4/28
 **/
@UtilityClass
public class OGLUtility {

    public void color(int hex) {
        GlStateManager.color(
                (hex >> 16 & 0xFF) / 255.0f,
                (hex >> 8 & 0xFF) / 255.0f,
                (hex & 0xFF) / 255.0f,
                (hex >> 24 & 0xFF) / 255.0f);
    }

    public static void color(int r,int g,int b,int a) {
        GlStateManager.color(r / 255f,g / 255f,b / 255f,a / 255f);
    }


}
