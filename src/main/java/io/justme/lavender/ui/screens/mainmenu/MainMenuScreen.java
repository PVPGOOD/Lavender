package io.justme.lavender.ui.screens.mainmenu;

import io.justme.lavender.ui.screens.mainmenu.panels.main.MainPanel;
import io.justme.lavender.utility.gl.shader.interfaces.Shader;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2024/4/10
 **/
@Getter
@Setter
public class MainMenuScreen extends GuiScreen {

    private ArrayList<AbstractMainMenuUI> elements = new ArrayList<>();

    public MainMenuScreen() {
        getElements().add(new MainPanel());
    }

    @Override
    public void initGui()
    {
        for (AbstractMainMenuUI element : getElements()) {
            element.initGui();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {

//        RenderUtility.drawRoundRect(0,0,width,height,0,new Color(0x6257FF));
        Shader.organicRect.drawOrganic( 0, 0,
                width , height ,
                3.0f, 0.1f,
                4.0f, 0.1f,
                3.5f, 0.08f,
                0.21f,
                partialTicks,
                new Color(0x80FDE6EF, true),
                new Color(0x80FDE6EF, true),
                new Color(0x66FCD9F1, true),
                new Color(0xFDF8FC));


        for (AbstractMainMenuUI element : getElements()) {
            element.drawScreen(mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractMainMenuUI element : getElements()) {
            element.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }

}
