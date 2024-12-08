package io.justme.lavender.ui.screens.clickgui.panel.module;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.panel.module.chill.ModuleButton;
import io.justme.lavender.ui.screens.clickgui.panel.popupscreen.PopupScreen;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;
import org.lwjglx.input.Mouse;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/6
 **/
@Getter
@Setter
public class ModulePanel extends AbstractComponent {

    private boolean shouldCheckAnimation = false;
    private AbstractControlsComponent componentToRemove;

    public ModulePanel() {
        this.setName("ModulePanel");
    }

    public void afterAddOptions() {
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
        RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 14, new Color(0xFFE9F0));

        int intervalX = 0;
        int intervalY = 0;
        int abstractComponentInitX = 10;
        int abstractComponentInitY = 10;

        int buttonWidth = 110;
        int buttonHeight = 26;

        for (AbstractControlsComponent abstractControlsComponent : La.getINSTANCE().getClickScreen().getModulePanelComponent()) {

            if (intervalX + buttonWidth + 15 > getWidth()) {
                intervalX = 0;
                intervalY += buttonHeight + 15;
            }

            float abstractControlsComponentsX = getX() + intervalX + abstractComponentInitX;
            float abstractControlsComponentsY = getY() + intervalY + abstractComponentInitY;

            if (abstractControlsComponent.isDragging() && abstractControlsComponent.getModule() == getLastModule()) {
                abstractControlsComponent.setX(mouseX - abstractControlsComponent.getDraggingX());
                abstractControlsComponent.setY(mouseY - abstractControlsComponent.getDraggingY());
            } else {
                if (!abstractControlsComponent.isPoppingUp()) {
                    abstractControlsComponent.setX(abstractControlsComponentsX);
                    abstractControlsComponent.setY(abstractControlsComponentsY);
                }
            }


            if (Mouse.isButtonDown(0)) {
                if (abstractControlsComponent.isHover(mouseX, mouseY) && getLastModule() == abstractControlsComponent.getModule()) {
                    if (abstractControlsComponent.getClickedTimerUtility().hasTimeElapsed(120)) {
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
        }

        if (shouldCheckAnimation && componentToRemove != null) {
            if (componentToRemove.getPopUpAnimation().isDone()) {

                La.getINSTANCE().getClickScreen().getModulePanelComponent().remove(componentToRemove);
                PopupScreen popUpScreen = new PopupScreen(componentToRemove.getModule());

                popUpScreen.setX(componentToRemove.getX());
                popUpScreen.setY(componentToRemove.getY());
                La.getINSTANCE().getClickScreen().getComponents().add(popUpScreen);

                shouldCheckAnimation = false;
                componentToRemove = null;
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
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        switch (state) {
            case 0 -> {
                for (AbstractControlsComponent abstractControlsComponent : La.getINSTANCE().getClickScreen().getModulePanelComponent()) {
                    if (abstractControlsComponent.isHover(mouseX, mouseY) && !abstractControlsComponent.isDragging()) {
                        Module module = abstractControlsComponent.getModule();
                        La.getINSTANCE().getModuleManager().getModuleByName(module.getName()).setToggle(!module.isToggle());
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
