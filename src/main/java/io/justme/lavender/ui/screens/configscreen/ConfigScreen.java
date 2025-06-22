package io.justme.lavender.ui.screens.configscreen;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.clickgui.imgui.theme.ThemeColorEnum;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.Animation;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

@Getter
@Setter
public class ConfigScreen extends GuiScreen {

    private ScaledResolution scaledResolution;
    private float x,y,width,height;
    private float draggingX,draggingY;
    private boolean dragging;
    private final CopyOnWriteArrayList<AbstractConfigFrame> abstractConfigFrames = new CopyOnWriteArrayList<>();
    private boolean expanded;
    private String selectConfig;

    public ConfigScreen() {

        setExpanded(false);

        getAbstractConfigFrames().add(La.getINSTANCE().getConfigListFrame());
        getAbstractConfigFrames().add(La.getINSTANCE().getConfigButtonFrame());
    }

    @Override
    public void initGui()
    {
        super.initGui();

        setSelectConfig(La.getINSTANCE().getConfigsManager().getPageName());

        if (getScaledResolution() == null) {
            setScaledResolution(new ScaledResolution(Minecraft.getMinecraft()));
        } else {
            setScaledResolution(null);
            setScaledResolution(new ScaledResolution(Minecraft.getMinecraft()));
        }

        setWidth(150);
        setHeight(250);
        setX(getScaledResolution().getScaledWidth() - getWidth() - 5);
        setY(getScaledResolution().getScaledHeight() - getHeight() - 5);

        for (AbstractConfigFrame frame : getAbstractConfigFrames()) {
            frame.initGui();
        }

    }

    //画板

    //实际为ListFrame Y
    private int finalHeight = 200;
    private final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    private Animation openingAnimationY = new Animation(15);
    private Animation openingAnimationHeight = new Animation(50);
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),16,La.getINSTANCE().getTheme().getColor(ThemeColorEnum.CLICKSCREEN_BACKGROUND));

        for (AbstractConfigFrame frame : getAbstractConfigFrames()) {
//
           switch (frame.getName()) {
                case "ListFrame" -> {
                    frame.setY(getY());
                    frame.setHeight(getHeight() - 50);
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

//        if (isDragging()){
//            setX(mouseX - getDraggingX());
//            setY(mouseY - getDraggingY());
//        }


        setY(getOpeningAnimationY().getValue());
        setHeight(getOpeningAnimationHeight().getValue());

        getOpeningAnimationHeight().animate(isExpanded() ? 35 : getFinalHeight() - 5,0.1f);
        getOpeningAnimationHeight().update();

        getOpeningAnimationY().animate(isExpanded() ? getScaledResolution().getScaledHeight() - 45 : getScaledResolution().getScaledHeight() - finalHeight - 5,0.1f);
        getOpeningAnimationY().update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
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
    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);

        if (MouseUtility.isHovering(getX(), getY(), getWidth(), 20, mouseX, mouseY)) {
            if (state == 1) {
                setExpanded(!isExpanded());
            }
        }

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
