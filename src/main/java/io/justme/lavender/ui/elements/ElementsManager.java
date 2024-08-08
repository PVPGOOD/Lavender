package io.justme.lavender.ui.elements;

import io.justme.lavender.ui.elements.impl.ArrayList;
import io.justme.lavender.ui.elements.impl.Notifications;
import io.justme.lavender.ui.elements.impl.TargetList;
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
public class ElementsManager extends Manager<AbstractElements> {

    private final ArrayList arrayList = new ArrayList();
    private final Notifications notifications = new Notifications();
    private final TargetList targetList = new TargetList();

    public ElementsManager() {

        getElements().addAll(Arrays.asList(
                getArrayList(),
                getNotifications(),
                getTargetList()
        ));
    }

    public List<AbstractElements> getVisibleElements() {
        return elements.stream()
                .filter(element -> !Minecraft.getMinecraft().gameSettings.showDebugInfo && element.isVisible())
                .collect(Collectors.toList());
    }

    public AbstractElements find(String name) {
        return elements.stream()
                .filter(element -> element.getElementsEnum().name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
