package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.impl.ModuleButton;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.impl.ModuleGroupHeader;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2025/5/8
 **/
//单个模块的列表
@Getter
@Setter
public class ModuleList extends AbstractModulePanel{

    //子组件
    //头部 按钮 等
    private final CopyOnWriteArrayList<AbstractModulePanel> elements = new CopyOnWriteArrayList<>();

    public ModuleList(CategoryType type) {
        super(type);

        getElements().add(new ModuleGroupHeader(type,ModulePanelType.MODULE_GROUP_HEADER));

        for (Module module : La.getINSTANCE().getModuleManager().getElements()) {
            if (!module.getCategory().getName().equalsIgnoreCase(type.getName())) continue;

            getElements().add(new ModuleButton(type,ModulePanelType.MODULE_BUTTON,module));
        }
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 18, new Color(0xF8F2FA));


        var intervalY = 0;
        var initY = 40;
        for (AbstractModulePanel element : elements) {

            switch (element.getPanelType()) {
                case MODULE_GROUP_HEADER -> {
                    element.setX(getX());
                    element.setY(getY());
                    element.setHeight(30);
                    element.setWidth(getWidth());
                }

                case MODULE_BUTTON -> {
                    element.setX(getX() + 5);
                    element.setY(getY() + initY + intervalY);
                    intervalY += 32;
                }
            }


            element.drawScreen(mouseX, mouseY, partialTicks);
        }

        setWidth(120);
        setHeight(300);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        return false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
