package io.justme.lavender.ui.elements.impl;

import io.justme.lavender.La;
import io.justme.lavender.ui.elements.AbstractElements;
import io.justme.lavender.ui.elements.ElementsEnum;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MathUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/13
 **/
@Setter
@Getter
public class Title extends AbstractElements {

    private float x,y,width,height;
    private boolean dragging;

    public Title() {
        super(ElementsEnum.Title);
    }

    private Animation animation = new Animation();
    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {

        var mc = Minecraft.getMinecraft();
        if (mc.ingameGUI.titlesTimer > 0) {
            float f3 = (float)mc.ingameGUI.titlesTimer - partialTicks;
            var alpha = 255f;

            if (mc.ingameGUI.titlesTimer > mc.ingameGUI.titleFadeOut + mc.ingameGUI.titleDisplayTime) {
                float f4 = (float)(mc.ingameGUI.titleFadeIn + mc.ingameGUI.titleDisplayTime + mc.ingameGUI.titleFadeOut) - f3;
                alpha = f4 * 255.0F / mc.ingameGUI.titleFadeIn;
            }
            if (mc.ingameGUI.titlesTimer <= mc.ingameGUI.titleFadeOut) {
                alpha = (int)(f3 * 255.0F / (float)mc.ingameGUI.titleFadeOut);
            }

            alpha = MathUtility.clamp(alpha, 0, 255);

            float finalAlpha = alpha;
            getAnimation().animate(finalAlpha > 8 ? 1 : 0,0.1f, Easings.EXPO_OUT);
            OGLUtility.scale(getX() + getWidth() /2f,getY() ,getAnimation().getValue(), () -> {
                if (finalAlpha > 8) {

                    int color = new Color(255, 255, 255, ((int) finalAlpha)).getRGB();

                    var title = La.getINSTANCE().getFontManager().getPingFang_Bold80();
                    var displayedTitle = mc.ingameGUI.displayedTitle;
                    title.drawCenteredString(
                            displayedTitle,
                            getX() + getWidth() /2f,
                            getY(),
                            color
                    );

                    var subtitle = La.getINSTANCE().getFontManager().getSFBold40();

                    subtitle.drawCenteredString(
                            mc.ingameGUI.displayedSubTitle,
                            getX() + getWidth() /2f,
                            getY() + subtitle.getHeight(),
                            color
                    );

                    setWidth(title.getStringWidth(displayedTitle));
                    setHeight(title.getHeight());
                } else {
                    setWidth(200);
                    setHeight(35);
                }
            });
        }

        if (MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY)) {
            RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),15,new Color(0,0,0,64));
        }

        setX(getPosX());
        setY(getPosY());

        getAnimation().update();
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
