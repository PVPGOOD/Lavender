package io.justme.lavender.ui.screens.clickgui.imgui.panels.popup;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.imgui.components.AbstractOptionComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.components.impl.ComBoxComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.components.impl.ModeComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.components.impl.SliderComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.components.impl.SwitchComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.components.impl.scrollbar.ScrollbarComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.AbstractPanel;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.module.chill.ModuleButton;
import io.justme.lavender.ui.screens.clickgui.imgui.theme.ThemeColorEnum;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.ScissorUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.utility.math.animation.util.Easings;
import io.justme.lavender.value.DefaultValue;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.ModeValue;
import io.justme.lavender.value.impl.MultiBoolValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import org.lwjglx.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/
@Getter
@Setter
public class PopupPanel extends AbstractPanel {

    private int mouseX,mouseY;
    private float ScrollOffset = 0;
    private float maxScroll = 0;
    private float x,y,width,height,draggingX,draggingY,scalingWidth, scalingHeight,lastMouseX,lastMouseY;
    private boolean dragging,scaling,expanded;
    private Module module;
    private CopyOnWriteArrayList<AbstractOptionComponent> valueComponents = new CopyOnWriteArrayList<>();
    private Animation animationHeight = new Animation(20);
    private Animation animationWidth = new Animation(100);
    private ScrollbarComponent scrollbarComponent = new ScrollbarComponent();


    public PopupPanel(Module module) {
        this.setName("PopupScreen");
        this.module = module;

        getValueComponents().clear();

        for (DefaultValue<?> setting : getModule().getOptions()) {
            var component = getComponent(setting);
            getValueComponents().add(component);
        }

        setWidth(230);
        setHeight(250);
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

        Color color = getModule().getName().equalsIgnoreCase("clickgui") ?
                La.getINSTANCE().getTheme().getColor(ThemeColorEnum.PANEL_POPUP_TEXT) : getModule().isToggle() ? La.getINSTANCE().getTheme().getColor(ThemeColorEnum.PANEL_POPUP_TOGGLE_ON) : La.getINSTANCE().getTheme().getColor(ThemeColorEnum.PANEL_POPUP_TOGGLE_OFF);


        RenderUtility.drawRoundRect(getX(),getY(),animationWidth,animationHeight,10,color);
        RenderUtility.drawRoundRect(getX(),getY(),animationWidth,animationHeight,10,color);

        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium22();

        //标题
        fontDrawer.drawString(getModule().getName(),
                getX() + animationWidth/2f - (fontDrawer.getStringWidth(getModule().getName()) /2f),
                getY() + 3,
                La.getINSTANCE().getTheme().getColor(ThemeColorEnum.PANEL_POPUP_HIGHLIGHT).getRGB());

        //值
        AtomicInteger intervalY = new AtomicInteger();
        setScrollOffset(getScrollAnimation().getValue());
        int rightSide = 10;
        int leftSide = 10;
        int initY = 30;

        ScissorUtility.scissor(getX() + 4,getY() + initY - 5,animationWidth,animationHeight - initY + 5,()->{
            for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
                switch (abstractOptionComponent.getComponentType()) {
                    case MODE -> {
                        abstractOptionComponent.setX(getX() + 20);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY + ScrollOffset + 18);

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY + ScrollOffset);
                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                        intervalY.addAndGet((int) (40 + abstractOptionComponent.getModeExpandingHeight()));
                    }

                    case COMBOX -> {
                        abstractOptionComponent.setX(getX() + getWidth() /2f -  abstractOptionComponent.getWidth() /2f);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY + ScrollOffset);

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY + ScrollOffset);

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                        intervalY.addAndGet(30);
                    }

                    case SLIDER -> {
                        abstractOptionComponent.setX(getX() + abstractOptionComponent.getWidth() + rightSide);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY + ScrollOffset + 15);

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY + ScrollOffset);

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);

                        intervalY.addAndGet(25);
                    }

                    case SWITCH -> {
                        abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide);
                        abstractOptionComponent.setY(getY() + intervalY.get() + initY + ScrollOffset);

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() +initY + ScrollOffset);

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);
                        intervalY.addAndGet(25);
                    }

                    case CHECKBOX -> {
                        abstractOptionComponent.setX(getX() + getWidth() - abstractOptionComponent.getWidth() - rightSide);
                        abstractOptionComponent.setY(getY() + intervalY.get() +initY + ScrollOffset);

                        abstractOptionComponent.setDescriptionX(getX() + leftSide);
                        abstractOptionComponent.setDescriptionY(getY() + intervalY.get() + initY + ScrollOffset);

                        abstractOptionComponent.drawScreen(mouseX,mouseY,partialTicks);
                        intervalY.addAndGet(30);
                    }
                }

            }
            getScrollbarComponent().drawScreen(mouseX, mouseY, partialTicks);

        });

        if (isDragging()){
            setX(mouseX - getDraggingX());
            setY(mouseY - getDraggingY());
        } else if (isScaling()) {

            setWidth(Math.min(Math.max(mouseX - getScalingWidth(), 200), 500));
            setHeight(Math.min(Math.max(mouseY - getScalingHeight(), 100), intervalY.get() + initY));

            getAnimationWidth().setFromValue(getWidth());
            getAnimationHeight().setFromValue(getHeight());
        }

        getScrollbarComponent().setWidth(3);
        getScrollbarComponent().setHeight(250);
        getScrollbarComponent().setX(getX() + getWidth() - getScrollbarComponent().getWidth() - 2);
        getScrollbarComponent().setY(getY() + initY);

        getScrollbarComponent().setScrollOffset(getScrollOffset());
        getScrollbarComponent().setMaxScrollOffset(getMaxScroll());
        getScrollbarComponent().setContentHeight(intervalY.get());
        getScrollbarComponent().setViewHeight(animationHeight - initY);

        getAnimationWidth().animate(isExpanded() ? getWidth() : 100, 0.1f);
        getAnimationHeight().animate(isExpanded() ? getHeight() : 20, 0.1f);

        getAnimationHeight().update();
        getAnimationWidth().update();

        getScrollAnimation().update();

        setMouseX(mouseX);
        setMouseY(mouseY);

        setMaxScroll(Math.max(0, intervalY.get() + initY - animationHeight));
    }

    private final Animation scrollAnimation = new Animation();
    @Override
    public void handleMouseInput() throws IOException {

        if (isHover(getMouseX(),getMouseY())) {
            int scroll = Mouse.getDWheel();
            if (scroll != 0) {
                float targetOffset = ScrollOffset + scroll * 100;
                targetOffset = Math.max(-maxScroll, Math.min(0, targetOffset));
                scrollAnimation.animate(targetOffset, 0.3f, Easings.QUAD_OUT);
            }
        }

        for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
            abstractOptionComponent.handleMouseInput();
        }
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

                if (isExpanded()) {
                    if (MouseUtility.isHovering(getX() + getWidth() - 20 ,getY() + 1,20,20,mouseX,mouseY)){
                        La.getINSTANCE().getClickScreen().getAbstractPanels().remove(this);
                        La.getINSTANCE().getClickScreen().getModulePanelComponent().add(new ModuleButton(getModule()));
                    }

                    for (AbstractOptionComponent abstractOptionComponent : getValueComponents()) {
                        abstractOptionComponent.mouseClicked(mouseX,mouseY,mouseButton);
                    }

                    getScrollbarComponent().mouseClicked(mouseX, mouseY, mouseButton);
                }

            }

            case 1 -> {
                if (MouseUtility.isHovering(getX(), getY(), animationWidth, 20, mouseX, mouseY)) {
                    setExpanded(!isExpanded());
                }
            }
        }


        setLastMouseX(mouseX);
        setLastMouseY(mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        float animationHeight = isScaling() ? getHeight() : getAnimationHeight().getValue();
        float animationWidth = isScaling() ? getWidth() : getAnimationWidth().getValue();

        if (state == 0){
            if (isDragging()) {
                setDragging(false);
            }

            if (isScaling()){
                setScaling(false);
            }

            if (MouseUtility.isHovering(getX(),getY(),animationWidth,20,mouseX,mouseY)) {
                if (getLastMouseX() == mouseX && getLastMouseY() == mouseY) {

                    if (!this.getModule().getName().equalsIgnoreCase("clickgui")) {
                        getModule().setStatus(!getModule().isToggle());
                    } else {
                        var clickScreen = La.getINSTANCE().getClickScreen();

                        AbstractPanel abstractPanel = La.getINSTANCE().getClickScreen().getAbstractPanels().stream()
                                .filter(abstractComponent1 -> abstractComponent1.getName().equalsIgnoreCase("PopupScreen"))
                                .findFirst().orElse(null);

                        if (abstractPanel != null) {
                            clickScreen.getAbstractPanels().add(clickScreen.getModulePanel());
                            clickScreen.getAbstractPanels().add(clickScreen.getCategoryPanel());
                        }

                        clickScreen.getAbstractPanels().remove(this);
                    }
                }
            }

            if (-getScrollOffset() > getMaxScroll()) {
                getScrollAnimation().animate(-getMaxScroll(),0.1f);
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
            SwitchComponent switchComponent = new SwitchComponent();
            switchComponent.setOption((BoolValue) setting);
            switchComponent.afterAddOption();
            component = switchComponent;
        } else if (setting instanceof MultiBoolValue) {
            ComBoxComponent comBoxComponent = new ComBoxComponent();
            comBoxComponent.setOption((MultiBoolValue) setting);
            component = comBoxComponent;
        } else if (setting instanceof NumberValue) {
            SliderComponent sliderComponent = new SliderComponent();
            sliderComponent.setOption((NumberValue) setting);
            component = sliderComponent;
        } else if (setting instanceof ModeValue) {
            ModeComponent modeComponent = new ModeComponent();
            modeComponent.setOption((ModeValue) setting);
            modeComponent.afterAddOption();
            component = modeComponent;
        }


        return component;
    }
}
