package io.justme.lavender.ui.screens.clickgui.imgui;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.imgui.components.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.AbstractPanel;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryPanel;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.category.CategoryType;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.module.ModulePanel;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.popup.PopupPanel;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.settings.SettingPanel;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2024/8/3
 **/

@Getter
@Setter
public class ClickScreen extends GuiScreen  {

    private float x,y,width,height;
    private float draggingX,draggingY,scalingWidth, scalingHeight;
    private boolean dragging,scaling;
    private CategoryType currentCategory = CategoryType.FIGHT;
    private Color clickGuiColor = new Color(22, 24, 33);

    private final CopyOnWriteArrayList<AbstractPanel> abstractPanels = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<AbstractControlsComponent> modulePanelComponent = new CopyOnWriteArrayList<>();

    private ModulePanel modulePanel = new ModulePanel();
    private CategoryPanel categoryPanel = new CategoryPanel();
    private SettingPanel settingPanel = new SettingPanel();

    public ClickScreen() {
        setX(10);
        setY(10);


        setWidth(400);
        setHeight(460);
    }

    @Override
    public void initGui() {

        //这两是固定的 后续应该还有 sidebar和searchbar
        if (getAbstractPanels().isEmpty()) {
            getAbstractPanels().add(getCategoryPanel());
            getAbstractPanels().add(getModulePanel());
            getAbstractPanels().add(getSettingPanel());
            getModulePanel().FirstAddonModule();
        }

        for (AbstractPanel abstractPanel : La.getINSTANCE().getClickScreen().getAbstractPanels()) {
            abstractPanel.initGui();
        }

        super.initGui();

        La.getINSTANCE().getConfigScreen().initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (isDragging()) {
            setX(mouseX - getDraggingX());
            setY(mouseY - getDraggingY());
        } else if (isScaling()) {
            setWidth(mouseX - getScalingWidth());
            setHeight(mouseY - getScalingHeight());

//            setWidth(Math.min(Math.max(mouseX - getScalingWidth(), 400), 600));
//            setHeight(Math.min(Math.max(mouseY - getScalingHeight(), 460), 650));
        }



        int abstractComponentInitY = 0;
        int categoryWidth = 120;

        if (getAbstractPanels().contains(getCategoryPanel()) && getAbstractPanels().contains(getModulePanel())) {
            RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),15,new Color(22, 24, 33));
        }

        for (AbstractPanel abstractPanel : La.getINSTANCE().getClickScreen().getAbstractPanels()) {

            if (abstractPanel.getName().equals("CategoryPanel")) {
                abstractPanel.setX(getX());
                int initCategoryPanelY = abstractComponentInitY - 10;
                abstractPanel.setY(getY() - abstractComponentInitY + initCategoryPanelY);
                abstractPanel.setWidth(categoryWidth);
                abstractPanel.setHeight(getHeight());
                abstractPanel.drawScreen(mouseX, mouseY, partialTicks);


            }

            if (getCurrentCategory() == CategoryType.MISC ||
                    getCurrentCategory() == CategoryType.FIGHT ||
                    getCurrentCategory() == CategoryType.MOVEMENTS ||
                    getCurrentCategory() == CategoryType.PLAYER ||
                    getCurrentCategory() == CategoryType.Exploit || getCurrentCategory() == CategoryType.VISUAL) {
                if (abstractPanel.getName().equals("ModulePanel")) {
                    abstractPanel.setX(getX() + categoryWidth);
                    abstractPanel.setY(getY() + abstractComponentInitY);
                    abstractPanel.setWidth(getWidth() - categoryWidth);
                    abstractPanel.setHeight(getHeight() - abstractComponentInitY);
                    abstractPanel.drawScreen(mouseX, mouseY, partialTicks);
                }
            }

            if (getCurrentCategory() == CategoryType.CLIENT_SETTINGS) {
                if (abstractPanel.getName().equals("SettingPanel")) {
                    abstractPanel.setX(getX() + categoryWidth);
                    abstractPanel.setY(getY() + abstractComponentInitY);
                    abstractPanel.setWidth(getWidth() - categoryWidth);
                    abstractPanel.setHeight(getHeight() - abstractComponentInitY);
                    abstractPanel.drawScreen(mouseX, mouseY, partialTicks);
                }
            }

            switch (abstractPanel.getName()) {
                case "PopupScreen", "PopupComBox" -> abstractPanel.drawScreen(mouseX, mouseY, partialTicks);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        La.getINSTANCE().getConfigScreen().drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

        for (AbstractPanel abstractPanel : La.getINSTANCE().getClickScreen().getAbstractPanels()) {
            abstractPanel.keyTyped(typedChar, keyCode);

            if (abstractPanel.getName().equalsIgnoreCase("PopupComBox")) {
                return;
            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            if (MouseUtility.isHovering(getX(), getY(), getWidth() - 40, 20, mouseX, mouseY)) {
                setDraggingX(mouseX - getX());
                setDraggingY(mouseY - getY());
                setDragging(true);
            } else if (MouseUtility.isHovering(getX() + getWidth() - 20, getY() + getHeight() - 20, 20, 20, mouseX, mouseY)) {
                setScalingWidth(mouseX - getWidth());
                setScalingHeight(mouseY - getHeight());
                setScaling(true);
            } else if (MouseUtility.isHovering(getX() + getWidth() - 20 ,getY() + 1,20,20, mouseX, mouseY)) {

                getAbstractPanels().removeIf(abstractComponent -> abstractComponent.getName().equals("CategoryPanel"));
                getAbstractPanels().removeIf(abstractComponent -> abstractComponent.getName().equals("ModulePanel"));

                PopupPanel popUpPanel = new PopupPanel(La.getINSTANCE().getModuleManager().getClickGui());
                getAbstractPanels().add(popUpPanel);
            }
        }


        for (AbstractPanel abstractPanel : La.getINSTANCE().getClickScreen().getAbstractPanels()) {


            if (getCurrentCategory() == CategoryType.MISC ||
                    getCurrentCategory() == CategoryType.FIGHT ||
                    getCurrentCategory() == CategoryType.MOVEMENTS ||
                    getCurrentCategory() == CategoryType.PLAYER ||
                    getCurrentCategory() == CategoryType.Exploit || getCurrentCategory() == CategoryType.VISUAL) {
                if (abstractPanel.getName().equals("ModulePanel")) {
                    abstractPanel.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }

            if (getCurrentCategory() == CategoryType.CLIENT_SETTINGS) {
                if (abstractPanel.getName().equals("SettingPanel")) {
                    abstractPanel.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }

            switch (abstractPanel.getName()) {
                case "PopupComBox", "CategoryPanel", "PopupScreen", "ModulePanel" ,"SettingPanel" -> abstractPanel.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        La.getINSTANCE().getConfigScreen().mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

        for (AbstractPanel abstractPanel : La.getINSTANCE().getClickScreen().getAbstractPanels()) {


            if (getCurrentCategory() == CategoryType.MISC ||
                    getCurrentCategory() == CategoryType.FIGHT ||
                    getCurrentCategory() == CategoryType.MOVEMENTS ||
                    getCurrentCategory() == CategoryType.PLAYER ||
                    getCurrentCategory() == CategoryType.Exploit || getCurrentCategory() == CategoryType.VISUAL) {
                if (abstractPanel.getName().equals("ModulePanel")) {
                    abstractPanel.mouseReleased(mouseX, mouseY, state);
                }
            }

            if (getCurrentCategory() == CategoryType.CLIENT_SETTINGS) {
                if (abstractPanel.getName().equals("SettingPanel")) {
                    abstractPanel.mouseReleased(mouseX, mouseY, state);
                }
            }

            switch (abstractPanel.getName()) {
                case "PopupComBox", "CategoryPanel", "PopupScreen" -> abstractPanel.mouseReleased(mouseX, mouseY, state);
            }
        }

        if (state == 0){
            if (isDragging()) {
                setDragging(false);
            } else
            if (isScaling()){
                setScaling(false);
            }
        }

        La.getINSTANCE().getConfigScreen().mouseReleased(mouseX, mouseY,state);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        for (AbstractPanel component : getAbstractPanels()) {
            component.handleMouseInput();
        }
    }

}
