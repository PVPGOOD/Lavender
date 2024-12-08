package io.justme.lavender.ui.screens.clickgui;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.module.Category;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.panel.category.CategoryPanel;
import io.justme.lavender.ui.screens.clickgui.panel.module.ModulePanel;
import io.justme.lavender.ui.screens.clickgui.panel.popupscreen.PopupScreen;
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
    private Category currentCategory = Category.FIGHT;
    private Color clickGuiColor = new Color(255, 240, 245);

    private final CopyOnWriteArrayList<AbstractComponent> components = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<AbstractControlsComponent> modulePanelComponent = new CopyOnWriteArrayList<>();

    private ModulePanel modulePanel = new ModulePanel();
    private CategoryPanel categoryPanel = new CategoryPanel();

    public ClickScreen() {
        setX(10);
        setY(10);


        setWidth(400);
        setHeight(460);
    }

    @Override
    public void initGui() {

        //这两是固定的 后续应该还有 sidebar和searchbar
        if (getComponents().isEmpty()) {
            getComponents().add(getCategoryPanel());
            getComponents().add(getModulePanel());
            getModulePanel().afterAddOptions();
        }

        for (AbstractComponent abstractComponent : La.getINSTANCE().getClickScreen().getComponents()) {
            abstractComponent.initGui();
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
            setWidth(Math.min(Math.max(mouseX - getScalingWidth(), 400), 600));
            setHeight(Math.min(Math.max(mouseY - getScalingHeight(), 460), 650));
        }



        int abstractComponentInitY = 30;
        int categoryWidth = 120;

        if (getComponents().contains(getCategoryPanel()) && getComponents().contains(getModulePanel())) {
            RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),15,new Color(255, 240, 245));
            //横线
            RenderUtility.drawRoundRect(getX(),getY() + abstractComponentInitY,getWidth(),0.5f,1,new Color(255, 232, 238));
            FontDrawer fontManager = La.getINSTANCE().getFontManager().getSFBold18();
            fontManager.drawString("My_Project",getX() + 5,getY() + 5,new Color(255,255,255).getRGB());
        }

        for (AbstractComponent abstractComponent : La.getINSTANCE().getClickScreen().getComponents()) {
            switch (abstractComponent.getName()) {
                case "CategoryPanel" -> {
                    abstractComponent.setX(getX());
                    abstractComponent.setY(getY() + abstractComponentInitY);
                    abstractComponent.setWidth(categoryWidth);
                    abstractComponent.setHeight(getHeight() - abstractComponentInitY);
                    abstractComponent.drawScreen(mouseX, mouseY, partialTicks);
                }

                case "ModulePanel" -> {
                    abstractComponent.setX(getX() + categoryWidth);
                    abstractComponent.setY(getY() + abstractComponentInitY);
                    abstractComponent.setWidth(getWidth() - categoryWidth);
                    abstractComponent.setHeight(getHeight() - abstractComponentInitY);
                    abstractComponent.drawScreen(mouseX, mouseY, partialTicks);
                }

                case "PopupScreen", "PopupComBox" -> abstractComponent.drawScreen(mouseX, mouseY, partialTicks);

            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        La.getINSTANCE().getConfigScreen().drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

        for (AbstractComponent abstractComponent : La.getINSTANCE().getClickScreen().getComponents()) {
            abstractComponent.keyTyped(typedChar, keyCode);

            if (abstractComponent.getName().equalsIgnoreCase("PopupComBox")) {
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

                getComponents().removeIf(abstractComponent -> abstractComponent.getName().equals("CategoryPanel"));
                getComponents().removeIf(abstractComponent -> abstractComponent.getName().equals("ModulePanel"));

                PopupScreen popUpScreen = new PopupScreen(La.getINSTANCE().getModuleManager().getClickGui());
                getComponents().add(popUpScreen);
            }
        }

        for (AbstractComponent abstractComponent : La.getINSTANCE().getClickScreen().getComponents()) {
            switch (abstractComponent.getName()) {
                case "PopupComBox", "CategoryPanel", "PopupScreen", "ModulePanel" -> abstractComponent.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        La.getINSTANCE().getConfigScreen().mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

        for (AbstractComponent abstractComponent : La.getINSTANCE().getClickScreen().getComponents()) {
            switch (abstractComponent.getName()) {
                case "PopupComBox", "CategoryPanel", "PopupScreen", "ModulePanel" -> abstractComponent.mouseReleased(mouseX, mouseY, state);
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

        for (AbstractComponent component : getComponents()) {
            component.handleMouseInput();
        }
    }

}
