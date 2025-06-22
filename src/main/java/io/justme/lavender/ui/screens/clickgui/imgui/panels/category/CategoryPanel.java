package io.justme.lavender.ui.screens.clickgui.imgui.panels.category;

import io.justme.lavender.ui.screens.clickgui.imgui.panels.AbstractPanel;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.chill.ModulePanelWindow;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.chill.OtherCategoryPanel;
import io.justme.lavender.utility.gl.RenderUtility;
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
        int categoryComponentsInterval = 0;
        int requestInterval = 5;
        for (AbstractCategory categoryComponents : getCategoryComponents()) {
            requestInterval += (int) categoryComponents.getRequestInterval() + 25;
        }
        var categoryInitY = 25;
        int initX = 4;
        RenderUtility.drawRoundRect(getX() + initX/2f, getY() + categoryComponentsInterval + categoryInitY - 3, getWidth() - initX, requestInterval, 10, new Color(26, 28, 38));

        //分类图标
        //模块分类
        requestInterval = 0;
        for (AbstractCategory categoryComponents : getCategoryComponents()) {
            switch (categoryComponents.getName()) {
                case "ModulePanelWindow" -> {
                    categoryComponents.setX(getX() + initX);
                    categoryComponents.setY(getY() + categoryComponentsInterval);
                    categoryComponents.setWidth(getWidth() - initX * 2);
                    requestInterval += (int) categoryComponents.getRequestInterval();
                }

                case "OtherCategoryPanel" -> {
                    categoryComponents.setX(getX() + initX);
                    categoryComponents.setY(getY() + categoryComponentsInterval + requestInterval + categoryInitY);
                    categoryComponents.setWidth(getWidth() - initX * 2);
                    requestInterval += (int) categoryComponents.getRequestInterval();
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
