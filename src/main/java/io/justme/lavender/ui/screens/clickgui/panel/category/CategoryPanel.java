package io.justme.lavender.ui.screens.clickgui.panel.category;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.panel.AbstractPanel;
import io.justme.lavender.ui.screens.clickgui.components.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.components.impl.panel.category.CategoryButton;
import io.justme.lavender.ui.screens.clickgui.panel.category.chill.ModulePanelWindow;
import io.justme.lavender.ui.screens.clickgui.panel.category.chill.OtherCategoryPanel;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2024/12/5
 **/
@Getter
public class CategoryPanel extends AbstractPanel {

    private final CopyOnWriteArrayList<AbstractCategory> categoryComponents = new CopyOnWriteArrayList<>();

    public CategoryPanel() {
        this.setName("CategoryPanel");

        getCategoryComponents().add(new ModulePanelWindow());
        getCategoryComponents().add(new OtherCategoryPanel());
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //背景

        //分类图标
        //模块分类
        int categoryComponentsInitY = 0;
        var requestInterval = 0;
        for (AbstractCategory categoryComponents : getCategoryComponents()) {

            switch (categoryComponents.getName()) {
                case "ModulePanelWindow" -> {
                    categoryComponents.setX(getX() + 4);
                    categoryComponents.setY(getY() + categoryComponentsInitY);
                    categoryComponents.setWidth(getWidth() - 8);
                    requestInterval += (int) categoryComponents.getRequestInterval();
                }

                case "OtherCategoryPanel" -> {
                    categoryComponents.setX(getX() + 4);
                    categoryComponents.setY(getY() + categoryComponentsInitY + requestInterval + 25);
                    categoryComponents.setWidth(getWidth() - 8);
                }
            }

            categoryComponents.drawScreen(mouseX, mouseY, partialTicks);
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractCategory categoryComponents : getCategoryComponents()) {
            categoryComponents.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
