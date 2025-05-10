package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.impl.ModuleButton;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.impl.ModuleGroupHeader;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.ScissorUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JustMe.
 * @since 2025/5/8
 **/
//单个模块的列表
@Getter
@Setter
public class ModuleList extends AbstractModulePanel {

    private int mouseX,mouseY;
    private float ScrollOffset = 0;
    private float maxScroll = 0;
    private float draggingX,draggingY,scalingWidth, scalingHeight,lastMouseX,lastMouseY;

    //子组件
    //头部 按钮 等
    private final CopyOnWriteArrayList<AbstractModulePanel> elements = new CopyOnWriteArrayList<>();

    public ModuleList(CategoryType type) {
        super(type);

        setWidth(120);
        setHeight(300);

        getElements().add(new ModuleGroupHeader(type, ModulePanelType.MODULE_GROUP_HEADER));

        for (Module module : La.getINSTANCE().getModuleManager().getElements()) {
            if (!module.getCategory().getName().equalsIgnoreCase(type.getName())) continue;
            getElements().add(new ModuleButton(type, ModulePanelType.MODULE_BUTTON, module));
        }
    }

    @Override
    public void initGui() {

    }

    private float lastHeight = 300;
    private Animation scrollAnimation = new Animation();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 18, new Color(0xF8F2FA));

        setScrollOffset(getScrollAnimation().getValue());
        AtomicInteger intervalY = new AtomicInteger();
        var initY = 40;

        for (AbstractModulePanel element : elements) {
            switch (element.getPanelType()) {
                case MODULE_GROUP_HEADER -> {
                    element.setExpanded(isExpanded());
                    element.setX(getX());
                    element.setY(getY());
                    element.setHeight(30);
                    element.setWidth(getWidth());
                    element.drawScreen(mouseX, mouseY, partialTicks);
                }
            }
        }

        ScissorUtility.scissor(getX(),getY() + initY - 10,getWidth(),getHeight() - initY - 3, () -> {
            for (AbstractModulePanel element : elements) {
                switch (element.getPanelType()) {
                    case MODULE_BUTTON -> {
                        element.setX(getX() + 5);
                        element.setY(getY() + initY + intervalY.get() + ScrollOffset);
                        element.setWidth(getWidth() - 10);
                        element.drawScreen(mouseX, mouseY, partialTicks);
                        intervalY.addAndGet((int) (32 + element.getRequestHeight()));
                    }
                }
            }
        });

        if (isDragging()) {
            setX(mouseX - getDraggingX());
            setY(mouseY - getDraggingY());
        }

        if (isScaling()) {
            getExpandedHeightAnimation().animate(isExpanded() ? getLastHeight() : 35,0.1f);
//            setWidth(mouseX - getScalingWidth());
//            setHeight(mouseY - getScalingHeight());

            setWidth(Math.min(Math.max(mouseX - getScalingWidth(), 120), 180));

            setHeight(Math.min(Math.max(mouseY - getScalingHeight(), 120), 650));
            getExpandedHeightAnimation().setToValue(getHeight());
            setLastHeight(getHeight());
        } else {
            getExpandedHeightAnimation().animate(isExpanded() ? getLastHeight() : 35,0.1f,Easings.QUART_OUT);
            setHeight(getExpandedHeightAnimation().getValue());
        }


        getExpandedHeightAnimation().update();
        setMouseX(mouseX);
        setMouseY(mouseY);
        setMaxScroll(Math.max(0, intervalY.get() + initY - getHeight()));

        getScrollAnimation().update();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if (mouseButton == 0) {
            if (MouseUtility.isHovering(getX(), getY(), getWidth() , 20, mouseX, mouseY)) {
                setDraggingX(mouseX - getX());
                setDraggingY(mouseY - getY());
                setDragging(true);
            } else if (MouseUtility.isHovering(getX() + getWidth() - 20, getY() + getHeight() - 20, 20, 20, mouseX, mouseY)) {
                setScalingWidth(mouseX - getWidth());
                setScalingHeight(mouseY - getHeight());
                setScaling(true);
            }
        }

        if (mouseButton == 1) {
            if (MouseUtility.isHovering(getX(), getY(), getWidth(), 20, mouseX, mouseY)) {
                setExpanded(!isExpanded());
            }
        }

        if (isExpanded()) {
            for (AbstractModulePanel element : elements) {
                switch (element.getPanelType()) {
                    case MODULE_GROUP_HEADER -> {
                        element.mouseClicked(mouseX, mouseY, mouseButton);
                    }

                    case MODULE_BUTTON -> {
                        element.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {

        if (isExpanded()) {
            for (AbstractModulePanel element : elements) {
                switch (element.getPanelType()) {
                    case MODULE_GROUP_HEADER -> {
                        element.mouseReleased(mouseX, mouseY, state);
                    }

                    case MODULE_BUTTON -> {
                        if (element.mouseReleased(mouseX, mouseY, state)) {
                            if (element instanceof ModuleButton moduleButton) {
                                switch (state) {
                                    case 0 -> moduleButton.getModule().setStatus(!moduleButton.getModule().isToggle());
                                    case 1 -> moduleButton.setExpanded(!moduleButton.isExpanded());
                                }
                            }
                            return false;
                        }
                    }
                }
            }
        }

        if (state == 0){
            if (isDragging()) {
                setDragging(false);
            }

            if (isScaling()){
                setScaling(false);
            }
        }

        return false;
    }


    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for (AbstractModulePanel element : elements) {
            element.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {

        for (AbstractModulePanel element : elements) {

            if (isHover(getMouseX(),getMouseY())) {
                int scroll = MouseUtility.getScroll();
                if (scroll != 0) {
                    float targetOffset = ScrollOffset + scroll * 50;
                    targetOffset = Math.max(-maxScroll, Math.min(0, targetOffset));
                    scrollAnimation.animate(targetOffset, 0.1f, Easings.QUAD_OUT);
                }
            }


            element.handleMouseInput();
        }
    }
}
