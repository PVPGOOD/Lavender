package io.justme.lavender.ui.screens.clickgui.dropdown.panels.module.components.value;

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
    private FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Heavy18();

    public float modeExpandingHeight;
    @Getter
    @Setter
    //唯一性
    public static String comBoxSelectingName;
    public ModuleComponentType moduleComponentType;
    //为了防止我以后忘记 requestInterval 由上级定义 并不需要在这里处理 2024/4/5留

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
