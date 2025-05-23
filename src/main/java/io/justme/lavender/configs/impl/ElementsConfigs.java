package io.justme.lavender.configs.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.justme.lavender.La;
import io.justme.lavender.configs.AbstractConfigs;
import io.justme.lavender.configs.ConfigsEnum;
import io.justme.lavender.ui.elements.AbstractElement;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.nio.file.Files;

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

        for (AbstractElement drag : La.getINSTANCE().getElementsManager().getElements()){
            if (jsonObject.has(drag.getElementName().toString())){
                var moduleJsonObject =  jsonObject.get(drag.getElementName().toString()).getAsJsonObject();

                if (moduleJsonObject.has("X")) {
                    drag.setPosX(moduleJsonObject.get("X").getAsInt());
                }

                if (moduleJsonObject.has("Y")) {
                    drag.setPosY(moduleJsonObject.get("Y").getAsInt());
                }

                if (moduleJsonObject.has("DragX")) {
                    drag.setDraggingX(moduleJsonObject.get("DragX").getAsInt());
                }


                if (moduleJsonObject.has("DragX")) {
                    drag.setDraggingY(moduleJsonObject.get("DragY").getAsInt());
                }

            }
        }
    }

    @SneakyThrows
    @Override
    public void save() {
        var jsonObject = new JsonObject();
        for (AbstractElement drag : La.getINSTANCE().getElementsManager().getElements()){
            var dragJsonObject = new JsonObject();

            dragJsonObject.addProperty("X",drag.getPosX());
            dragJsonObject.addProperty("Y",drag.getPosY());
            dragJsonObject.addProperty("DragX",drag.getDraggingX());
            dragJsonObject.addProperty("DragY",drag.getDraggingY());

            jsonObject.add(drag.getElementName().name(),dragJsonObject);
        }
        Files.writeString(getFilesPath(), new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
    }


}
