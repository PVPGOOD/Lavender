package io.justme.lavender.ui.screens.clickgui.imgui.components.impl;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.imgui.components.ComponentType;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.AbstractPanel;
import io.justme.lavender.ui.screens.clickgui.imgui.components.AbstractOptionComponent;
import io.justme.lavender.ui.screens.clickgui.imgui.panels.popup.PopupComBox;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.value.impl.MultiBoolValue;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/
@Getter
@Setter
public class ComBoxComponent extends AbstractOptionComponent {

    public MultiBoolValue option;

    public ComBoxComponent() {
        this.componentType = ComponentType.COMBOX;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRectWithOutline(getX(),getY(),getWidth(),getHeight(),12,1,new Color(255, 233, 240, 255),new Color(215, 198, 215, 255));
        FontDrawer fontDrawer = getFontDrawer();
        fontDrawer.drawString(getOption().getName() ,
                getX() + getWidth() /2f - fontDrawer.getStringWidth(getOption().getName()) /2f,
                getY() + getHeight()/2f - fontDrawer.getHeight()/2f + 3,
                new Color(129, 57, 80).getRGB());

        setWidth(100);
        setHeight(25);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if (isHover(mouseX,mouseY)) {
            CopyOnWriteArrayList<AbstractPanel> component = La.getINSTANCE().getClickScreen().getAbstractPanels();

            for (AbstractPanel abstractPanel : component) {
                if (abstractPanel.getName().contains("PopupComBox")) {
                    return;
                }
            }
            component.add(new PopupComBox(getOption()));
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
