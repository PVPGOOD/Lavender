package io.justme.lavender.ui.screens.configscreen.frame.impl.button;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.configscreen.frame.impl.AbstractComponents;
import io.justme.lavender.ui.screens.configscreen.frame.impl.button.components.impl.CheckBoxComponents;
import io.justme.lavender.ui.screens.configscreen.AbstractConfigFrame;
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
public class ConfigButtonFrame extends AbstractConfigFrame {

    private ArrayList<AbstractComponents> componentsArrayList = new ArrayList<>();

    public ConfigButtonFrame() {
        super("ButtonFrame");

        //必须的组件
        getComponentsArrayList().add(getReloadButton());
        getComponentsArrayList().add(getRefreshButton());
    }

    @Override
    public void initGui() {
        for (AbstractComponents components : getComponentsArrayList()) {
            components.initGui();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRect(getX(),getY(),getWidth(),getHeight(),new Color(0,0,0,128));

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

                var configListFrame = La.getINSTANCE().getConfigListFrame();;

                switch (components.getName()) {

                    case "Reload" -> {
                        La.getINSTANCE().getConfigsManager().load();
                        La.getINSTANCE().print("Reload");
                    }

                    case "Refresh" -> {
                        configListFrame.getComponentsArrayList().clear();
                        configListFrame.FileReader();

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

    public CheckBoxComponents getReloadButton() {
        return new CheckBoxComponents("Reload");
    }

    public CheckBoxComponents getRefreshButton() {
        return new CheckBoxComponents("Refresh");
    }
}
