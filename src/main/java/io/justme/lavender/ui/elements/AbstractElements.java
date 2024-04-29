package io.justme.lavender.ui.elements;

import lombok.Getter;
import lombok.Setter;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/3/2
 **/

@Getter
@Setter
public abstract class AbstractElements {

    private float x, y, draggingX, draggingY;

    private ElementsEnum elementsEnum;

    private boolean dragging;

    public AbstractElements(ElementsEnum elementsEnum) {
        setElementsEnum(elementsEnum);
    }

    public void onDrag(int mouseX, int mouseY){
        if (getDraggingX() == 0 && getDraggingY() == 0) {
            setDraggingX(mouseX - getX());
            setDraggingY(mouseY - getY());
        } else {
            setX(mouseX - getDraggingX());
            setY(mouseY - getDraggingY());
        }
    }

    public void reset() {
        setDraggingY(0);
        setDraggingX(0);
    }

    public abstract void draw(float partialTicks, int mouseX, int mouseY);
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;

    public boolean isVisible() {
        return true;
    }
}
