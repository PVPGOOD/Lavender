package io.justme.lavender.ui.elements.impl.arraylist;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.elements.AbstractElements;
import io.justme.lavender.ui.elements.ElementsEnum;
import io.justme.lavender.utility.gl.ColorUtility;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.MouseUtility;
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
 * @since 2025/4/3
 **/
@Getter
@Setter
public class CircleArrayList extends AbstractElements {

    private ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    private float x,y,width,height;
    private boolean dragging;

    public CircleArrayList() {
        super(ElementsEnum.CircleArrayList);
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
                    if (getY() < height / 2f + 80) {
                        return -fontRenderer.getStringWidth(name);
                    } else {
                        return fontRenderer.getStringWidth(name);
                    }
                }))
                .toList();


        final float[] intervalX = {0.0f};
        final float[] intervalY = {0.0f};
        final int[] index = {0};

        for (Module module : modules) {

            var animationInterval = module.getAnimationInterval();

            if (module.getAnimation().isDone() && !module.isToggle()) continue;

            var str = module.getName();
            var x = getX() + animationInterval.getValue() < width / 2f + 180 ?
                    getX() + animationInterval.getValue():
                    getPosX() - animationInterval.getValue() - fontRenderer.getStringWidth(str) + getWidth();

            var y = getY() + intervalY[0];


            module.getAnimation().update();
            module.getAnimation().animate(module.isToggle() ? 1 : 0F, 0.5F, Easings.EXPO_OUT);

            animationInterval.animate(intervalX[0], 0.1f, Easings.LINEAR);
            animationInterval.update();

            int finalIndex = index[0];
            OGLUtility.scale(x + fontRenderer.getStringWidth(str) / 2f, y + fontRenderer.getHeight() / 2f, module.getAnimation().getValue(), () -> {

                RenderUtility.drawRoundRect(x - 3, y - 3,
                        fontRenderer.getStringWidth(str) + 6 ,
                        fontRenderer.getHeight(),
                        6,
                        new Color(30, 27, 27, 255));

                fontRenderer.drawStringWithOutline(module.getName(), x, y, ColorUtility.fadeBetween(finalIndex, 10, new Color(255, 207, 0).getRGB(), new Color(63, 2, 2).getRGB()));
            });

            intervalX[0] += fontRenderer.getStringWidth(str) + 10; // 调整模块之间的间距

            if ((index[0] + 1) % 5 == 0) { // 每5个模块换一行
                intervalX[0] = 0;
                intervalY[0] += fontRenderer.getHeight() + 5; // 调整行之间的间距
            }

            index[0]++;
        }

        setX(getPosX());
        setY(getPosY());
        setWidth(100);
        setHeight(intervalY[0] + fontRenderer.getHeight());
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
