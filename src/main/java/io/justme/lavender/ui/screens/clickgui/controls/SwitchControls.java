package io.justme.lavender.ui.screens.clickgui.controls;

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
public class SwitchControls extends AbstractControlsComponents {

    private BoolValue boolValue = new BoolValue(getName(),true);
    private Animation animation = new Animation(6);
    private Animation scaleAnimation = new Animation();

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //背景

        //点
        Color enabledColor = new Color(0x675496);
        Color disabledColor = new Color(0x79747E);
        RenderUtility.drawRoundRectWithOutline(
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                8,
                0.6f,
                getBoolValue().getValue() ? enabledColor : new Color(0xE6E0E9),getBoolValue().getValue() ? enabledColor : new Color(0, 0,0,128));

        OGLUtility.scale(getX() + getAnimation().getValue() + getWidth() /2f,getY() + getHeight() /2f,getScaleAnimation().getValue(), () -> {
            RenderUtility.drawRoundRect(
                    getX() + getAnimation().getValue(),
                    getY() + getHeight() /2f - 5,
                    10,
                    10,
                    5,
                    getBoolValue().getValue() ? new Color(255,255,255) : disabledColor);
        });

        getScaleAnimation().update();
        getAnimation().update();

        setWidth(28);
        setHeight(16);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isHover(mouseX,mouseY)) {
            getScaleAnimation().animate(getBoolValue().getValue() ? 0.8f : 1.2f,0.1f);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            if (isHover(mouseX,mouseY)) {
                getBoolValue().setValue(!getBoolValue().getValue());
                getAnimation().animate(getBoolValue().getValue() ? 16 : 2,0.1f);
                getScaleAnimation().animate(getBoolValue().getValue() ? 1.2f : 0.8f,0.1f);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
