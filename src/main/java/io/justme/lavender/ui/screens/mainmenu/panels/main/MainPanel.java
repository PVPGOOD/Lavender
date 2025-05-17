package io.justme.lavender.ui.screens.mainmenu.panels.main;

import io.justme.lavender.ui.screens.mainmenu.AbstractMainMenuUI;
import io.justme.lavender.ui.screens.mainmenu.MainMenuScreen;
import io.justme.lavender.ui.screens.mainmenu.panels.main.components.AbstractComponent;
import io.justme.lavender.ui.screens.mainmenu.panels.main.components.impl.button.MainMenuButton;
import io.justme.lavender.ui.screens.mainmenu.panels.main.components.impl.button.MainMenuButtonType;
import io.justme.lavender.ui.screens.microsoft.GuiMicrosoftLogin;
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
        getAbstractComponents().add(new MainMenuButton(MainMenuButtonType.ALTS_LOGIN));
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
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        float screenWidth = scaledResolution.getScaledWidth();
        float screenHeight = scaledResolution.getScaledHeight();

        float panelWidth = screenWidth * 0.20f;
        float initX = panelWidth * 0.1f;
        float initY = screenHeight * 0.05f;
        float buttonHeight = screenHeight * 0.04f;
        float verticalSpacing = buttonHeight + screenHeight * 0.02f;

        float intervalY = 0f;
        for (AbstractComponent component : getAbstractComponents()) {
            if (component.getMainMenuButtonType() == MainMenuButtonType.SINGLE_PLAY
                    || component.getMainMenuButtonType() == MainMenuButtonType.MULTIPLE_PLAY) {
                intervalY += verticalSpacing;
            }
        }
        float bottomButtonOffset = screenHeight * 0.15f;

        float panelHeight = intervalY + initY + bottomButtonOffset;
        float panelX = screenWidth / 2f - panelWidth / 2f;
        float panelY = screenHeight / 2f - panelHeight / 2f;

        setX(panelX);
        setY(panelY);
        setWidth(panelWidth);
        setHeight(panelHeight);

        Shader.bloom.run(() -> RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 15, new Color(0xFEF7FF)), true);

        float intervalX = 0f;
        intervalY = 0f;

        for (AbstractComponent abstractComponent : getAbstractComponents()) {
            switch (abstractComponent.getMainMenuButtonType()) {
                case SINGLE_PLAY, MULTIPLE_PLAY,ALTS_LOGIN -> {
                    abstractComponent.setX(getX() + initX);
                    abstractComponent.setY(getY() + initY + intervalY);
                    abstractComponent.setWidth(getWidth() - initX * 2);
                    abstractComponent.setHeight((int) buttonHeight);
                    abstractComponent.drawScreen(mouseX, mouseY, partialTicks);
                    intervalY += verticalSpacing;
                }

                case OPTIONS, EXIT -> {
                    float smallButtonWidth = panelWidth * 0.3f;
                    if (abstractComponent.getMainMenuButtonType() == MainMenuButtonType.EXIT) {
                        abstractComponent.setX(getX() + getWidth() - initX - smallButtonWidth);
                    } else {
                        abstractComponent.setX(getX() + initX + intervalX);
                    }
                    abstractComponent.setY(getY() + initY + intervalY  + bottomButtonOffset / 8f);
                    abstractComponent.setWidth(smallButtonWidth);
                    abstractComponent.setHeight((int) buttonHeight);
                    abstractComponent.drawScreen(mouseX, mouseY, partialTicks);
                    intervalX += smallButtonWidth + screenWidth * 0.015f;
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractComponent abstractComponent : getAbstractComponents()) {
            if (abstractComponent.mouseClicked(mouseX, mouseY, mouseButton)) {
                final Minecraft mc = Minecraft.getMinecraft();
                switch (abstractComponent.getMainMenuButtonType()) {
                    case SINGLE_PLAY -> mc.displayGuiScreen(new GuiSelectWorld(new MainMenuScreen()));
                    case MULTIPLE_PLAY -> mc.displayGuiScreen(new GuiMultiplayer(new MainMenuScreen()));
                    case ALTS_LOGIN -> mc.displayGuiScreen(new GuiMicrosoftLogin(new MainMenuScreen()));
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
