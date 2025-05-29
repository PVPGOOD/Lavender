package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.category.chill;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.category.AbstractCategory;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.category.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.category.CategoryType;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.category.conponent.CategoryButtonComponent;
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
        getCategoryComponents().add(new CategoryButtonComponent(CategoryType.SETTING,true));
        getCategoryComponents().add(new CategoryButtonComponent(CategoryType.ELEMENT,true));
        getCategoryComponents().add(new CategoryButtonComponent(CategoryType.SKIN,true));
        getCategoryComponents().add(new CategoryButtonComponent(CategoryType.THEME,true));
        getCategoryComponents().add(new CategoryButtonComponent(CategoryType.ABOUT,true));

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

        var fontDrawer = La.getINSTANCE().getFontManager().getSFBold16();
        fontDrawer.drawString("Category",getX() + 4,getY() + categoryComponentsInitY + 1,new Color(129, 57, 80,128).getRGB());

        int height1 = 22;
        for (AbstractComponent categoryComponents : getCategoryComponents()) {
            categoryComponents.setX(getX() + 4);
            categoryComponents.setY(getY() + categoryComponentsIntervalY + categoryComponentsInitY + 15);
            categoryComponents.setWidth(getWidth() - 8);

            categoryComponents.setHeight(height1);
            categoryComponents.drawScreen(mouseX, mouseY, partialTicks);
            categoryComponentsIntervalY += height1;
        }


        setHeight(categoryComponentsIntervalY + height1);
        setRequestInterval(categoryComponentsIntervalY);
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractComponent categoryComponents : getCategoryComponents()) {
            if (categoryComponents.isHover(mouseX,mouseY)) {
                La.getINSTANCE().getDropScreen().setCurrentCategory(categoryComponents.getAbstractCategory());

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
