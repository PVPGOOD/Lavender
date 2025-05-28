package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.impl.category.chill;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.impl.category.AbstractCategory;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.impl.category.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.impl.category.CategoryType;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.impl.category.conponent.CategoryButtonComponent;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2025/4/4
 **/
@Getter
@Setter
public class ModulePanelWindow extends AbstractCategory {

    private final CopyOnWriteArrayList<AbstractComponent> categoryComponents = new CopyOnWriteArrayList<>();

    public ModulePanelWindow() {
        setName("ModulePanelWindow");
        getCategoryComponents().add(new CategoryButtonComponent(CategoryType.SETTING,false));
        getCategoryComponents().add(new CategoryButtonComponent(CategoryType.ELEMENT,false));
        getCategoryComponents().add(new CategoryButtonComponent(CategoryType.SKIN,false));
        getCategoryComponents().add(new CategoryButtonComponent(CategoryType.THEME,false));
        getCategoryComponents().add(new CategoryButtonComponent(CategoryType.ABOUT,false));

    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int categoryComponentsIntervalY = 0;
        int categoryComponentsInitY = 15;

        RenderUtility.drawRoundRect(
                getX(),
                getY() + categoryComponentsInitY,
                getWidth(),
                getHeight(),
                8,
                new Color(243, 237, 247));

        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        fontDrawer.drawString("Category",getX() + 4,getY() + categoryComponentsInitY + 1,new Color(129, 57, 80,128).getRGB());

        for (AbstractComponent categoryComponents : getCategoryComponents()) {
            categoryComponents.setX(getX() + 4);
            categoryComponents.setY(getY() + categoryComponentsIntervalY + categoryComponentsInitY + 15);
            categoryComponents.setWidth(getWidth() - 8);
            categoryComponents.setHeight(22);
            categoryComponents.drawScreen(mouseX, mouseY, partialTicks);
            categoryComponentsIntervalY += 22;
        }


        setHeight(categoryComponentsIntervalY + 15);
        setRequestInterval(categoryComponentsIntervalY);
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractComponent categoryComponents : getCategoryComponents()) {
            if (categoryComponents.isHover(mouseX,mouseY)) {
//                La.getINSTANCE().getClickScreen().setCurrentCategory(categoryComponents.getAbstractCategory());

            }
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
