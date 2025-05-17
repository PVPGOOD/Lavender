package io.justme.lavender.ui.screens.mainmenu.panels.main;

import io.justme.lavender.ui.screens.mainmenu.AbstractMainMenuUI;
import io.justme.lavender.ui.screens.mainmenu.MainMenuScreen;
import io.justme.lavender.ui.screens.mainmenu.panels.main.components.AbstractComponent;
import io.justme.lavender.ui.screens.mainmenu.panels.main.components.impl.button.MainMenuButton;
import io.justme.lavender.ui.screens.mainmenu.panels.main.components.impl.button.MainMenuButtonType;
import io.justme.lavender.ui.screens.multiplayer.GuiMultiplayer;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.shader.interfaces.Shader;
import io.justme.lavender.utility.math.animation.Animation;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2025/5/17
 **/
@Getter
@Setter
public class MainPanel extends AbstractMainMenuUI {

    private ArrayList<AbstractComponent> abstractComponents = new ArrayList<>();


    public MainPanel() {
        getAbstractComponents().add(new MainMenuButton(MainMenuButtonType.SINGLE_PLAY));
        getAbstractComponents().add(new MainMenuButton(MainMenuButtonType.MULTIPLE_PLAY));
        getAbstractComponents().add(new MainMenuButton(MainMenuButtonType.OPTIONS));
        getAbstractComponents().add(new MainMenuButton(MainMenuButtonType.EXIT));
    }

    @Override
    public void initGui() {

    }

    private Animation rounded = new Animation();
    private Animation widthAnimation = new Animation();
    private Animation alpha = new Animation();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Shader.bloom.run(() -> RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),23,new Color(0xFEF7FF)),true);

        var intervalX = 0f;
        var intervalY = 0f;
        var initY = 20;
        var initX = 30;
        var topButtonToBottomButton = 60;
        for (AbstractComponent abstractComponent : getAbstractComponents()) {
            switch (abstractComponent.getMainMenuButtonType()) {
                case SINGLE_PLAY, MULTIPLE_PLAY -> {
                    abstractComponent.setX(getX() + initX);
                    abstractComponent.setY(getY() + intervalY + initY);
                    abstractComponent.setWidth(getWidth() - initX * 2);
                    abstractComponent.setHeight(30);
                    intervalY += 55;
                    abstractComponent.drawScreen(mouseX, mouseY, partialTicks);
                }

                case OPTIONS,EXIT -> {
                    int width = 80;
                    if (abstractComponent.getMainMenuButtonType() == MainMenuButtonType.EXIT) {
                        abstractComponent.setX(getX() + getWidth() - initX - width);
                    } else {
                        abstractComponent.setX(getX() + initX + intervalX);
                    }
                    abstractComponent.setY(getY() + intervalY + topButtonToBottomButton / 2f);
                    abstractComponent.setWidth(width);
                    abstractComponent.setHeight(30);
                    abstractComponent.drawScreen(mouseX, mouseY, partialTicks);
                    intervalX += 100;
                }
            }
        }

        var scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        var width = 260;
        setX(scaledResolution.getScaledWidth() / 2f - width /2f);
        setWidth(width);
        var height = intervalY + initY + topButtonToBottomButton;
        setY(scaledResolution.getScaledHeight() /2f - height /2f);
        setHeight(height);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractComponent abstractComponent : getAbstractComponents()) {
            if (abstractComponent.mouseClicked(mouseX, mouseY, mouseButton)) {
                final Minecraft mc = Minecraft.getMinecraft();
                switch (abstractComponent.getMainMenuButtonType()) {
                    case SINGLE_PLAY -> mc.displayGuiScreen(new GuiSelectWorld(new MainMenuScreen()));
                    case MULTIPLE_PLAY -> mc.displayGuiScreen(new GuiMultiplayer(new MainMenuScreen()));
                    case OPTIONS ->  mc.displayGuiScreen(new GuiOptions(new MainMenuScreen(), mc.gameSettings));
                    case EXIT -> Minecraft.getMinecraft().shutdown();
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
