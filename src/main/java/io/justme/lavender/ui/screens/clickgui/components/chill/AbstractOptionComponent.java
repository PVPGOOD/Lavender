package io.justme.lavender.ui.screens.clickgui.components.chill;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.controls.type.ControlsType;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/8
 **/
@Getter
@Setter
public abstract class AbstractOptionComponent extends AbstractComponent {

    private float descriptionX,descriptionY, descriptionWidth, descriptionHeight;
    private FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium18();

    public float modeExpandingHeight;

    public String comBoxChillName;
    @Getter
    @Setter
    public static String comBoxSelectingName;

    public ControlsType controlsType;

    public boolean isHover(int mouseX, int mouseY) {
        return super.isHover(mouseX, mouseY);
    }

    @Override
    public abstract void initGui();
    @Override
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    @Override
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    @Override
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    @Override
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
}
