package io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar;

import io.justme.lavender.ui.screens.clickgui.dropdown.AbstractPanelUI;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.impl.category.NavCategoryButton;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.impl.hamburger.NavHamburgerButton;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.impl.setting.NavSettingButton;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.navbar.impl.setting.NavSettingButtonType;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.animation.Animation;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2025/5/7
 **/
@Getter
@Setter
public class NavBarPanel extends AbstractPanelUI {

    private boolean expanded = false;
    private ScaledResolution scaledResolution;
    private ArrayList<AbstractNavBar> elements = new ArrayList<>();

    public NavBarPanel() {
        getElements().add(new NavHamburgerButton());

        getElements().add(new NavSettingButton(NavSettingButtonType.SETTING_BUTTON));
        getElements().add(new NavCategoryButton(CategoryType.FIGHT));
        getElements().add(new NavCategoryButton(CategoryType.VISUAL));
        getElements().add(new NavCategoryButton(CategoryType.MOVEMENTS));
        getElements().add(new NavCategoryButton(CategoryType.PLAYER));
        getElements().add(new NavCategoryButton(CategoryType.MISC));
        getElements().add(new NavCategoryButton(CategoryType.World));
        getElements().add(new NavCategoryButton(CategoryType.Exploit));
        getElements().add(new NavSettingButton(NavSettingButtonType.DESIGN_BUTTON));
    }

    @Override
    public void initGui() {
        setScaledResolution(new ScaledResolution(Minecraft.getMinecraft()));

        setX(50);
        //这里Y应该是居中的
        setY(getScaledResolution().getScaledHeight() / 2f - 200);
    }

    private final Animation expandedAnimations = new Animation();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(), 25, new java.awt.Color(243, 237, 247));

        var initY = 16;
        var interval = 0;
        for (AbstractNavBar element : getElements()) {

            switch (element.getType()) {
                case HAMBURGER -> {
                    element.setX(getX() + getWidth() / 2f - element.getWidth() /2f);
                    element.setY(getY() + initY + interval);
                }

                case SETTING -> {
                    element.setX(getX() + getWidth() / 2f - element.getWidth() /2f);
                    element.setY(getY() + initY + interval + element.getRequestHeight());
                }

                case CATEGORY -> {
                    element.setX(getX() + getWidth() / 2f - element.getWidth() /2f);
                    element.setY(getY() + initY + interval + element.getRequestHeight());
                }
            }
            interval += 35;


            OGLUtility.scissor(getX(),getY(),getWidth(),getHeight() - 5 ,()-> {
                element.drawScreen(mouseX, mouseY, partialTicks);
            });
        }

        getExpandedAnimations().animate(isExpanded() ? interval + 32 : 55,0.1f);

        setHeight(getExpandedAnimations().getValue());
        setWidth(50);

        getExpandedAnimations().update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractNavBar element : getElements()) {
            if (element.isHover(mouseX, mouseY)) {
                switch (element.getType()) {
                    case HAMBURGER -> {
                        setExpanded(!isExpanded());
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractNavBar element : getElements()) {
            element.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }
}
