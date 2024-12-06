package io.justme.lavender.ui.screens.clickgui.panel.module.chill;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponents;
import io.justme.lavender.module.Module;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/6
 **/
@Getter
@Setter
public class ModuleButton extends AbstractControlsComponents {

    private Module module;

    public ModuleButton(Module module) {
        setModule(module);
        this.module = module;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),12,getModule().isToggle() ? new Color(255, 255, 255,255) : new Color(201, 201, 201,255));

        FontDrawer fontManager = La.getINSTANCE().getFontManager().getPingFang_Medium22();
        fontManager.drawString(getModule().getName(),
                getX() + getWidth()/2f - (fontManager.getStringWidth(getModule().getName()) /2f),
                getY() +getHeight()/2f - (fontManager.getHeight() / 2f) + 5,
                new Color(129, 57, 80,255).getRGB());

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
