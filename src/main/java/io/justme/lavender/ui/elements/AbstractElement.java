package io.justme.lavender.ui.elements;

import io.justme.lavender.ui.elements.quadrant.Quadrant;
import io.justme.lavender.ui.elements.quadrant.QuadrantEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/3/2
 **/

@Getter
@Setter
public abstract class AbstractElement {

    private float posX, posY,width,height, draggingX, draggingY;

    private String elementName;

    private boolean dragging;

    public AbstractElement(String elementName) {
        setElementName(elementName);
    }

    public void onDrag(int mouseX, int mouseY){
        if (getDraggingX() == 0 && getDraggingY() == 0) {
            setDraggingX(mouseX - getPosX());
            setDraggingY(mouseY - getPosY());
        } else {
            setPosX(mouseX - getDraggingX());
            setPosY(mouseY - getDraggingY());
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

    public QuadrantEnum getQuadrant() {
        Quadrant quadrant = new Quadrant();
        return quadrant.getQuadrant(posX, posY);
    }
}
