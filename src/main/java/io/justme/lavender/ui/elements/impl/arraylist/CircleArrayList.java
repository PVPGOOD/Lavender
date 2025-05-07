package io.justme.lavender.ui.elements.impl.arraylist;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.elements.AbstractElements;
import io.justme.lavender.ui.elements.ElementsEnum;
import io.justme.lavender.ui.elements.quadrant.Quadrant;
import io.justme.lavender.ui.elements.quadrant.QuadrantEnum;
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
    private float x,y,arraylistWidth,arrayListHeight;
    private boolean dragging;

    public CircleArrayList() {
        super(ElementsEnum.CircleArrayList);
    }


    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {

        var fontRenderer = La.getINSTANCE().getFontManager().getPingFang_Heavy14();
        var quadrant = new Quadrant();

        //sort
        var modules = La.getINSTANCE().getModuleManager().getElements()
                .stream()
                .sorted(Comparator.comparingInt(module -> {
                    var name = module.getName();
                    if (quadrant.getQuadrant(getX(), getY()) == QuadrantEnum.FIRST || quadrant.getQuadrant(getX(), getY()) == QuadrantEnum.SECOND) {
                        return -fontRenderer.getStringWidth(name);
                    } else {
                        return fontRenderer.getStringWidth(name);
                    }
                }))
                .toList();


        final float[] intervalX = {0.0f};
        final float[] intervalY = {0.0f};
        final int[] index = {0};
        int intervalWidth = 0;

        for (Module module : modules) {

            var animationInterval = module.getAnimationInterval();

            if (module.getAnimation().isDone() && !module.isToggle()) continue;

            var str = module.getName();

            float x = 0;
            float y = 0;
            switch (quadrant.getQuadrant(getPosX(),getPosY())) {
                case FIRST -> {
                    x = getPosX() - animationInterval.getValue() - fontRenderer.getStringWidth(str) + getWidth();
                    y = getY() + intervalY[0];

                    x -= 6;
                    y += 6;
                }

                case SECOND -> {
                    x = getX() + animationInterval.getValue();
                    y = getY() + intervalY[0];

                    x -= 6;
                    y += 6;
                }

                case THIRD ->  {
                    x = getX() + animationInterval.getValue();
                    y = getY() + intervalY[0] - fontRenderer.getHeight();
                }

                case FOURTH -> {
                    x = getX() - animationInterval.getValue() - fontRenderer.getStringWidth(str) + getWidth();
                    y = getY() + intervalY[0] - fontRenderer.getHeight();
                }
            }

            module.getAnimation().update();
            module.getAnimation().animate(module.isToggle() ? 1 : 0F, 0.5F, Easings.EXPO_OUT);

            animationInterval.animate(intervalX[0], 0.1f, Easings.LINEAR);
            animationInterval.update();

            int finalIndex = index[0];
            float finalX = x;
            float finalY = y;
            OGLUtility.scale(x + fontRenderer.getStringWidth(str) / 2f, y + fontRenderer.getHeight() / 2f, module.getAnimation().getValue(), () -> {

                RenderUtility.drawRoundRect(finalX - 3, finalY - 3,
                        fontRenderer.getStringWidth(str) + 6 ,
                        fontRenderer.getHeight(),
                        7,
                        new Color(0, 0, 0, 255));

                fontRenderer.drawStringWithOutline(module.getName(), finalX, finalY, ColorUtility.fadeBetween(finalIndex, 10, new Color(255, 207, 0).getRGB(), new Color(63, 2, 2).getRGB()));
            });

            intervalX[0] += fontRenderer.getStringWidth(str) + 10;

            switch (quadrant.getQuadrant(getPosX(),getPosY())) {
                case FIRST , SECOND -> {
                    intervalWidth = modules.stream()
                            .limit(5)
                            .mapToInt(module1 -> 15 + fontRenderer.getStringWidth(module1.getName()))
                            .sum();

                    if ((index[0] + 1) % 5 == 0) {
                        intervalX[0] = 0;
                        intervalY[0] += fontRenderer.getHeight() + 6;
                    }

                    setWidth(intervalWidth);
                    setHeight(intervalY[0] + fontRenderer.getHeight());
                }

                case THIRD, FOURTH ->  {
                    intervalWidth = modules.stream()
                            .skip(Math.max(0, modules.size() - 5))
                            .mapToInt(module1 -> 15 + fontRenderer.getStringWidth(module1.getName()))
                            .sum();


                    if ((index[0]) % 5 == 0) {
                        intervalX[0] = 0;
                        intervalY[0] += fontRenderer.getHeight() + 6;
                    }

                    setWidth(intervalWidth);
                    setHeight(intervalY[0] + fontRenderer.getHeight());
                }
            }


            index[0]++;
        }


        setX(getPosX());
        setY(getPosY());
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
