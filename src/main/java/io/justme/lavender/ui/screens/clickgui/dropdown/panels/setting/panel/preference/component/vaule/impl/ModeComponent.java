package io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.impl;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.AbstractSettingComponent;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.SettingComponentType;
import io.justme.lavender.ui.screens.clickgui.dropdown.panels.setting.panel.preference.component.vaule.impl.chill.ModeChill;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.gl.ScissorUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.value.impl.ModeValue;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author JustMe.
 * @since 2024/12/7
 **/

@Getter
@Setter
public class ModeComponent extends AbstractSettingComponent {

    private ModeValue option;
    private boolean expanded;
    private CopyOnWriteArrayList<AbstractSettingComponent> modeChill = new CopyOnWriteArrayList<>();

    public ModeComponent() {
        this.moduleComponentType = SettingComponentType.MODE;
    }

    public void afterAddOption() {
        for (String modeOption : getOption().getModes()) {
            getModeChill().add(new ModeChill(modeOption, getOption()));
        }
    }

    @Override
    public void initGui() {

    }

    private float interval = 0;
    private Animation expandingRadius = new Animation();
    private Animation expandingHeight = new Animation();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {


        getExpandingRadius().animate(isExpanded() ? 0 : 20,0.1f);
        getExpandingHeight().animate(isExpanded() ? getHeight() + getInterval() - 8 : 0,0.1f);

        var optionInterval = new AtomicReference<>((float) 8);
        if (isExpanded()) {
            var posX = getX();
            var posY = getY() + getHeight() - 5;
            ScissorUtility.scissor(posX,posY,getWidth(),getExpandingHeight().getValue(),() -> {
                //背景
                RenderUtility.drawRoundRectWithOutline(
                        posX,
                        getY() + getHeight() - 8,
                        getWidth(),
                        getExpandingHeight().getValue(),
                        10,1,new Color(248, 225, 234, 255),
                        new Color(255, 230, 241, 255));

                for (AbstractSettingComponent chill : getModeChill()) {
                    chill.setX(getX() + 4);
                    chill.setY(getY() + getHeight() + optionInterval.get());
                    chill.setWidth(getWidth() - 8);
                    chill.setHeight(17);
                    optionInterval.updateAndGet(v -> v + 23);
                    setInterval(optionInterval.get() - 8);

                    chill.drawScreen(mouseX, mouseY, partialTicks);
                }
            });

        }

        RenderUtility.drawRoundRectWithCustomRounded(
                getX(),getY(),getWidth(),getHeight(),
                new Color(232, 222, 248, 255),getExpandingRadius().getValue(),getExpandingRadius().getValue(),20,20);

        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Medium18();
        fontDrawer.drawString(
                getOption().getName(),
                getDescriptionX() + 2,
                getDescriptionY() + getHeight() /2f - fontDrawer.getHeight() /2f,
                new Color(0,0,0,155).getRGB());

        fontDrawer.drawString(getOption().getValue() ,
                getX() + 10,
                getY() + getHeight()/2f - fontDrawer.getHeight()/2f + 3,new Color(0, 0, 0,155).getRGB());


        if (isExpanded()) {
            setModeExpandingHeight(optionInterval.get() + 10);
        } else {
            setModeExpandingHeight(optionInterval.get());
        }

        getExpandingHeight().update();
        getExpandingRadius().update();
        setWidth(100);
        setHeight(25);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY)) {
            setExpanded(!isExpanded());
        }

        if (isExpanded()) {
            for (AbstractSettingComponent chill : getModeChill()) {

                if (chill.isHover(mouseX, mouseY)) {
                    if (chill instanceof ModeChill) {
                        getOption().setValue(((ModeChill) chill).getComBoxChillName());
                        setComBoxSelectingName(getOption().getValue());
                    }

                    chill.mouseClicked(mouseX, mouseY, mouseButton);
                }


            }
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
