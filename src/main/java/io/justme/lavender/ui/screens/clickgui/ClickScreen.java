package io.justme.lavender.ui.screens.clickgui;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.fonts.FontManager;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.panel.category.CategoryPanel;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

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
    private ArrayList<AbstractComponent> component = new ArrayList<>();

    public ClickScreen() {
        setX(10);
        setY(10);
        setWidth(100);
        setHeight(100);

        getComponent().add(new CategoryPanel());
    }

    @Override
    public void initGui() {

        for (AbstractComponent abstractComponent : getComponent()) {
            abstractComponent.initGui();
        }

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (isDragging()){
            setX(mouseX - getDraggingX());
            setY(mouseY - getDraggingY());
        } else if (isScaling()) {
            setWidth(mouseX - getScalingWidth());
            setHeight(mouseY - getScalingHeight());
        }

        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),15,new Color(255, 240, 245));

        int abstractComponentInitY = 50;
        for (AbstractComponent abstractComponent : getComponent()) {
            abstractComponent.setX(getX());
            abstractComponent.setY(getY() + abstractComponentInitY);
            abstractComponent.setWidth(40);
            abstractComponent.setHeight(getHeight() - abstractComponentInitY);

            abstractComponent.drawScreen(mouseX, mouseY, partialTicks);
        }

        //横线
        RenderUtility.drawRoundRect(getX(),getY() + abstractComponentInitY,getWidth(),0.5f,1,new Color(255, 149, 191));
        FontDrawer fontManager = La.getINSTANCE().getFontManager().getSFBold18();
        fontManager.drawString("My_Project",getX() + 5,getY() + 5,new Color(255,255,255).getRGB());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

        for (AbstractComponent abstractComponent : getComponent()) {
            abstractComponent.keyTyped(typedChar, keyCode);
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0){
            if (MouseUtility.isHovering(getX(),getY(),getWidth(),20,mouseX,mouseY)){
                setDraggingX(mouseX - getX());
                setDraggingY(mouseY - getY());
                setDragging(true);
            } else if (MouseUtility.isHovering(getX() + getWidth() - 20 ,getY() + getHeight() - 20,20,20,mouseX,mouseY)){
                setScalingWidth(mouseX - getWidth());
                setScalingHeight(mouseY - getHeight());
                setScaling(true);
            }
        }


        for (AbstractComponent abstractComponent : getComponent()) {
            abstractComponent.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractComponent abstractComponent : getComponent()) {
            abstractComponent.mouseReleased(mouseX, mouseY, state);
        }


        if (state == 0){
            if (isDragging()) {
                setDragging(false);
            } else
            if (isScaling()){
                setScaling(false);
            }
        }

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
}
