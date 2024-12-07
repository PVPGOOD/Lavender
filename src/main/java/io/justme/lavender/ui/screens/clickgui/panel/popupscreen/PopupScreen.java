package io.justme.lavender.ui.screens.clickgui.panel.popupscreen;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponents;
import io.justme.lavender.ui.screens.clickgui.controls.*;
import io.justme.lavender.ui.screens.clickgui.panel.module.chill.ModuleButton;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.Animation;
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

    private CopyOnWriteArrayList<AbstractControlsComponents> controlsComponents = new CopyOnWriteArrayList<>();
    private Animation animationHeight = new Animation(20);
    private Animation animationWidth = new Animation(100);


    public PopupScreen(Module module) {
        this.setName("PopupScreen");
        this.module = module;

        getControlsComponents().add(new SliderControls());
        getControlsComponents().add(new SwitchControls());
        getControlsComponents().add(new CheckboxControls());
        getControlsComponents().add(new ModeControls());
        getControlsComponents().add(new ComBoxControls());

        setWidth(150);
        setHeight(200);


    }

    @Override
    public void initGui() {

        for (AbstractControlsComponents controlsComponent : getControlsComponents()) {
            controlsComponent.initGui();
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
        for (AbstractControlsComponents controlsComponent : getControlsComponents()) {
            controlsComponent.setX(getX() + 4);
            controlsComponent.setY(getY() + intervalY +  25);
            controlsComponent.drawScreen(mouseX,mouseY,partialTicks);

            intervalY += 30;
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
                    for (AbstractControlsComponents controlsComponent : getControlsComponents()) {
                        controlsComponent.mouseClicked(mouseX,mouseY,mouseButton);
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

        for (AbstractControlsComponents controlsComponent : getControlsComponents()) {
            controlsComponent.mouseReleased(mouseX,mouseY,state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
