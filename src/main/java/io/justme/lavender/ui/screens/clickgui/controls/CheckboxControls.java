package io.justme.lavender.ui.screens.clickgui.controls;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponents;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.value.impl.BoolValue;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/
@Getter
@Setter
public class CheckboxControls extends AbstractControlsComponents {

    private BoolValue boolValue = new BoolValue("test",true);

    private Animation animation = new Animation(100);
    private Animation scaleAnimation = new Animation(0);

    public CheckboxControls() {
        setWidth(10);
        setHeight(10);
    }

    @Override
    public void initGui() {

    }

    private final FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getCheckMark14();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRectWithOutline(
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                2,
                0.6f,
                getBoolValue().getValue() ? new Color(103, 84, 150, 255) : new Color(255, 187, 213, 0) ,new Color(0, 0,0,150));

        if (getBoolValue().getValue()) {
            getFontDrawer().drawString("a", getX() + 1.8f, getY() + getHeight() /2f - 3.8f, new Color(255, 255, 255).getRGB());
        }

        int alpha = ((int)(getAnimation().getValue()));
        int size = 5;
        if (isHover(mouseX,mouseY)) {
            RenderUtility.drawRoundRect(getX() - size,getY() - size,getWidth() + size * 2 ,getHeight() + size * 2,9,new Color(0,0,0,28));
        }

        OGLUtility.scale(getX() + getWidth()/2f,getY() + getHeight()/2f,getScaleAnimation().getValue(),() -> RenderUtility.drawRoundRect(getX() - size,getY() - size,getWidth() + size * 2 ,getHeight() + size * 2,9,new Color(0,0,0,alpha)));


        getAnimation().update();
        getScaleAnimation().update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isHover(mouseX,mouseY)) {
            getScaleAnimation().animate(1f,0.01f);
            getAnimation().animate(30,0.01f);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (isHover(mouseX,mouseY)) {
            getScaleAnimation().animate(0,0.1f);
            getBoolValue().setValue(!getBoolValue().getValue());
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

}
