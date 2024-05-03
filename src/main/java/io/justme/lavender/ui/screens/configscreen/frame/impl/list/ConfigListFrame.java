package io.justme.lavender.ui.screens.configscreen.frame.impl.list;

import io.justme.lavender.ui.screens.configscreen.frame.impl.AbstractComponents;
import io.justme.lavender.ui.screens.configscreen.frame.impl.list.components.ListComponents;
import io.justme.lavender.ui.screens.configscreen.AbstractConfigFrame;
import io.justme.lavender.utility.gl.OGLUtility;
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
public class ConfigListFrame extends AbstractConfigFrame {

    private ArrayList<AbstractComponents> componentsArrayList = new ArrayList<>();

    public ConfigListFrame() {
        super("ListFrame");

        getComponentsArrayList().add(new ListComponents("Test Configs Name"));
        getComponentsArrayList().add(new ListComponents("Test Configs Name"));
        getComponentsArrayList().add(new ListComponents("Test Configs Name"));
        getComponentsArrayList().add(new ListComponents("Test Configs Name"));
        getComponentsArrayList().add(new ListComponents("Test Configs Name"));
        getComponentsArrayList().add(new ListComponents("Test Configs Name"));


    }

    @Override
    public void initGui() {
        for (AbstractComponents components : getComponentsArrayList()) {
            components.initGui();
        }

    }

    private float interval = 0;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        //配置列表

        RenderUtility.drawRect(getX(),getY(),getWidth(),100,new Color(0,0,0,128));

        //绘制组件
        OGLUtility.scissor(getX(),getY(),getWidth(),100, () -> {
            float interval = 0;
            for (AbstractComponents components : getComponentsArrayList()) {
                components.setX(getX());
                components.setY(getY() + interval);
                components.setWidth(getWidth());
                components.setHeight(15);
                interval += 25;

                setInterval(interval);
                components.drawScreen(mouseX, mouseY, partialTicks);
                setHeight(getInterval());
            }
        });
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractComponents components : getComponentsArrayList()) {
            components.mouseClicked(mouseX, mouseY,mouseButton);
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

}
