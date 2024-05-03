package io.justme.lavender.ui.screens.configscreen.frame.components.impl;

import io.justme.lavender.ui.screens.configscreen.frame.components.AbstractComponents;
import io.justme.lavender.ui.screens.configscreen.frame.components.ComponentsEnum;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

@Getter
@Setter
public class CheckBox extends AbstractComponents {

    public CheckBox(ComponentsEnum name) {
        super(name);
    }

    @Override
    public void initGui() {

    }

    private final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRect(getX(),getY(),getWidth(),getHeight(),new Color(0,0,0,64));
        getFontRenderer().drawStringWithShadow(getComponentsEnum().getName(),((int) getX()) + 5, (int) (getY() + (getHeight() / 2) - 4),-1);

        setWidth(55);
        setHeight(20);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        return MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
