package io.justme.lavender.ui.elements.impl.arraylist;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.elements.AbstractElement;
import io.justme.lavender.ui.elements.impl.arraylist.circle.AbstractGroup;
import io.justme.lavender.ui.elements.impl.arraylist.circle.GroupCircleArrayList;
import io.justme.lavender.ui.elements.impl.arraylist.circle.components.impl.CircleComponent;
import io.justme.lavender.ui.elements.impl.arraylist.circle.popup.AbstractPopUp;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2025/5/24
 **/

//这里负责渲染整个group
@Getter
@Setter
public class GroupCircleArrayListElement extends AbstractElement {

    private float x,y,width,height;

    public CopyOnWriteArrayList<AbstractPopUp> popUps = new CopyOnWriteArrayList<>();


    private CopyOnWriteArrayList<AbstractGroup> abstractGroupArrayList = new CopyOnWriteArrayList<>();


    public GroupCircleArrayListElement() {
        super("");

        GroupCircleArrayList e = new GroupCircleArrayList();

        for (Module module : La.getINSTANCE().getModuleManager().getElements()) {
            e.getCircleComponents().add(new CircleComponent(e,module));
        }

        getAbstractGroupArrayList().add(e);

    }

    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {

        for (AbstractGroup abstractGroup : getAbstractGroupArrayList()) {
            abstractGroup.draw(mouseX,mouseY);
        }

        setWidth(100);
        setHeight(100);
        setX(0);
        setY(0);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractGroup abstractGroup : getAbstractGroupArrayList()) {
            abstractGroup.mouseClicked(mouseX,mouseY,mouseButton);

            var groupList = La.getINSTANCE().getElementsManager().getGroupCircleArrayListElement().getAbstractGroupArrayList();

            if (abstractGroup.getCircleComponents().isEmpty()) {
                groupList.remove(abstractGroup);
            }
        }
        for (AbstractPopUp abstractPopUp  : getPopUps()) {
            abstractPopUp.mouseClicked(mouseX,mouseY,mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractGroup group : getAbstractGroupArrayList()) {
            group.mouseReleased(mouseX, mouseY, state);
        }

        for (AbstractPopUp popUp : getPopUps()) {
            if (!popUp.mouseReleased(mouseX, mouseY, state)) continue;
            var popUps = La.getINSTANCE().getElementsManager().getGroupCircleArrayListElement().getPopUps();
            var groupList = La.getINSTANCE().getElementsManager().getGroupCircleArrayListElement().getAbstractGroupArrayList();
            boolean added = false;

            for (AbstractGroup group : groupList) {
                if (group instanceof GroupCircleArrayList groupCircle && group.isHover(mouseX, mouseY)) {
                    groupCircle.addCircleComponent(new CircleComponent(groupCircle, popUp.getModule()));
                    added = true;
                    break;
                }
            }

            if (!added) {
                GroupCircleArrayList newGroup = new GroupCircleArrayList();
                newGroup.setX(popUp.getX());
                newGroup.setY(popUp.getY());
                newGroup.setWidth(getWidth());
                newGroup.setHeight(getHeight());

                newGroup.addCircleComponent(new CircleComponent(newGroup, popUp.getModule()));
                groupList.add(newGroup);
            }

            popUps.remove(popUp);

        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
