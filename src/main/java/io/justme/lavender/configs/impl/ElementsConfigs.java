package io.justme.lavender.configs.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.justme.lavender.La;
import io.justme.lavender.configs.AbstractConfigs;
import io.justme.lavender.configs.ConfigsEnum;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.elements.AbstractElement;
import io.justme.lavender.ui.elements.impl.arraylist.circle.GroupCircleArrayList;
import io.justme.lavender.ui.elements.impl.arraylist.circle.components.impl.CircleComponent;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

/**
 * @author PVP_GOOD_
 * @since 2023/8/25 14:51
 */
@Getter
@Setter
public class ElementsConfigs extends AbstractConfigs {

    public ElementsConfigs() {
        setFileName(ConfigsEnum.ELEMENTS);
    }

    @SneakyThrows
    @Override
    public void load() {
        var jsonObject = new Gson().fromJson(Files.readString(getFilesPath()), JsonObject.class);

        for (AbstractElement abstractElement : La.getINSTANCE().getElementsManager().getElements()){


            if (jsonObject.has(abstractElement.getElementName())){
                var moduleJsonObject = jsonObject.get(abstractElement.getElementName()).getAsJsonObject();

                if (moduleJsonObject.has("X")) {
                    abstractElement.setPosX(moduleJsonObject.get("X").getAsInt());
                }

                if (moduleJsonObject.has("Y")) {
                    abstractElement.setPosY(moduleJsonObject.get("Y").getAsInt());
                }

                if (moduleJsonObject.has("DragX")) {
                    abstractElement.setDraggingX(moduleJsonObject.get("DragX").getAsInt());
                }


                if (moduleJsonObject.has("DragX")) {
                    abstractElement.setDraggingY(moduleJsonObject.get("DragY").getAsInt());
                }


                // 处理以 CircleArrayList_Group 开头的配置
                Set<Module> addedModules = new HashSet<>();

                La.getINSTANCE().getElementsManager().getElements().removeIf(element -> element instanceof GroupCircleArrayList);

                for (String key : jsonObject.keySet()) {
                    if (key.startsWith("CircleArrayList_Group")) {
                        var groupJsonObject = jsonObject.getAsJsonObject(key);
                        var group = new GroupCircleArrayList(key);
                        group.setPosX(groupJsonObject.get("X").getAsInt());
                        group.setPosY(groupJsonObject.get("Y").getAsInt());

                        if (groupJsonObject.has("CircleComponents")) {
                            var circleComponentsArray = groupJsonObject.getAsJsonArray("CircleComponents");
                            for (var element : circleComponentsArray) {
                                var moduleName = element.getAsString();
                                var module = La.getINSTANCE().getModuleManager().getModuleByName(moduleName);

                                if (module != null && !addedModules.contains(module)) {
                                    group.getCircleComponents().add(new CircleComponent(group, module));
                                    addedModules.add(module);
                                }
                            }
                        }

                        // 保证唯一性：只添加一次 group
                        if (La.getINSTANCE().getElementsManager().getElements().stream().noneMatch(e -> e.getElementName().equals(group.getElementName()))) {
                            La.getINSTANCE().getElementsManager().addElement(group);
                        }
                    }
                }
            }
        }
    }

    @SneakyThrows
    @Override
    public void save() {
        var jsonObject = new JsonObject();
        for (AbstractElement abstractElement : La.getINSTANCE().getElementsManager().getElements()){
            var dragJsonObject = new JsonObject();

            dragJsonObject.addProperty("X", abstractElement.getPosX());
            dragJsonObject.addProperty("Y", abstractElement.getPosY());
            dragJsonObject.addProperty("DragX", abstractElement.getDraggingX());
            dragJsonObject.addProperty("DragY", abstractElement.getDraggingY());

            var circleComponentsArray = new JsonArray();
            for (CircleComponent circleComponent : abstractElement.getCircleComponents()) {
                circleComponentsArray.add(circleComponent.getModule().getName());
            }
            dragJsonObject.add("CircleComponents", circleComponentsArray);
            jsonObject.add(abstractElement.getElementName(),circleComponentsArray);


            jsonObject.add(abstractElement.getElementName(),dragJsonObject);
        }
        Files.writeString(getFilesPath(), new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
    }


}
