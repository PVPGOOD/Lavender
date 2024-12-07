package io.justme.lavender.ui.screens.clickgui.controls;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponents;
import io.justme.lavender.ui.screens.clickgui.panel.popupscreen.PopupComBox;
import io.justme.lavender.utility.gl.RenderUtility;
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
public class ComBoxControls extends AbstractControlsComponents {

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRectWithOutline(getX(),getY(),getWidth(),getHeight(),8,1,new Color(255, 224, 235, 255),new Color(164, 158, 255, 255));
        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Bold18();
        fontDrawer.drawString(getComBoxOption().getName() ,
                getX() + getWidth() /2f - fontDrawer.getStringWidth(getComBoxOption().getName()) /2f,
                getY() + getHeight()/2f - fontDrawer.getHeight()/2f + 3,
                new Color(129, 57, 80).getRGB());

        setWidth(100);
        setHeight(25);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if (isHover(mouseX,mouseY)) {
            CopyOnWriteArrayList<AbstractComponent> component = La.getINSTANCE().getClickScreen().getComponents();

            for (AbstractComponent abstractComponent : component) {
                if (abstractComponent.getName().contains("PopupComBox")) {
                    return;
                }
            }
            component.add(new PopupComBox());
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
