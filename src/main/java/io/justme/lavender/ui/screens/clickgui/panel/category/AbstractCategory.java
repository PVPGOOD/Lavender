package io.justme.lavender.ui.screens.clickgui.panel.category;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.panel.module.chill.ModuleButton;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/4/4
 **/
@Getter
@Setter
//用来管理 category 窗口 类别 例如 模块窗口和 设置窗口等
public abstract class AbstractCategory {

    public String name;
    public float x,y,width,height;
    public float requestInterval;

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
    public abstract void handleMouseInput() throws IOException;


    public void refreshModule(CategoryType category) {
        La.getINSTANCE().getClickScreen().getModulePanelComponent().clear();
        La.getINSTANCE().getModuleManager().getElements().stream()
                .filter(module -> !module.getName().equalsIgnoreCase("clickgui"))
                .filter(module -> module.getCategory().getName().equalsIgnoreCase(category.getName()))
                .forEach(module -> La.getINSTANCE().getClickScreen().getModulePanelComponent().add(new ModuleButton(module)));
    }

}
