package io.justme.lavender.ui.screens.clickgui.panel.category;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.module.Category;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.panel.category.chill.CategoryModuleButton;
import io.justme.lavender.ui.screens.clickgui.panel.category.chill.CategoryOtherButton;
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
public class CategoryPanel extends AbstractComponent {

    private final CopyOnWriteArrayList<AbstractControlsComponent> categoryComponents = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<AbstractControlsComponent> otherComponents = new CopyOnWriteArrayList<>();

    public CategoryPanel() {
        this.setName("CategoryPanel");

        getCategoryComponents().add(new CategoryModuleButton(Category.FIGHT));
        getCategoryComponents().add(new CategoryModuleButton(Category.VISUAL));
        getCategoryComponents().add(new CategoryModuleButton(Category.MOVEMENTS));
        getCategoryComponents().add(new CategoryModuleButton(Category.PLAYER));
        getCategoryComponents().add(new CategoryModuleButton(Category.World));
        getCategoryComponents().add(new CategoryModuleButton(Category.Exploit));

        getOtherComponents().add(new CategoryOtherButton(CategoryTypes.CLIENT_SETTINGS));
        getOtherComponents().add(new CategoryOtherButton(CategoryTypes.MANAGER_POPPING));
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium18();

        //背景
        RenderUtility.drawRoundRectWithCustomRounded(
                //为什么不靠边呢
                getX() - 1 ,
                getY(),
                getWidth(),
                getHeight(),
                new Color(253, 235, 241),28,0,0,0);

        //分类图标
        //模块分类
        int categoryComponentsIntervalY = 0;
        int categoryComponentsInitY = 25;
        fontDrawer.drawString("Category",getX() + 4,getY() + categoryComponentsInitY - 15,new Color(129, 57, 80,128).getRGB());
        for (AbstractControlsComponent categoryComponents : getCategoryComponents()) {
            categoryComponents.setX(getX() + 4);
            categoryComponents.setY(getY() + categoryComponentsIntervalY + categoryComponentsInitY);
            categoryComponents.setWidth(110);
            categoryComponents.setHeight(26);

            categoryComponents.getCategoryTypeBackgroundAlpha().animate(!categoryComponents.isHover(mouseX,mouseY) ? 0 : 255,0.1f, Easings.LINEAR);
            categoryComponents.getCategoryTypeBackgroundAlpha().update();

            categoryComponents.drawScreen(mouseX, mouseY, partialTicks);
            categoryComponentsIntervalY += 26;
        }

        int categoryOtherComponentsIntervalY = 0;
        int categoryOtherComponentsInitY = categoryComponentsInitY + categoryComponentsIntervalY + 50;
        fontDrawer.drawString("Other",getX() + 4,getY() + categoryOtherComponentsInitY - 15,new Color(129, 57, 80,128).getRGB());
        for (AbstractControlsComponent otherComponents : getOtherComponents()) {
            otherComponents.setX(getX() + 4);
            otherComponents.setY(getY() + categoryOtherComponentsIntervalY + categoryOtherComponentsInitY);
            otherComponents.setWidth(110);
            otherComponents.setHeight(26);

            otherComponents.getCategoryTypeBackgroundAlpha().animate(!otherComponents.isHover(mouseX,mouseY) ? 0 : 255,0.1f, Easings.LINEAR);
            otherComponents.getCategoryTypeBackgroundAlpha().update();

            otherComponents.drawScreen(mouseX, mouseY, partialTicks);
            categoryOtherComponentsIntervalY += 26;
        }



    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractControlsComponent categoryComponents : getCategoryComponents()) {
            if (categoryComponents.isHover(mouseX,mouseY)) {
                La.getINSTANCE().getClickScreen().setCurrentCategory(categoryComponents.getAbstractCategory());
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
