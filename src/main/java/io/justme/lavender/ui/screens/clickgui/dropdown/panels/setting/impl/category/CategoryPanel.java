package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.impl.category;

import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.AbstractSetting;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.SettingType;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.impl.category.chill.ModulePanelWindow;
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
public class CategoryPanel extends AbstractSetting {

    private final CopyOnWriteArrayList<AbstractCategory> categoryComponents = new CopyOnWriteArrayList<>();

    public CategoryPanel(SettingType type) {
        super(type);
        getCategoryComponents().add(new ModulePanelWindow());
//        getCategoryComponents().add(new OtherCategoryPanel());
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRectWithCustomRounded(getX(),getY(),getWidth(),getHeight() ,new Color(0x1CE5DAF5, true),35,0,37,0);
        //分类图标
        //模块分类
        int categoryComponentsInitY = 15;
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
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractCategory categoryComponents : getCategoryComponents()) {
            categoryComponents.mouseClicked(mouseX, mouseY, mouseButton);
        }

        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        return false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
