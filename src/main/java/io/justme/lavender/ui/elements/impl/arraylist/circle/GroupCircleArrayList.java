package io.justme.lavender.ui.elements.impl.arraylist.circle;

import io.justme.lavender.La;
import io.justme.lavender.ui.elements.AbstractElement;
import io.justme.lavender.ui.elements.impl.arraylist.circle.components.impl.CircleComponent;
import io.justme.lavender.ui.elements.impl.arraylist.circle.popup.AbstractPopUp;
import io.justme.lavender.ui.elements.impl.arraylist.circle.popup.impl.CirclePopUp;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/24
 **/
@Getter
@Setter
public class GroupCircleArrayList extends AbstractElement {

    private float x,y,width,height;

    public GroupCircleArrayList(String label) {
        super(label);
    }

    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {

        var interval = 0;

        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(), 0.1f,new Color(0,0,0,32));
        La.getINSTANCE().getFontManager().getPingFang_Bold22().drawString(getLabel(),getX() + getWidth()/2f,getY(),-1);

        for (CircleComponent circleComponent : getCircleComponents()) {

            if (!circleComponent.getModule().isToggle()) continue;

            var pingFangMedium18 = La.getINSTANCE().getFontManager().getPingFang_Medium18();
            
            circleComponent.setFontDrawer(pingFangMedium18);
            circleComponent.setX(getX());
            circleComponent.setY(getY() + interval);
            circleComponent.draw(mouseX, mouseY);
            interval += 25;
        }

        for (AbstractPopUp popUp : La.getINSTANCE().getElementsManager().getGroupCircleArrayListManager().getPopUps()) {

            if (popUp.isDragging()) {
                popUp.setX(mouseX - popUp.getWidth() / 2);
                popUp.setY(mouseY - popUp.getHeight() / 2);
            } else {
                popUp.setX(getX() + getWidth() / 2 - popUp.getWidth() / 2);
                popUp.setY(getY() + getHeight() / 2 - popUp.getHeight() / 2);
            }

            popUp.draw(mouseX, mouseY);
        }


        setX(getPosX());
        setY(getPosY());
        setHeight(interval);
        setWidth(100);
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        boolean componentClicked = false;

        for (CircleComponent circleComponent : getCircleComponents()) {
            if (!circleComponent.mouseClicked(mouseX, mouseY, mouseButton)) continue;

            componentClicked = true;

            circleComponent.getClickedTimerUtility().reset();
            circleComponent.setDragging(false);

            if (mouseButton != 0 || !circleComponent.isHover(mouseX, mouseY)) continue;

            circleComponent.setDragging(true);

            var circlePopUp = getCirclePopUp(circleComponent);
            La.getINSTANCE().getElementsManager()
                    .getGroupCircleArrayListManager()
                    .getPopUps()
                    .add(circlePopUp);

            circleComponent.getAbstractGroup()
                    .getCircleComponents()
                    .remove(circleComponent);
        }

        if (!componentClicked && MouseUtility.isHovering(getPosX(), getPosY(), getWidth(), 20, mouseX, mouseY)) {
            setDragging(true);
        }

        if (getCircleComponents().isEmpty()) {
            La.getINSTANCE().getElementsManager().getElements().remove(this);

            var elementsConfigs = La.getINSTANCE().getConfigsManager().getElementsConfigs();
            if (elementsConfigs.has(getElementName())) {
                elementsConfigs.remove(getElementName());
                elementsConfigs.save();
            }
        }

    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        setDragging(false);
    }

    private CirclePopUp getCirclePopUp(CircleComponent circleComponent) {
        var circlePopUp = new CirclePopUp(circleComponent.getModule());
        circlePopUp.setX(getX());
        circlePopUp.setY(getY());
        circlePopUp.setDragging(true);
        return circlePopUp;
    }


    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    public void addCircleComponent(CircleComponent circleComponent) {
        getCircleComponents().add(circleComponent);
    }
}
