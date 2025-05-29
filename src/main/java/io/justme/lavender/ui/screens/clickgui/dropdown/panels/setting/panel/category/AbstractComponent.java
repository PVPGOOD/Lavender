package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.category;

import io.justme.lavender.utility.math.animation.Animation;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/26
 **/
@Getter
@Setter
//有点多余 有空再整理代码(如果主动开源的话? 2025/5/26 16:45 XD)
public abstract class AbstractComponent {

    private final Animation categoryTypeBackgroundAlpha = new Animation(16);
    public CategoryType abstractCategory;

    public AbstractComponent(CategoryType abstractCategory) {
        this.abstractCategory = abstractCategory;
    }

    public float x,y,width,height;

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
    public abstract void handleMouseInput() throws IOException;

}
