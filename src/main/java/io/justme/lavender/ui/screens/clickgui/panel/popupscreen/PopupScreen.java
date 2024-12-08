package io.justme.lavender.ui.screens.clickgui.panel.popupscreen;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractOptionComponent;
import io.justme.lavender.ui.screens.clickgui.controls.*;
import io.justme.lavender.ui.screens.clickgui.panel.module.chill.ModuleButton;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.MultiBoolValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import io.justme.lavender.module.Module;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/
@Getter
@Setter
public class PopupScreen extends AbstractComponent {

    private float x,y,width,height;
    private float draggingX,draggingY,scalingWidth, scalingHeight;
    private boolean dragging,scaling,expanded;
    private Module module;

    private CopyOnWriteArrayList<AbstractOptionComponent> valueComponents = new CopyOnWriteArrayList<>();
    private Animation animationHeight = new Animation(20);
    private Animation animationWidth = new Animation(100);


    public PopupScreen(Module module) {
        this.setName("PopupScreen");
        this.module = module;

        getValueComponents().clear();

        for (DefaultValue<?> setting : getModule().getOptions()) {
            var component = getComponent(setting);
            getValueComponents().add(component);
        }

        setWidth(150);
        setHeight(200);


    }

    @Override
    public void initGui() {

        for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
            abstractOptionComponent.initGui();
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        float animationHeight = isScaling() ? getHeight() : getAnimationHeight().getValue();
        float animationWidth = isScaling() ? getWidth() : getAnimationWidth().getValue();

        RenderUtility.drawRoundRect(getX(),getY(),animationWidth,animationHeight,10,new Color(La.getINSTANCE().getClickScreen().getClickGuiColor().getRGB()));

        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium22();

        //标题
        fontDrawer.drawString(getModule().getName(),
                getX() + animationWidth/2f - (fontDrawer.getStringWidth(getModule().getName()) /2f),
                getY() + 3,
                new Color(129, 57, 80,255).getRGB());

        //值
        int intervalY = 0;
        int rightSide = 10;
        int leftSide = 10;
        int initY = 30;
        for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
            switch (abstractOptionComponent.getControlsType()) {
                case MODE -> {
                    abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide);
                    abstractOptionComponent.setY(getY() + intervalY +  initY);

                    abstractOptionComponent.setDescriptionX(getX() + leftSide);
                    abstractOptionComponent.setDescriptionY(getY() + intervalY +  initY);

                    intervalY += 35;
                }

                case COMBOX -> {
                    abstractOptionComponent.setX(getX() + getWidth() /2f - abstractOptionComponent.getWidth() /2f);
                    abstractOptionComponent.setY(getY() + intervalY +  initY);

                    abstractOptionComponent.setDescriptionX(getX() + leftSide);
                    abstractOptionComponent.setDescriptionY(getY() + intervalY +  initY);

                    intervalY += 30;
                }

                case SLIDER -> {
                    abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide);
                    abstractOptionComponent.setY(getY() + intervalY +  initY);

                    abstractOptionComponent.setDescriptionX(getX() + leftSide);
                    abstractOptionComponent.setDescriptionY(getY() + intervalY + initY - fontDrawer.getHeight() /2f + abstractOptionComponent.getHeight());


                    intervalY += 25;
                }

                case SWITCH -> {
                    abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide);
                    abstractOptionComponent.setY(getY() + intervalY +  initY);

                    abstractOptionComponent.setDescriptionX(getX() + leftSide);
                    abstractOptionComponent.setDescriptionY(getY() + intervalY + initY);

                    intervalY += 25;
                }

                case CHECKBOX -> {
                    abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide);
                    abstractOptionComponent.setY(getY() + intervalY + initY);

                    abstractOptionComponent.setDescriptionX(getX() + leftSide);
                    abstractOptionComponent.setDescriptionY(getY() + intervalY +  initY);

                    intervalY += 30;
                }
            }

            abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);
        }

        if (isDragging()){
            setX(mouseX - getDraggingX());
            setY(mouseY - getDraggingY());
        } else if (isScaling()) {
            setWidth(mouseX - getScalingWidth());
            setHeight(mouseY - getScalingHeight());

            getAnimationWidth().setFromValue(getWidth());
            getAnimationHeight().setFromValue(getHeight());
        }

        getAnimationWidth().animate(isExpanded() ? getWidth() : 100, 0.1f);
        getAnimationHeight().animate(isExpanded() ? getHeight() : 20, 0.1f);

        getAnimationHeight().update();
        getAnimationWidth().update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        float animationHeight = isScaling() ? getHeight() : getAnimationHeight().getValue();
        float animationWidth = isScaling() ? getWidth() : getAnimationWidth().getValue();

        switch (mouseButton) {
            case 0 -> {
                if (MouseUtility.isHovering(getX(),getY(),animationWidth,20,mouseX,mouseY)){
                    setDraggingX(mouseX - getX());
                    setDraggingY(mouseY - getY());
                    setDragging(true);
                }


                //在没展开的情况不能缩放
                if (MouseUtility.isHovering(
                        getX() + getWidth() - 20 ,
                        getY() + getHeight() - 20,
                        20,
                        20,mouseX,mouseY) && isExpanded()){
                    setScalingWidth(mouseX - getWidth());
                    setScalingHeight(mouseY - getHeight());
                    setScaling(true);
                }

                if (isHover(mouseX,mouseY)) {
                    for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
                        abstractOptionComponent.mouseClicked(mouseX,mouseY,mouseButton);
                    }
                }

                if (MouseUtility.isHovering(getX() + getWidth() - 20 ,getY() + 1,20,20,mouseX,mouseY)){
                    La.getINSTANCE().getClickScreen().getComponents().remove(this);
                    La.getINSTANCE().getClickScreen().getModulePanelComponent().add(new ModuleButton(getModule()));
                }

            }

            case 1 -> {
                if (MouseUtility.isHovering(getX(), getY(), animationWidth, 20, mouseX, mouseY)) {
                    setExpanded(!isExpanded());
                }
            }
        }


    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0){
            if (isDragging()) {
                setDragging(false);
            } else
            if (isScaling()){
                setScaling(false);
            }
        }

        for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
            abstractOptionComponent.mouseReleased(mouseX,mouseY,state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    private AbstractOptionComponent getComponent(DefaultValue<?> setting) {
        AbstractOptionComponent component = null;

        if (setting instanceof BoolValue)  {
            SwitchControls switchControls = new SwitchControls();
            switchControls.setOption((BoolValue) setting);
            switchControls.afterAddOption();
            component = switchControls;
        } else if (setting instanceof ModeValue) {
            ModeControls modeControls = new ModeControls();
            modeControls.setOption((ModeValue) setting);
            modeControls.afterAddOption();
            component = modeControls;
        } else if (setting instanceof MultiBoolValue) {
            ComBoxControls comBoxControls = new ComBoxControls();
            comBoxControls.setOption((MultiBoolValue) setting);
            component = comBoxControls;
        } else if (setting instanceof NumberValue) {
            SliderControls sliderControls = new SliderControls();
            sliderControls.setOption((NumberValue) setting);
            component = sliderControls;
        }


        return component;
    }
}
