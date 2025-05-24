package io.justme.lavender.ui.elements.impl.arraylist.circle.components;

import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.elements.AbstractElement;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.math.animation.Animation;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/13
 **/
@Getter
@Setter

//这里负责规范化每个Group的基本属性和方法
public abstract class AbstractComponent {

    private final TimerUtility clickedTimerUtility = new TimerUtility();
    private float x,y,width,height;
    private boolean dragging;
    private float index;
    public Module module;
    private final Animation popUpAnimation = new Animation(1);
    private FontDrawer fontDrawer;

    //属于哪个组
    public AbstractElement abstractGroup;

    public AbstractComponent(AbstractElement abstractGroup, Module module) {
        this.abstractGroup = abstractGroup;
        this.module = module;
    }

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void draw(int mouseX, int mouseY);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;

}
