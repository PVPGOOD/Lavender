package io.justme.lavender.ui.screens.clickgui.panel.category.chill;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.components.impl.panel.category.CategoryButton;
import io.justme.lavender.ui.screens.clickgui.panel.category.AbstractCategory;
import io.justme.lavender.ui.screens.clickgui.panel.category.CategoryType;
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

    private final CopyOnWriteArrayList<AbstractControlsComponent> categoryComponents = new CopyOnWriteArrayList<>();

    public ModulePanelWindow() {
        setName("ModulePanelWindow");
        getCategoryComponents().add(new CategoryButton(CategoryType.FIGHT));
        getCategoryComponents().add(new CategoryButton(CategoryType.VISUAL));
        getCategoryComponents().add(new CategoryButton(CategoryType.MOVEMENTS));
        getCategoryComponents().add(new CategoryButton(CategoryType.PLAYER));
        getCategoryComponents().add(new CategoryButton(CategoryType.World));
        getCategoryComponents().add(new CategoryButton(CategoryType.Exploit));

    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int categoryComponentsIntervalY = 0;
        int categoryComponentsInitY = 25;

        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        fontDrawer.drawString("Category",getX() + 4,getY() + categoryComponentsInitY ,new Color(129, 57, 80,128).getRGB());

        for (AbstractControlsComponent categoryComponents : getCategoryComponents()) {
            categoryComponents.setX(getX() + 4);
            categoryComponents.setY(getY() + categoryComponentsIntervalY + categoryComponentsInitY + 15);
            categoryComponents.setWidth(110);
            categoryComponents.setHeight(26);
            categoryComponents.drawScreen(mouseX, mouseY, partialTicks);
            categoryComponentsIntervalY += 35;
        }


        setRequestInterval(categoryComponentsIntervalY);
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractControlsComponent categoryComponents : getCategoryComponents()) {
            if (categoryComponents.isHover(mouseX,mouseY)) {
                La.getINSTANCE().getClickScreen().setCurrentCategory(categoryComponents.getAbstractCategory());
                this.refreshModule(categoryComponents.getAbstractCategory());
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
