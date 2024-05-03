package io.justme.lavender.ui.screens.configscreen.frame;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.configscreen.frame.components.AbstractComponents;
import io.justme.lavender.ui.screens.configscreen.frame.components.ComponentsEnum;
import io.justme.lavender.ui.screens.configscreen.frame.components.impl.CheckBox;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

@Getter
@Setter
public class ConfigFrame extends AbstractConfigFrame{

    private ArrayList<AbstractComponents> componentsArrayList = new ArrayList<>();

    public ConfigFrame() {
        getComponentsArrayList().add(getLoadButton());
        getComponentsArrayList().add(getReloadButton());
        getComponentsArrayList().add(getRefreshButton());
        getComponentsArrayList().add(getAddButton());
    }

    @Override
    public void initGui() {
        for (AbstractComponents components : getComponentsArrayList()) {
            components.initGui();
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRect(getX(),getY(),getWidth(),40,new Color(0,0,0,128));

        float leftY = getHeight(), rightY = getHeight(), index = 0;
        for (AbstractComponents components : getComponentsArrayList()) {

            if (index % 2 == 0) {
                leftY -= 25;
            } else {
                rightY -= 25;
            }

            components.setX(getX() + (index % 2 == 0 ? 5 : 90));
            components.setY(getY() + (index % 2 == 0 ? leftY : rightY));

            components.drawScreen(mouseX, mouseY, partialTicks);
            index++;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractComponents components : getComponentsArrayList()) {
            if (components.mouseClicked(mouseX, mouseY, mouseButton)) {
                switch (components.getComponentsEnum()) {
                    case LOAD -> {
                        La.getINSTANCE().print("Load");
                    }

                    case RELOAD -> {
                        La.getINSTANCE().print("Reload");
                    }

                    case ADD -> {
                        La.getINSTANCE().print("Add");
                    }

                    case REFRESH -> {
                        La.getINSTANCE().print("Refresh");
                    }
                }

            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractComponents components : getComponentsArrayList()) {
            components.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for (AbstractComponents components : getComponentsArrayList()) {
            components.keyTyped(typedChar, keyCode);
        }
    }

    public CheckBox getLoadButton() {
        return new CheckBox(ComponentsEnum.LOAD);
    }

    public CheckBox getReloadButton() {
        return new CheckBox(ComponentsEnum.RELOAD);
    }

    public CheckBox getAddButton() {
        return new CheckBox(ComponentsEnum.ADD);
    }

    public CheckBox getRefreshButton() {
        return new CheckBox(ComponentsEnum.REFRESH);
    }
}
