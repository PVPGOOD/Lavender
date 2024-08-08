package io.justme.lavender.ui.elements.impl;

import io.justme.lavender.La;
import io.justme.lavender.module.impl.fight.KillAura;
import io.justme.lavender.ui.elements.AbstractElements;
import io.justme.lavender.ui.elements.ElementsEnum;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/8/9
 **/
@Getter
@Setter
public class TargetList extends AbstractElements {

    private float x,y,width,height;
    private boolean dragging;


    public TargetList() {
        super(ElementsEnum.TargetList);
    }

    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {
        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),3, Color.BLACK);

        var ka = (((KillAura) La.getINSTANCE().getModuleManager().getModuleByName("KillAura")));

        var interval = 0;

        if (!ka.getTargets().isEmpty()) {
            for (EntityLivingBase livingBase : ka.getTargets()) {
                La.getINSTANCE().getFontManager().getSFBold12().drawString(
                        livingBase.getName(),
                        getX() + 5,
                        getY() + 5 + interval,
                        new Color(255,255,255,ka.getTargets().get(ka.getIndex()) == livingBase ? 255 : 155).getRGB());
                interval += 5;
            }

            setWidth(50);
        } else {
            La.getINSTANCE().getFontManager().getSFBold14().drawString(
                  "NO TARGET THERE",
                    getX() + 5,
                    getY() + 3 + interval,
                    new Color(255,255,255,255).getRGB());
            setWidth(80);
        }

        setX(getPosX());
        setY(getPosY());
        setHeight(15 + interval);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            if (MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY)) {
                setDragging(true);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        setDragging(false);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
