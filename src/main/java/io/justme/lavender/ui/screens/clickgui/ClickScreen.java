package io.justme.lavender.ui.screens.clickgui;

import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

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

    public ClickScreen() {

    }

    @Override
    public void initGui() {

        this.x = 15;
        this.y = 15;
        this.width = 100;
        this.height = 250;

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

        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),4,new Color(255, 240, 245));
        RenderUtility.drawRoundRect(getX(),getY(),15,15,4,new Color(240, 222, 223));
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
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
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
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
    public boolean doesGuiPauseGame() {
        return false;
    }
}
