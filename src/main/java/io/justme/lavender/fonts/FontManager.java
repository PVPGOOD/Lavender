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

    private final FontDrawer PingFang_Bold12;
    private final FontDrawer PingFang_Bold14;
    private final FontDrawer PingFang_Bold18;
    private final FontDrawer PingFang_Bold22;

    private final FontDrawer PingFang_Light12;
    private final FontDrawer PingFang_Light14;
    private final FontDrawer PingFang_Light18;
    private final FontDrawer PingFang_Light22;

    private final FontDrawer PingFang_Medium12;
    private final FontDrawer PingFang_Medium14;
    private final FontDrawer PingFang_Medium18;
    private final FontDrawer PingFang_Medium22;

    private final FontDrawer PingFang_Regular12;
    private final FontDrawer PingFang_Regular14;
    private final FontDrawer PingFang_Regular18;
    private final FontDrawer PingFang_Regular22;

    private final FontDrawer PingFang_ExtraLight12;
    private final FontDrawer PingFang_ExtraLight14;
    private final FontDrawer PingFang_ExtraLight18;
    private final FontDrawer PingFang_ExtraLight22;

    private final FontDrawer PingFang_Heavy12;
    private final FontDrawer PingFang_Heavy14;
    private final FontDrawer PingFang_Heavy18;
    private final FontDrawer PingFang_Heavy22;



    public FontManager() {
        try {
            SFBold12 = createByResource("SFBOLD.ttf",12f,true);
            SFBold14 = createByResource("SFBOLD.ttf",14f,true);
            SFBold18 = createByResource("SFBOLD.ttf",18f,true);
            SFBold22 = createByResource("SFBOLD.ttf",22f,true);

            PingFang_Bold12 = createByResource("PingFang-Bold.ttf",12f,true);
            PingFang_Bold14 = createByResource("PingFang-Bold.ttf",14f,true);
            PingFang_Bold18 = createByResource("PingFang-Bold.ttf",18f,true);
            PingFang_Bold22 = createByResource("PingFang-Bold.ttf",22f,true);

            PingFang_Light12 = createByResource("PingFang-Light.ttf",12f,true);
            PingFang_Light14 = createByResource("PingFang-Light.ttf",14f,true);
            PingFang_Light18 = createByResource("PingFang-Light.ttf",18f,true);
            PingFang_Light22 = createByResource("PingFang-Light.ttf",22f,true);

            PingFang_Medium12 = createByResource("PingFang-Medium.ttf",12f,true);
            PingFang_Medium14 = createByResource("PingFang-Medium.ttf",14f,true);
            PingFang_Medium18 = createByResource("PingFang-Medium.ttf",18f,true);
            PingFang_Medium22 = createByResource("PingFang-Medium.ttf",22f,true);

            PingFang_Regular12 = createByResource("PingFang-Regular.ttf",12f,true);
            PingFang_Regular14 = createByResource("PingFang-Regular.ttf",14f,true);
            PingFang_Regular18 = createByResource("PingFang-Regular.ttf",18f,true);
            PingFang_Regular22 = createByResource("PingFang-Regular.ttf",22f,true);

            PingFang_ExtraLight12 = createByResource("PingFang-ExtraLight.ttf",12f,true);
            PingFang_ExtraLight14 = createByResource("PingFang-ExtraLight.ttf",14f,true);
            PingFang_ExtraLight18 = createByResource("PingFang-ExtraLight.ttf",18f,true);
            PingFang_ExtraLight22 = createByResource("PingFang-ExtraLight.ttf",22f,true);

            PingFang_Heavy12 = createByResource("PingFang-Heavy.ttf",12f,true);
            PingFang_Heavy14 = createByResource("PingFang-Heavy.ttf",14f,true);
            PingFang_Heavy18 = createByResource("PingFang-Heavy.ttf",18f,true);
            PingFang_Heavy22 = createByResource("PingFang-Heavy.ttf",22f,true);

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
