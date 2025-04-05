package io.justme.lavender.ui.screens.clickgui.components.impl;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.AbstractOptionComponent;
import io.justme.lavender.ui.screens.clickgui.components.ComponentType;
import io.justme.lavender.ui.screens.clickgui.components.impl.chill.ModeChill;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.value.impl.ModeValue;
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
public class ModeComponent extends AbstractOptionComponent {

    private ModeValue option;
    private boolean expanded;
    private CopyOnWriteArrayList<AbstractOptionComponent> modeChill = new CopyOnWriteArrayList<>();

    public ModeComponent() {
        this.componentType = ComponentType.MODE;
    }

    public void afterAddOption() {
        for (String modeOption : getOption().getModes()) {
            getModeChill().add(new ModeChill(modeOption));
        }
    }

    @Override
    public void initGui() {

    }

    private float interval = 0;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRectWithOutline(getX(),getY(),getWidth(),getHeight(),12,1,new Color(255, 233, 240, 255),new Color(215, 198, 215, 255));
        getFontDrawer().drawString(getOption().getName(),getDescriptionX() + 2,getDescriptionY() + getHeight() /2f - getFontDrawer().getHeight() /2f,new Color(0).getRGB());

        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Bold18();
        fontDrawer.drawString(getOption().getValue() ,getX() + 10   ,getY() + getHeight()/2f - fontDrawer.getHeight()/2f + 3,new Color(129, 57, 80).getRGB());

        float optionInterval = 8;

        if (isExpanded()) {

            //背景
            RenderUtility.drawRoundRectWithOutline(
                    getX(),
                    getY() + getHeight() + 1,
                    getWidth(),
                    getHeight() + getInterval() - 8,
                    10,1,new Color(255, 224, 235, 255),
                    new Color(164, 158, 255, 255));

            for (AbstractOptionComponent chill : getModeChill()) {
                chill.setX(getX() + 4);
                chill.setY(getY() + getHeight() + optionInterval + 1);
                chill.setWidth(getWidth() - 8);
                chill.setHeight(17);
                optionInterval += 19;
                setInterval(optionInterval - 5);

                chill.drawScreen(mouseX, mouseY, partialTicks);
            }
        }


        setModeExpandingHeight(optionInterval + 5);
        setWidth(100);
        setHeight(25);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY)) {
            setExpanded(!isExpanded());
        }

        if (isExpanded()) {
            for (AbstractOptionComponent chill : getModeChill()) {

                if (chill.isHover(mouseX,mouseY)) {
                    getOption().setValue(chill.getComBoxChillName());
                }

                chill.mouseClicked(mouseX, mouseY, mouseButton);
            }

            setComBoxSelectingName(getOption().getValue());
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
