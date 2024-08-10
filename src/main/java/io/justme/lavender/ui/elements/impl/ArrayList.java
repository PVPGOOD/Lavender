package io.justme.lavender.ui.elements.impl;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.elements.AbstractElements;
import io.justme.lavender.ui.elements.ElementsEnum;
import io.justme.lavender.utility.gl.ColorUtility;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.Animation;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;
import java.util.Comparator;

/**
 * @author JustMe.
 * @since 2024/3/17
 **/
@Getter
@Setter
public class ArrayList extends AbstractElements {

    private ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    private float x,y,width,height;
    private boolean dragging;


    private Animation scalingAnimations = new Animation();

    public ArrayList() {
        super(ElementsEnum.ArrayList);
    }

    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {

        var fontRenderer = La.getINSTANCE().getFontManager().getPingFang_Heavy14();
        var width = getScaledResolution().getScaledWidth();
        var height = getScaledResolution().getScaledHeight();

        //sort
        var modules = La.getINSTANCE().getModuleManager().getElements()
                .stream()
                .sorted(Comparator.comparingInt(module -> {
                    var name = module.getName();

                    return getY() < height / 2f + 80 ? -fontRenderer.getStringWidth(name) : fontRenderer.getStringWidth(name);
                }))
                .toList();



        var interval = 0.0f;
        int index = 0;
        for (Module module : modules){

            if (module.getAnimation().isDone() && !module.isToggle()) continue;

            var str = module.getName();
            var x = getX() < width / 2f + 180 ? getX(): getPosX() - fontRenderer.getStringWidth(str) + getWidth();
            float finalInterval = interval;

            module.getAnimation().update();
            module.getAnimation().animate(module.isToggle() ? 1: 0F, 0.5F, Easings.EXPO_OUT);

            int finalIndex = index;
            OGLUtility.scale(x + fontRenderer.getStringWidth(str) /2f ,(getY() + interval + fontRenderer.getHeight() /2f),module.getAnimation().getValue(),() -> {

                fontRenderer.drawString(module.getName(), x ,getY() + finalInterval, ColorUtility.fadeBetween(finalIndex, 10, new Color(255,255,255).getRGB(), new Color(155,155,155).getRGB()));
            });

            interval += 11.5f;

            index++;
        }


        setX(getPosX());
        setY(getPosY());
        setWidth(100);
        setHeight(interval);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (MouseUtility.isHovering(getX(),getY(),getWidth(),getHeight(),mouseX,mouseY)){
            setDragging(!isDragging());
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
