package io.justme.lavender.ui.elements.impl.arraylist.circle;

import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.elements.impl.arraylist.circle.components.impl.CircleComponent;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2025/5/24
 **/
@Getter

@Setter
public abstract class AbstractGroup {

    private String label;
    //module
    private CopyOnWriteArrayList<CircleComponent> circleComponents = new CopyOnWriteArrayList<>();
    private float x,y,width,height;
    private boolean dragging;
    private float draggingX, draggingY;
    private float index;
    private FontDrawer fontDrawer;

    public AbstractGroup(String label) {
        this.label = label;
    }

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void draw(int mouseX, int mouseY);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract boolean mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
}
