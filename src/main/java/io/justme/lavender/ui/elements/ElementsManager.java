package io.justme.lavender.ui.elements;

import io.justme.lavender.ui.elements.impl.NotificationElement;
import io.justme.lavender.ui.elements.impl.TargetListElement;
import io.justme.lavender.ui.elements.impl.TitleElement;
import io.justme.lavender.ui.elements.impl.arraylist.GroupCircleArrayListManager;
import io.justme.lavender.ui.elements.impl.legacy.LegacyArrayList;
import io.justme.lavender.utility.interfaces.Manager;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JustMe.
 * @since 2024/3/2
 **/

@Getter
@Setter
public class ElementsManager extends Manager<AbstractElement> {

    private final LegacyArrayList legacyArrayList = new LegacyArrayList();
    private final CircleArrayListElement circleArrayListElement = new CircleArrayListElement();
    private GroupCircleArrayListManager groupCircleArrayListManager;
    private final NotificationElement notificationElement = new NotificationElement();
    private final TargetListElement targetListElement = new TargetListElement();
    private final TitleElement titleElement = new TitleElement();

    public ElementsManager() {

        getElements().addAll(Arrays.asList(
                getLegacyArrayList(),
                getCircleArrayListElement(),
                getTargetListElement(),
                getTitleElement()
        ));

    }
    public void onInitialization() {
        groupCircleArrayListManager = new GroupCircleArrayListManager();
    }

    public void addElement(AbstractElement element) {
        if (getElements().contains(element)) {
            return;
        }

        getElements().add(element);
    }

    public List<AbstractElement> getVisibleElements() {
        return elements.stream()
                .filter(element -> !Minecraft.getMinecraft().gameSettings.showDebugInfo && element.isVisible())
                .collect(Collectors.toList());
    }

    public AbstractElement find(String name) {
        return elements.stream()
                .filter(element -> element.getElementName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
