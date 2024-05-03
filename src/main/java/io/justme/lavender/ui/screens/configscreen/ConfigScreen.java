package io.justme.lavender.ui.screens.configscreen;

import io.justme.lavender.ui.screens.configscreen.frame.impl.button.ConfigButtonFrame;
import io.justme.lavender.ui.screens.configscreen.frame.impl.list.ConfigListFrame;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

@Getter
@Setter
public class ConfigScreen extends GuiScreen {

    private float x,y,width,height;
    private float draggingX,draggingY;
    private boolean dragging;

    private final ArrayList<AbstractConfigFrame> abstractConfigFrames = new ArrayList<>();

    public ConfigScreen() {
        setX(0);
        setY(0);
        setWidth(150);
        setHeight(250);

        getAbstractConfigFrames().add(new ConfigListFrame());
        getAbstractConfigFrames().add(new ConfigButtonFrame());
    }

    @Override
    public void initGui()
    {

        for (AbstractConfigFrame frame : getAbstractConfigFrames()) {
            frame.initGui();
        }

        super.initGui();
    }

    //画板
    private final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        RenderUtility.drawRect(getX(),getY(),getWidth(),getHeight(),new Color(0,0,0,128));
        
        for (AbstractConfigFrame frame : getAbstractConfigFrames()) {

            switch (frame.getName()) {
                case "ListFrame" -> {
                    frame.setY(getY());
                    frame.setHeight(100);
                }

                case "ButtonFrame" -> {
                    frame.setY(getY() + getHeight() - 50);
                    frame.setHeight(50);
                }
            }

            frame.setX(getX());
            frame.setWidth(getWidth());

            frame.drawScreen(mouseX, mouseY, partialTicks);
        }

        if (isDragging()){
            setX(mouseX - getDraggingX());
            setY(mouseY - getDraggingY());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            if (MouseUtility.isHovering(getX(), getY(), getWidth(), 20, mouseX, mouseY)) {
                setDraggingX(mouseX - getX());
                setDraggingY(mouseY - getY());
                setDragging(true);
            }
        }

            for (AbstractConfigFrame frame : getAbstractConfigFrames()) {
            frame.mouseClicked(mouseX, mouseY, mouseButton);
        }

    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);

        setDragging(state == 1 && isDragging());

        for (AbstractConfigFrame frame : getAbstractConfigFrames()) {
            frame.mouseReleased(mouseX, mouseY, state);
        }

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        for (AbstractConfigFrame frame : getAbstractConfigFrames()) {
            frame.keyTyped(typedChar, keyCode);
        }

    }

    @Override
    public void onGuiClosed()
    {
        setDragging(false);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
       return false;
    }

}
