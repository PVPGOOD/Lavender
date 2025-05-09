package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.impl.ModuleButton;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.impl.ModuleGroupHeader;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import io.justme.lavender.utility.ScissorHelper;
import io.justme.lavender.utility.gl.RenderUtility;
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

        getElements().add(new ModuleGroupHeader(type, ModulePanelType.MODULE_GROUP_HEADER));

        for (Module module : La.getINSTANCE().getModuleManager().getElements()) {
            if (!module.getCategory().getName().equalsIgnoreCase(type.getName())) continue;

            getElements().add(new ModuleButton(type, ModulePanelType.MODULE_BUTTON, module));
        }
    }

    @Override
    public void initGui() {

    }

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
                    element.setX(getX());
                    element.setY(getY());
                    element.setHeight(30);
                    element.setWidth(getWidth());
                    element.drawScreen(mouseX, mouseY, partialTicks);
                }
            }
        }

        ScissorHelper.scissor(getX(),getY() + initY - 10,getWidth(),getHeight() - initY - 3, () -> {
            for (AbstractModulePanel element : elements) {
                switch (element.getPanelType()) {
                    case MODULE_BUTTON -> {
                        element.setX(getX() + 5);
                        element.setY(getY() + initY + intervalY.get() + ScrollOffset);
                        intervalY.addAndGet((int) (32 + element.getRequestHeight()));
                        element.drawScreen(mouseX, mouseY, partialTicks);
                    }
                }
            }
        });


        setMouseX(mouseX);
        setMouseY(mouseY);
        setMaxScroll(Math.max(0, intervalY.get() + initY - getHeight()));


        setWidth(120);
        setHeight(300);
        getScrollAnimation().update();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
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
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractModulePanel element : elements) {
            switch (element.getPanelType()) {
                case MODULE_GROUP_HEADER -> {
                    element.mouseReleased(mouseX, mouseY, state);
                }

                case MODULE_BUTTON -> {
                    if (element.mouseReleased(mouseX, mouseY, state)) {
                        if (element instanceof ModuleButton moduleButton) {
                            switch (state) {
                                case 0 -> moduleButton.getModule().setToggle(!moduleButton.getModule().isToggle());
                                case 1 -> moduleButton.setExpanded(!moduleButton.isExpanded());
                            }
                        }
                        return false;
                    }
                }
            }
        }

        return false;
    }


    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

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
