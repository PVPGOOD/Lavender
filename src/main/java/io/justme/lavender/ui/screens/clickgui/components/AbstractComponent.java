package io.justme.lavender.ui.screens.clickgui.components;

import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.MultiBoolValue;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/5
 **/

//parent
@Getter
@Setter
public abstract class AbstractComponent {

    public String name;
    public float x,y,width,height;

    //临时的 问题丢给我以后吧...
    public final MultiBoolValue comBoxOption = new MultiBoolValue("comBox",
            new BoolValue("test", true),
            new BoolValue("test", false),
            new BoolValue("test", false),
            new BoolValue("test", false),
            new BoolValue("test", false),
            new BoolValue("test", false),
            new BoolValue("test", false),
            new BoolValue("test", false),
            new BoolValue("test", false),
            new BoolValue("test", false)
    );

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;

}
