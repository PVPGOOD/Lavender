package io.justme.lavender.ui.screens.clickgui.imgui.panels.category;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.module.ModulePanel;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.module.chill.ModuleButton;
import io.justme.lavender.utility.math.animation.Animation;
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
                .forEach(module -> {
                    var moduleButton = new ModuleButton(module);

                    var modulePanel = La.getINSTANCE().getClickScreen().getAbstractPanels().stream()
                            .filter(panel -> panel instanceof ModulePanel)
                            .findFirst()
                            .orElse(null);

                    if (modulePanel == null) return;

                    moduleButton.setModuleButtonXAnimation(new Animation(modulePanel.getX() + modulePanel.getWidth() / 2f));
                    moduleButton.setModuleButtonYAnimation(new Animation(modulePanel.getY() + modulePanel.getHeight() / 2f));
                    La.getINSTANCE().getClickScreen().getModulePanelComponent().add(moduleButton);
                });
    }

}
