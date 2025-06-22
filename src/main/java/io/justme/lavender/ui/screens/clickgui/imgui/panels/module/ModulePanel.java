package io.justme.lavender.ui.screens.clickgui.imgui.panels.module;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.imgui.components.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.AbstractPanel;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.module.chill.ModuleButton;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.popup.PopupPanel;
import io.justme.lavender.ui.screens.clickgui.imgui.theme.ThemeColorEnum;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;
import org.lwjglx.input.Mouse;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/6
 **/
@Getter
@Setter
public class ModulePanel extends AbstractPanel {

    private float lastMouseX,lastMouseY;
    private boolean shouldCheckAnimation = false;
    private AbstractControlsComponent componentToRemove;

    public ModulePanel() {
        this.setName("ModulePanel");
    }

    public void FirstAddonModule() {
        for (Module module : La.getINSTANCE().getModuleManager().getElements()) {
            if (module.getName().equalsIgnoreCase("clickgui")) continue;

            La.getINSTANCE().getClickScreen().getModulePanelComponent().add(new ModuleButton(module));
        }
    }

    @Override
    public void initGui() {

    }

    private Module lastModule;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 14, La.getINSTANCE().getTheme().getColor(ThemeColorEnum.PANEL_MODULEPANEL_BACKGROUND));

        int intervalX = 0;
        int intervalY = 0;
        int abstractComponentInitX = 10;
        int abstractComponentInitY = 10;

        int buttonWidth = 110;
        int buttonHeight = 26;

        for (AbstractControlsComponent abstractControlsComponent : La.getINSTANCE().getClickScreen().getModulePanelComponent()) {

            if (abstractControlsComponent instanceof ModuleButton moduleButton) {

                if (intervalX + buttonWidth + 15 > getWidth()) {
                    intervalX = 0;
                    intervalY += buttonHeight + 15;
                }

                float abstractControlsComponentsX = getX() + intervalX + abstractComponentInitX;
                float abstractControlsComponentsY = getY() + intervalY + abstractComponentInitY;

                if (abstractControlsComponent.isDragging() && abstractControlsComponent.getModule() == getLastModule()) {
                    abstractControlsComponent.setX(mouseX - abstractControlsComponent.getDraggingX());
                    abstractControlsComponent.setY(mouseY - abstractControlsComponent.getDraggingY());
                } else if (!abstractControlsComponent.isPoppingUp()) {
                    if (La.getINSTANCE().getClickScreen().isDragging()) {
                        abstractControlsComponent.setX(abstractControlsComponentsX);
                        abstractControlsComponent.setY(abstractControlsComponentsY);
                        moduleButton.getModuleButtonXAnimation().setValue(abstractControlsComponentsX);
                        moduleButton.getModuleButtonYAnimation().setValue(abstractControlsComponentsY);
                    } else {
                        abstractControlsComponent.setX(moduleButton.getModuleButtonXAnimation().getValue());
                        abstractControlsComponent.setY(moduleButton.getModuleButtonYAnimation().getValue());
                        moduleButton.getModuleButtonXAnimation().animate(abstractControlsComponentsX, 0.1f);
                        moduleButton.getModuleButtonYAnimation().animate(abstractControlsComponentsY, 0.1f);
                    }
                }


                if (Mouse.isButtonDown(0)) {
                    if (abstractControlsComponent.isHover(mouseX, mouseY) && getLastModule() == abstractControlsComponent.getModule()) {
                        if (abstractControlsComponent.getClickedTimerUtility().hasTimeElapsed(100)) {
                            abstractControlsComponent.setDraggingX(mouseX - abstractControlsComponent.getX());
                            abstractControlsComponent.setDraggingY(mouseY - abstractControlsComponent.getY());
                            abstractControlsComponent.setDragging(true);
                        }
                    }
                }



                abstractControlsComponent.setWidth(buttonWidth);
                abstractControlsComponent.setHeight(buttonHeight);

                abstractControlsComponent.drawScreen(mouseX, mouseY, partialTicks);
                intervalX += buttonWidth + 15;

                moduleButton.getModuleButtonYAnimation().update();
                moduleButton.getModuleButtonXAnimation().update();
            }

            if (shouldCheckAnimation && componentToRemove != null) {
                if (componentToRemove.getPopUpAnimation().isDone()) {

                    La.getINSTANCE().getClickScreen().getModulePanelComponent().remove(componentToRemove);
                    PopupPanel popUpPanel = new PopupPanel(componentToRemove.getModule());

                    popUpPanel.setX(componentToRemove.getX());
                    popUpPanel.setY(componentToRemove.getY());
                    La.getINSTANCE().getClickScreen().getAbstractPanels().add(popUpPanel);

                    shouldCheckAnimation = false;
                    componentToRemove = null;
                }
            }

        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        switch (mouseButton) {
            case 0 -> {
                for (AbstractControlsComponent abstractControlsComponent : La.getINSTANCE().getClickScreen().getModulePanelComponent()) {

                    if (abstractControlsComponent.isHover(mouseX, mouseY)) {
                        setLastModule(abstractControlsComponent.getModule());
                    }

                    abstractControlsComponent.mouseClicked(mouseX, mouseY, mouseButton);
                    abstractControlsComponent.getClickedTimerUtility().reset();
                }
            }

//            case 1 -> La.getINSTANCE().getClickScreen().getModulePanelComponent().stream()
//                    .filter(abstractControlsComponents -> abstractControlsComponents.isHover(mouseX, mouseY))
//                    .forEach(abstractControlsComponents -> {
//                        Module module = abstractControlsComponents.getModule();
//                        La.getINSTANCE().getClickScreen().getComponents().add(new SubScreenPanel(module));
//                    });
        }

        setLastMouseX(mouseX);
        setLastMouseY(mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        switch (state) {
            case 0 -> {
                for (AbstractControlsComponent abstractControlsComponent : La.getINSTANCE().getClickScreen().getModulePanelComponent()) {
                    if (abstractControlsComponent.isHover(mouseX, mouseY) && getLastMouseX() == mouseX && getLastMouseY() == mouseY) {
                        Module module = abstractControlsComponent.getModule();
                        La.getINSTANCE().getModuleManager().getModuleByName(module.getName()).setStatus(!module.isToggle());
                    }

                    if (abstractControlsComponent.isDragging()) {
                        abstractControlsComponent.setDragging(false);

                        if (abstractControlsComponent.getX() < La.getINSTANCE().getClickScreen().getX() ||
                                abstractControlsComponent.getX() > La.getINSTANCE().getClickScreen().getX() + La.getINSTANCE().getClickScreen().getWidth() ||
                                abstractControlsComponent.getY() < La.getINSTANCE().getClickScreen().getY() ||
                                abstractControlsComponent.getY() > La.getINSTANCE().getClickScreen().getY() + La.getINSTANCE().getClickScreen().getHeight()) {
                            abstractControlsComponent.setShouldPop(true);
                        }
                    }

                    if (abstractControlsComponent.isShouldPop()) {
                        abstractControlsComponent.setShouldPop(false);
                        abstractControlsComponent.setPoppingUp(true);
                        shouldCheckAnimation = true;
                        componentToRemove = abstractControlsComponent;
                    }

                    abstractControlsComponent.getClickedTimerUtility().reset();

                    abstractControlsComponent.mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
