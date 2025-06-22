package io.justme.lavender.ui.screens.configscreen.frame.impl.list.components;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.imgui.theme.ThemeColorEnum;
import io.justme.lavender.ui.screens.configscreen.frame.impl.AbstractComponents;
import io.justme.lavender.ui.screens.configscreen.frame.impl.button.components.ComponentsEnum;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/
@Getter
@Setter
public class ListComponents extends AbstractComponents {

    private FontDrawer fontRenderer = La.getINSTANCE().getFontManager().getPingFang_Bold22();

    public ListComponents(String name) {
        super(name);
        setType(ComponentsEnum.List);
    }

    @Override
    public void initGui() {

    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        var hovering = MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY);


        var select = La.getINSTANCE().getTheme().getColor(ThemeColorEnum.CONFIG_UI_BUTTON_SELECT);
        var unselect = La.getINSTANCE().getTheme().getColor(ThemeColorEnum.CONFIG_UI_BUTTON_UNSELECT);
        Color finalColor = getName().equalsIgnoreCase(La.getINSTANCE().getConfigScreen().getSelectConfig()) ? select : unselect;

        RenderUtility.drawRoundRect(
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                16,finalColor);

        getFontRenderer().drawString(getName(),getX() + (getWidth() /2f) - (getFontRenderer().getStringWidth(getName()) /2f), getY() + (getFontRenderer().getHeight() / 2f - 4),new Color(137, 154, 192).getRGB());

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
