package io.justme.lavender.ui.elements.impl.arraylist.circle;

import io.justme.lavender.La;
import io.justme.lavender.ui.elements.impl.arraylist.circle.components.impl.CircleComponent;
import io.justme.lavender.ui.elements.impl.arraylist.circle.popup.AbstractPopUp;
import io.justme.lavender.ui.elements.impl.arraylist.circle.popup.impl.CirclePopUp;
import io.justme.lavender.utility.gl.RenderUtility;
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
public class GroupCircleArrayList extends AbstractGroup {
    
    public GroupCircleArrayList() {
        super("");
        this.setLabel("" + hashCode());
    }

    @Override
    public void draw(int mouseX, int mouseY) {
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

        for (AbstractPopUp popUp : La.getINSTANCE().getElementsManager().getGroupCircleArrayListElement().getPopUps()) {

            if (popUp.isDragging()) {
                popUp.setX(mouseX - popUp.getWidth() / 2);
                popUp.setY(mouseY - popUp.getHeight() / 2);
            } else {
                popUp.setX(getX() + getWidth() / 2 - popUp.getWidth() / 2);
                popUp.setY(getY() + getHeight() / 2 - popUp.getHeight() / 2);
            }

            popUp.draw(mouseX, mouseY);
        }

        setHeight(interval);
        setWidth(100);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (CircleComponent circleComponent : getCircleComponents()) {
            if (circleComponent.mouseClicked(mouseX, mouseY, mouseButton)) {
                if (mouseButton == 0) {
                    if (circleComponent.isHover(mouseX, mouseY)) {
                        circleComponent.setDragging(true);

                        var circlePopUp = getCirclePopUp(circleComponent);

                        La.getINSTANCE().getElementsManager().getGroupCircleArrayListElement().getPopUps().add(circlePopUp);
                        circleComponent.getAbstractGroup().getCircleComponents().remove(circleComponent);
                    }
                }

                circleComponent.getClickedTimerUtility().reset();
                circleComponent.setDragging(false);
            }
        }
        return false;

    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        return isHover(mouseX, mouseY);
    }

    private CirclePopUp getCirclePopUp(CircleComponent circleComponent) {
        var circlePopUp = new CirclePopUp(circleComponent.getModule());
        circlePopUp.setX(getX());
        circlePopUp.setY(getY());
        circlePopUp.setFontDrawer(getFontDrawer());
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
