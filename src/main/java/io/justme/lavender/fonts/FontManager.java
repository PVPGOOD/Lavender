package io.justme.lavender.fonts;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@Getter
@SuppressWarnings("all")
public final class FontManager {

    private final FontDrawer SFBold12;
    private final FontDrawer SFBold14;
    private final FontDrawer SFBold18;
    private final FontDrawer SFBold22;

    public FontManager() {
        try {
            SFBold12 = createByResource("SFBOLD.ttf",12f,true);
            SFBold14 = createByResource("SFBOLD.ttf",14f,true);
            SFBold18 = createByResource("SFBOLD.ttf",18f,true);
            SFBold22 = createByResource("SFBOLD.ttf",22f,true);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    private FontDrawer createByName(String fontName, int size, boolean antiAliasing) {
        return new FontDrawer(new Font(fontName,Font.PLAIN,size), antiAliasing,false);
    }

    private FontDrawer createByResource(String resourceName, float size, boolean antiAliasing) throws Throwable {

        var  inputStream = Minecraft.getMinecraft().getResourceManager().getResource(
                new ResourceLocation("la" + "/fonts/" + resourceName)).getInputStream();

        return new FontDrawer(
                Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(Font.PLAIN, size),true, antiAliasing
        );
    }
}
