package io.justme.lavender.ui.screens.configscreen.frame.components;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

@Getter
@Setter
//集合组件的容器
public abstract class AbstractComponents {

    private float x,y,width,height;
    private String name;
    private ComponentsEnum type;

    public AbstractComponents(String name) {
        this.name = name;
    }

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
}
