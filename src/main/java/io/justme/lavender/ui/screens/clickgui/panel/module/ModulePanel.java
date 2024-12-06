package io.justme.lavender.ui.screens.clickgui.panel.module;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponents;
import io.justme.lavender.ui.screens.clickgui.panel.module.chill.ModuleButton;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2024/12/6
 **/
@Getter
@Setter
public class ModulePanel extends AbstractComponent {

    private final ArrayList<AbstractControlsComponents> component = new ArrayList<>();

    public ModulePanel() {
        this.setName("ModulePanel");

        for (Module module : La.getINSTANCE().getModuleManager().getElements()) {
            getComponent().add(new ModuleButton(module));
        }
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 14, new Color(0xFFE9F0));

        int intervalX = 0;
        int intervalY = 0;
        int abstractComponentInitX = 10;
        int abstractComponentInitY = 10;

        int buttonWidth = 110;
        int buttonHeight = 26;

        for (AbstractControlsComponents abstractControlsComponents : getComponent()) {
            if (intervalX + buttonWidth + 15 > getWidth()) {
                intervalX = 0;
                intervalY += buttonHeight + 15;
            }

            float abstractControlsComponentsX = getX() + intervalX + abstractComponentInitX;
            float abstractControlsComponentsY = getY() + intervalY + abstractComponentInitY;

            abstractControlsComponents.setX(abstractControlsComponentsX);
            abstractControlsComponents.setY(abstractControlsComponentsY);
            abstractControlsComponents.setWidth(buttonWidth);
            abstractControlsComponents.setHeight(buttonHeight);

            abstractControlsComponents.drawScreen(mouseX, mouseY, partialTicks);
            intervalX += buttonWidth + 15;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        switch (mouseButton) {
            case 0 -> {
                for (AbstractControlsComponents abstractControlsComponents : getComponent()) {
                    if (abstractControlsComponents.isHover(mouseX, mouseY)) {
                        Module module = abstractControlsComponents.getModule();
                        La.getINSTANCE().getModuleManager().getModuleByName(module.getName()).setToggle(!module.isToggle());
                    }
                }
            }

            case 1 -> {

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
