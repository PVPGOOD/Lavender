package io.justme.lavender.ui.elements.impl.arraylist;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.elements.AbstractElement;
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
public class GroupCircleArrayListManager  {

    public CopyOnWriteArrayList<AbstractPopUp> popUps = new CopyOnWriteArrayList<>();

    public GroupCircleArrayListManager() {

    }

    public void onInitialization() {
        int totalSize = 0;
        for (AbstractElement element : La.getINSTANCE().getElementsManager().getElements()) {
            if (element instanceof GroupCircleArrayList groupCircleArrayList) {
                totalSize += groupCircleArrayList.getCircleComponents().size();
            }
        }

        if (totalSize != La.getINSTANCE().getModuleManager().getElements().size()) {

            La.getINSTANCE().getElementsManager().getElements().removeIf(element -> element instanceof GroupCircleArrayList);
            La.getINSTANCE().getConfigsManager().getElementsConfigs().save();


            var groupCircleArrayList = new GroupCircleArrayList("CircleArrayList_Group");

            for (Module module : La.getINSTANCE().getModuleManager().getElements()) {
                groupCircleArrayList.getCircleComponents().add(new CircleComponent(groupCircleArrayList, module));
            }

            La.getINSTANCE().getElementsManager().addElement(groupCircleArrayList);
        }
    }


    public void draw(float partialTicks, int mouseX, int mouseY) {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractPopUp abstractPopUp : getPopUps()) {
            abstractPopUp.mouseClicked(mouseX,mouseY,mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractPopUp popUp : getPopUps()) {
            if (!popUp.mouseReleased(mouseX, mouseY, state)) continue;
            var popUps = getPopUps();
            var groupList = La.getINSTANCE().getElementsManager().getElements();
            boolean added = false;

            for (AbstractElement group : groupList) {
                if (group instanceof GroupCircleArrayList groupCircle && group.isHover(mouseX, mouseY)) {
                    groupCircle.addCircleComponent(new CircleComponent(groupCircle, popUp.getModule()));
                    added = true;
                    break;
                }
            }

            if (!added) {
                var newGroup = new GroupCircleArrayList("CircleArrayList_Group" + System.currentTimeMillis());
                newGroup.setPosX(popUp.getX());
                newGroup.setPosY(popUp.getY());

                CircleComponent circleComponent = new CircleComponent(newGroup, popUp.getModule());

                newGroup.addCircleComponent(circleComponent);
                groupList.add(newGroup);
            }

            popUps.remove(popUp);
        }
    }

}
