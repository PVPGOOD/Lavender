package io.justme.lavender.ui.screens.clickgui.imgui.components.impl;

import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.imgui.components.AbstractOptionComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.components.ComponentType;
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
public class SwitchComponent extends AbstractOptionComponent {

    private BoolValue option;
    private Animation animation;
    private Animation scaleAnimation = new Animation();

    public SwitchComponent() {
        this.componentType = ComponentType.SWITCH;
    }

    public void afterAddOption() {
        setScaleAnimation(new Animation(getOption().getValue() ? 1.2f : 0.8f));
        setAnimation(new Animation(getOption().getValue() ? 16 : 2));
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        FontDrawer fontDrawer = getFontDrawer();
        fontDrawer.drawString(getOption().getName(),getDescriptionX(),getDescriptionY(),new Color(0).getRGB());

        //背景
        Color enabledColor = new Color(255, 198, 215, 255);
        Color disabledColor = new Color(0x79747E);
        RenderUtility.drawRoundRectWithOutline(
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                8,
                0.6f,
                getOption().getValue() ? enabledColor : new Color(0xE6E0E9), getOption().getValue() ? enabledColor : new Color(0, 0,0,128));
        //点
        OGLUtility.scale(getX() + getAnimation().getValue() + getWidth() /2f,getY() + getHeight() /2f,getScaleAnimation().getValue(), () -> {
            RenderUtility.drawRoundRect(
                    getX() + getAnimation().getValue(),
                    getY() + getHeight() /2f - 5,
                    10,
                    10,
                    5,
                    getOption().getValue() ? new Color(255,255,255) : disabledColor);
        });

        getScaleAnimation().update();
        getAnimation().update();

        setWidth(28);
        setHeight(16);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isHover(mouseX,mouseY)) {
            getScaleAnimation().animate(getOption().getValue() ? 0.8f : 1.2f,0.1f);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            if (isHover(mouseX,mouseY)) {
                getOption().setValue(!getOption().getValue());
                getAnimation().animate(getOption().getValue() ? 16 : 2,0.1f);
                getScaleAnimation().animate(getOption().getValue() ? 1.2f : 0.8f,0.1f);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
