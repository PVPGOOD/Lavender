package io.justme.lavender.ui.screens.configscreen.frame.impl.list.components;

import io.justme.lavender.ui.screens.configscreen.frame.impl.AbstractComponents;
import io.justme.lavender.ui.screens.configscreen.frame.impl.button.components.ComponentsEnum;
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
public class ListComponents extends AbstractComponents {

    public ListComponents(String name) {
        super(name);

        setType(ComponentsEnum.List);
    }

    @Override
    public void initGui() {

    }

    private final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        var hovering = MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY);
        RenderUtility.drawRect(getX(),getY(),getWidth(),getHeight(),new Color(0,0,0,hovering ? 64 : 128));

        getFontRenderer().drawString(getName(),((int) getX()),((int) getY()),-1);
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
