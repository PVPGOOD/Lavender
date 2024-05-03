package io.justme.lavender.ui.screens.configscreen.frame;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

@Getter
@Setter
//集合画板的容器 (未来可能不只是 只有一个画板)
public abstract class AbstractConfigFrame {

    private String name;
    private float x,y,width,height;

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
}
