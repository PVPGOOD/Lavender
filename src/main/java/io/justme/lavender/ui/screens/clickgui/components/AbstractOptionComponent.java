package io.justme.lavender.ui.screens.clickgui.components;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/8
 **/
@Getter
@Setter
public abstract class AbstractOptionComponent {

    private float descriptionX,descriptionY, descriptionWidth, descriptionHeight;
    public float x,y,width,height;
    private FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium18();

    public float modeExpandingHeight;
    public String comBoxChillName;
    @Getter
    @Setter
    public static String comBoxSelectingName;
    public ComponentsType componentsType;

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public abstract void initGui();
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
    public abstract void handleMouseInput() throws IOException;

}
