package io.justme.lavender.ui.screens.element;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.elements.AbstractElements;
import io.justme.lavender.ui.elements.quadrant.Quadrant;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/4/3
 **/

@Getter
@Setter
public class ElementScreen extends GuiScreen {

    //elements
    private AbstractElements elements;

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Bold18();
        Quadrant quadrant = new Quadrant();
        int centerX = (int) quadrant.getCenterX();
        int centerY = (int) quadrant.getCenterY();


        for (AbstractElements element : La.getINSTANCE().getElementsManager().getElements()) {
            if (element.isDragging()) {
                elements = element;
            }
//            element.draw(partialTicks, mouseX, mouseY);
        }

        if (elements != null) {
            if (elements.isDragging()) {
                elements.onDrag(mouseX, mouseY);

                fontDrawer.drawStringWithOutline(elements.getQuadrant().name(),
                        mouseX + 10,
                        mouseY + fontDrawer.getHeight() / 2f,
                        new Color(255, 255, 255).getRGB());

            } else {
                elements.reset();
            }
        }
    }

    public void onGuiClosed()
    {
        for (AbstractElements elements : La.getINSTANCE().getElementsManager().getElements()) {
            elements.reset();
       }
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException
    {
        for (AbstractElements elements : La.getINSTANCE().getElementsManager().getElements())
            elements.keyTyped(typedChar,keyCode);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (mouseButton == 0)
        {
            for (AbstractElements elements : La.getINSTANCE().getElementsManager().getElements())
                elements.mouseClicked(mouseX,mouseY,mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

        for (AbstractElements elements : La.getINSTANCE().getElementsManager().getElements())
            elements.mouseReleased(mouseX,mouseY,state);

        super.mouseReleased(mouseX,mouseY,state);
    }
}
