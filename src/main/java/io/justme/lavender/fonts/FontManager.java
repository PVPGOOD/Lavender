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
    private final FontDrawer SFBold16;
    private final FontDrawer SFBold18;
    private final FontDrawer SFBold22;
    private final FontDrawer SFBold32;
    private final FontDrawer SFBold34;
    private final FontDrawer SFBold36;
    private final FontDrawer SFBold38;
    private final FontDrawer SFBold40;
    private final FontDrawer SFBold60;
    private final FontDrawer SFBold80;

    private final FontDrawer PingFang_Bold12;
    private final FontDrawer PingFang_Bold14;
    private final FontDrawer PingFang_Bold16;
    private final FontDrawer PingFang_Bold18;
    private final FontDrawer PingFang_Bold22;
    private final FontDrawer PingFang_Bold32;
    private final FontDrawer PingFang_Bold34;
    private final FontDrawer PingFang_Bold36;
    private final FontDrawer PingFang_Bold38;
    private final FontDrawer PingFang_Bold40;
    private final FontDrawer PingFang_Bold80;

    private final FontDrawer PingFang_Light12;
    private final FontDrawer PingFang_Light14;
    private final FontDrawer PingFang_Light18;
    private final FontDrawer PingFang_Light22;

    private final FontDrawer PingFang_Medium12;
    private final FontDrawer PingFang_Medium14;
    private final FontDrawer PingFang_Medium18;
    private final FontDrawer PingFang_Medium20;
    private final FontDrawer PingFang_Medium22;
    private final FontDrawer PingFang_Medium32;
    private final FontDrawer PingFang_Medium34;
    private final FontDrawer PingFang_Medium36;
    private final FontDrawer PingFang_Medium38;
    private final FontDrawer PingFang_Medium40;
    private final FontDrawer PingFang_Medium80;

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

    private final FontDrawer CheckMark4;
    private final FontDrawer CheckMark5;
    private final FontDrawer CheckMark6;
    private final FontDrawer CheckMark7;
    private final FontDrawer CheckMark8;
    private final FontDrawer CheckMark10;
    private final FontDrawer CheckMark12;
    private final FontDrawer CheckMark14;

    private final FontDrawer lavender12;
    private final FontDrawer lavender13;
    private final FontDrawer lavender14;
    private final FontDrawer lavender16;
    private final FontDrawer lavender18;
    private final FontDrawer lavender20;
    private final FontDrawer lavender22;
    private final FontDrawer lavender24;
    private final FontDrawer lavender26;
    private final FontDrawer lavender28;
    private final FontDrawer lavender30;
    private final FontDrawer lavender32;




    public FontManager() {
        try {
            SFBold12 = createByResource("SFBOLD.ttf",12f,true);
            SFBold14 = createByResource("SFBOLD.ttf",14f,true);
            SFBold16 = createByResource("SFBOLD.ttf",16f,true);
            SFBold18 = createByResource("SFBOLD.ttf",18f,true);
            SFBold22 = createByResource("SFBOLD.ttf",22f,true);
            SFBold32 = createByResource("SFBOLD.ttf",32f,true);
            SFBold34 = createByResource("SFBOLD.ttf",34f,true);
            SFBold36 = createByResource("SFBOLD.ttf",36f,true);
            SFBold38 = createByResource("SFBOLD.ttf",38f,true);
            SFBold40 = createByResource("SFBOLD.ttf",40f,true);
            SFBold60 = createByResource("SFBOLD.ttf",60f,true);
            SFBold80 = createByResource("SFBOLD.ttf",80f,true);

            PingFang_Bold12 = createByResource("PingFang-Bold.ttf",12f,true);
            PingFang_Bold14 = createByResource("PingFang-Bold.ttf",14f,true);
            PingFang_Bold16 = createByResource("PingFang-Bold.ttf",16f,true);
            PingFang_Bold18 = createByResource("PingFang-Bold.ttf",18f,true);
            PingFang_Bold22 = createByResource("PingFang-Bold.ttf",22f,true);
            PingFang_Bold32 = createByResource("PingFang-Bold.ttf",32f,true);
            PingFang_Bold34 = createByResource("PingFang-Bold.ttf",34f,true);
            PingFang_Bold36 = createByResource("PingFang-Bold.ttf",36f,true);
            PingFang_Bold38 = createByResource("PingFang-Bold.ttf",38f,true);
            PingFang_Bold40 = createByResource("PingFang-Bold.ttf",40f,true);
            PingFang_Bold80 = createByResource("PingFang-Bold.ttf",80f,true);

            PingFang_Light12 = createByResource("PingFang-Light.ttf",12f,true);
            PingFang_Light14 = createByResource("PingFang-Light.ttf",14f,true);
            PingFang_Light18 = createByResource("PingFang-Light.ttf",18f,true);
            PingFang_Light22 = createByResource("PingFang-Light.ttf",22f,true);

            PingFang_Medium12 = createByResource("PingFang-Medium.ttf",12f,true);
            PingFang_Medium14 = createByResource("PingFang-Medium.ttf",14f,true);
            PingFang_Medium18 = createByResource("PingFang-Medium.ttf",18f,true);
            PingFang_Medium20 = createByResource("PingFang-Medium.ttf",20f,true);
            PingFang_Medium22 = createByResource("PingFang-Medium.ttf",22f,true);
            PingFang_Medium32 = createByResource("PingFang-Medium.ttf",32f,true);
            PingFang_Medium34 = createByResource("PingFang-Medium.ttf",34f,true);
            PingFang_Medium36 = createByResource("PingFang-Medium.ttf",36f,true);
            PingFang_Medium38 = createByResource("PingFang-Medium.ttf",38f,true);
            PingFang_Medium40 = createByResource("PingFang-Medium.ttf",40f,true);
            PingFang_Medium80 = createByResource("PingFang-Medium.ttf",80f,true);

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

            CheckMark4 = createByResource("CheckMark.ttf",4f,true);
            CheckMark5 = createByResource("CheckMark.ttf",5f,true);
            CheckMark6 = createByResource("CheckMark.ttf",6f,true);
            CheckMark7 = createByResource("CheckMark.ttf",7f,true);
            CheckMark8 = createByResource("CheckMark.ttf",8f,true);
            CheckMark10 = createByResource("CheckMark.ttf",10f,true);
            CheckMark12 = createByResource("CheckMark.ttf",12f,true);
            CheckMark14 = createByResource("CheckMark.ttf",14f,true);

            lavender12 = createByResource("lavender.ttf",12f,true);
            lavender13 = createByResource("lavender.ttf",13f,true);
            lavender14 = createByResource("lavender.ttf",14f,true);
            lavender16 = createByResource("lavender.ttf",16f,true);
            lavender18 = createByResource("lavender.ttf",18f,true);
            lavender20 = createByResource("lavender.ttf",20f,true);
            lavender22 = createByResource("lavender.ttf",22f,true);
            lavender24 = createByResource("lavender.ttf",24f,true);
            lavender26 = createByResource("lavender.ttf",26f,true);
            lavender28 = createByResource("lavender.ttf",28f,true);
            lavender30 = createByResource("lavender.ttf",30f,true);
            lavender32 = createByResource("lavender.ttf",32f,true);


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
