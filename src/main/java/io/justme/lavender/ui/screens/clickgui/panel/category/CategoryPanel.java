package io.justme.lavender.ui.screens.clickgui.panel.category;

import io.justme.lavender.La;
import io.justme.lavender.module.Category;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponents;
import io.justme.lavender.ui.screens.clickgui.panel.category.chill.CategoryIcon;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2024/12/5
 **/
@Getter
public class CategoryPanel extends AbstractComponent {

    private final ArrayList<AbstractControlsComponents> component = new ArrayList<>();

    public CategoryPanel() {
        this.setName("CategoryPanel");

        getComponent().add(new CategoryIcon(Category.FIGHT));
        getComponent().add(new CategoryIcon(Category.VISUAL));
        getComponent().add(new CategoryIcon(Category.MOVEMENTS));
        getComponent().add(new CategoryIcon(Category.PLAYER));
        getComponent().add(new CategoryIcon(Category.World));
        getComponent().add(new CategoryIcon(Category.Exploit));
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        //背景
        RenderUtility.drawRoundRectWithCustomRounded(
                //为什么不靠边呢
                getX() - 1 ,
                getY(),
                getWidth(),
                getHeight(),
                new Color(253, 235, 241),28,0,0,0);

        //分类图标
        int intervalY = 0;
        int abstractComponentInitY = 10;
        for (AbstractControlsComponents abstractControlsComponents : getComponent()) {
            abstractControlsComponents.setX(getX() + 4);
            abstractControlsComponents.setY(getY() + intervalY + abstractComponentInitY);
            abstractControlsComponents.setWidth(110);
            abstractControlsComponents.setHeight(26);

            abstractControlsComponents.getCategoryTypeBackgroundAlpha().animate(!abstractControlsComponents.isHover(mouseX,mouseY) ? 0 : 255,0.1f, Easings.LINEAR);
            abstractControlsComponents.getCategoryTypeBackgroundAlpha().update();

            abstractControlsComponents.drawScreen(mouseX, mouseY, partialTicks);
            intervalY += 26;
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractControlsComponents abstractControlsComponents : getComponent()) {
            if (abstractControlsComponents.isHover(mouseX,mouseY)) {
                La.getINSTANCE().getClickScreen().setCurrentCategory(abstractControlsComponents.getAbstractCategory());
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
