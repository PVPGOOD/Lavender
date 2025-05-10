package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.keybind;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.utility.gl.RenderUtility;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/10
 **/
public class ModuleKeybind extends AbstractKeybind {

    public ModuleKeybind(Module module) {
        super(module);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        var font = La.getINSTANCE().getFontManager().getSFBold18();
        String keyName = Keyboard.getKeyName(getModule().getKey());

        if (isHover(mouseX,mouseY)) {
            RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),6,new Color(0x269D9D9D, true));
        }

        if (isBinding()) {
            font.drawString("...",getX() + getWidth() /2f - font.getStringWidth("...") /2f,getY() + getHeight() /2f - font.getHeight() /2f + 3,new Color(0,0,0,100).getRGB());
        } else {
            if (!keyName.equalsIgnoreCase("None")) {
                RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),6,new Color(0x269D9D9D, true));
                font.drawString(keyName,getX() + getWidth() /2f - font.getStringWidth(keyName) /2f,getY() + getHeight() /2f - font.getHeight() /2f + 3,new Color(0,0,0,100).getRGB());
            }
        }

        setWidth(20);
        setHeight(15);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        return isHover(mouseX, mouseY);
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        return isHover(mouseX, mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            setBinding(false);
        }

        if (isBinding()) {
            getModule().setKey(keyCode);
            setBinding(false);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
