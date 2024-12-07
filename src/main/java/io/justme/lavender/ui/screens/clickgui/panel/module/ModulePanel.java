package io.justme.lavender.ui.screens.clickgui.panel.module;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponents;
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
    private AbstractControlsComponents componentToRemove;

    public ModulePanel() {
        this.setName("ModulePanel");

        for (Module module : La.getINSTANCE().getModuleManager().getElements()) {
            La.getINSTANCE().getClickScreen().getModulePanelComponent().add(new ModuleButton(module));
        }
    }

    @Override
    public void initGui() {

    }

    private float lastMouseX,lastMouseY;
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

        for (AbstractControlsComponents abstractControlsComponents : La.getINSTANCE().getClickScreen().getModulePanelComponent()) {

            if (intervalX + buttonWidth + 15 > getWidth()) {
                intervalX = 0;
                intervalY += buttonHeight + 15;
            }

            float abstractControlsComponentsX = getX() + intervalX + abstractComponentInitX;
            float abstractControlsComponentsY = getY() + intervalY + abstractComponentInitY;

            if (abstractControlsComponents.isDragging() && abstractControlsComponents.getModule() == getLastModule()) {
                abstractControlsComponents.setX(mouseX - abstractControlsComponents.getDraggingX());
                abstractControlsComponents.setY(mouseY - abstractControlsComponents.getDraggingY());
            } else {
                if (!abstractControlsComponents.isPoppingUp()) {
                    abstractControlsComponents.setX(abstractControlsComponentsX);
                    abstractControlsComponents.setY(abstractControlsComponentsY);
                }
            }


            if (Mouse.isButtonDown(0)) {
                if (abstractControlsComponents.isHover(mouseX, mouseY) && getLastModule() == abstractControlsComponents.getModule()) {
                    if (abstractControlsComponents.getClickedTimerUtility().hasTimeElapsed(120)) {
                        abstractControlsComponents.setDraggingX(mouseX - abstractControlsComponents.getX());
                        abstractControlsComponents.setDraggingY(mouseY - abstractControlsComponents.getY());
                        abstractControlsComponents.setDragging(true);
                    }
                }
            }



            abstractControlsComponents.setWidth(buttonWidth);
            abstractControlsComponents.setHeight(buttonHeight);

            abstractControlsComponents.drawScreen(mouseX, mouseY, partialTicks);
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


        setLastMouseX(mouseX);
        setLastMouseY(mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        switch (mouseButton) {
            case 0 -> {
                for (AbstractControlsComponents abstractControlsComponents : La.getINSTANCE().getClickScreen().getModulePanelComponent()) {

                    if (abstractControlsComponents.isHover(mouseX, mouseY)) {
                        setLastModule(abstractControlsComponents.getModule());
                    }

                    abstractControlsComponents.mouseClicked(mouseX, mouseY, mouseButton);
                    abstractControlsComponents.getClickedTimerUtility().reset();
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
                for (AbstractControlsComponents abstractControlsComponents : La.getINSTANCE().getClickScreen().getModulePanelComponent()) {
                    if (abstractControlsComponents.isHover(mouseX, mouseY) && !abstractControlsComponents.isDragging()) {
                        Module module = abstractControlsComponents.getModule();
                        La.getINSTANCE().getModuleManager().getModuleByName(module.getName()).setToggle(!module.isToggle());
                    }

                    if (abstractControlsComponents.isDragging()) {
                        abstractControlsComponents.setDragging(false);

                        if (abstractControlsComponents.getX() < La.getINSTANCE().getClickScreen().getX() ||
                                abstractControlsComponents.getX() > La.getINSTANCE().getClickScreen().getX() + La.getINSTANCE().getClickScreen().getWidth() ||
                                abstractControlsComponents.getY() < La.getINSTANCE().getClickScreen().getY() ||
                                abstractControlsComponents.getY() > La.getINSTANCE().getClickScreen().getY() + La.getINSTANCE().getClickScreen().getHeight()) {
                            abstractControlsComponents.setShouldPop(true);
                        }
                    }

                    if (abstractControlsComponents.isShouldPop()) {
                        abstractControlsComponents.setShouldPop(false);
                        abstractControlsComponents.setPoppingUp(true);
                        shouldCheckAnimation = true;
                        componentToRemove = abstractControlsComponents;
                    }

                    abstractControlsComponents.getClickedTimerUtility().reset();

                    abstractControlsComponents.mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
