package io.justme.lavender.ui.elements.impl.arraylist.draw;

import io.justme.lavender.La;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.elements.AbstractElements;
import io.justme.lavender.ui.elements.ElementsEnum;
import io.justme.lavender.ui.elements.impl.arraylist.AbstractArraylist;
import io.justme.lavender.ui.elements.impl.arraylist.components.CircleComponent;
import io.justme.lavender.ui.elements.quadrant.Quadrant;
import io.justme.lavender.ui.elements.quadrant.QuadrantEnum;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2025/4/3
 **/
@Getter
@Setter
public class CircleArrayList extends AbstractElements {

    private ArrayList<AbstractArraylist> components = new ArrayList<>();
    private ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    private float x,y,arraylistWidth,arrayListHeight;
    private boolean dragging;

    public CircleArrayList() {
        super(ElementsEnum.CircleArrayList);

        for (Module module : La.getINSTANCE().getModuleManager().getElements()) {
            getComponents().add(new CircleComponent(module));
        }
    }


    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {

        var fontRenderer = La.getINSTANCE().getFontManager().getPingFang_Heavy14();
        var quadrant = new Quadrant();

        //sort
        var modules = getComponents()
                .stream()
                .filter(elements -> elements.module.isToggle())
                .sorted((a, b) -> {
                    var currentQuadrant = quadrant.getQuadrant(getPosX(), getPosY());
                    if (currentQuadrant == QuadrantEnum.FIRST || currentQuadrant == QuadrantEnum.SECOND) {
                        return Double.compare(b.getWidth(), a.getWidth());
                    } else {
                        return Double.compare(a.getWidth(), b.getWidth());
                    }
                })
                .toList();


        final float[] intervalX = {0.0f};
        final float[] intervalY = {0.0f};
        final int[] index = {0};
        int intervalWidth = 0;


        for (AbstractArraylist elements : modules) {

            var animationInterval = elements.module.getAnimationInterval();

            if (elements.module.getAnimation().isDone() && !elements.module.isToggle()) continue;

            var str = elements.module.getName();

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

            if (elements.isDragging()) {
                elements.setDraggingX(mouseX - x);
                elements.setDraggingY(mouseY - y);
            }

            if (elements.isDragging()) {
                x += mouseX - elements.getDraggingX();
                y += mouseY - elements.getDraggingY();
            }


            elements.module.getAnimation().update();
            elements.module.getAnimation().animate(elements.module.isToggle() ? 1 : 0F, 0.5F, Easings.EXPO_OUT);

            animationInterval.animate(intervalX[0], 0.1f, Easings.LINEAR);
            animationInterval.update();

            int finalIndex = index[0];
            float finalX = x;
            float finalY = y;
            OGLUtility.scale(x + fontRenderer.getStringWidth(str) / 2f, y + fontRenderer.getHeight() / 2f, elements.module.getAnimation().getValue(), () -> {

                elements.setX(finalX - 3);
                elements.setY(finalY - 3);
                elements.setWidth(fontRenderer.getStringWidth(str) + 6);
                elements.setHeight(fontRenderer.getHeight());
                elements.setIndex(finalIndex);
                elements.setFontDrawer(fontRenderer);
                elements.draw(mouseX, mouseY);
            });

            intervalX[0] += fontRenderer.getStringWidth(str) + 10;

            switch (quadrant.getQuadrant(getPosX(), getPosY())) {
                case FIRST, SECOND -> {
                    intervalWidth = modules.stream()
                            .sorted((a, b) -> Float.compare(fontRenderer.getStringWidth(b.module.getName()), fontRenderer.getStringWidth(a.module.getName()))) // 按宽度降序排序
                            .limit(5)
                            .mapToInt(module1 -> 15 + fontRenderer.getStringWidth(module1.module.getName()))
                            .sum();

                    if ((index[0] + 1) % 5 == 0) {
                        intervalX[0] = 0;
                        intervalY[0] += fontRenderer.getHeight() + 6;
                    }

                    setWidth(intervalWidth);
                    setHeight(intervalY[0] + fontRenderer.getHeight());
                }

                case THIRD, FOURTH -> {
                    intervalWidth = modules.stream()
                            .skip(Math.max(0, modules.size() - 5))
                            .mapToInt(module1 -> 15 + fontRenderer.getStringWidth(module1.module.getName()))
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
        for (AbstractArraylist elements : getComponents()) {
            if (elements.mouseClicked(mouseX, mouseY, mouseButton)) {
                elements.setDraggingY(mouseX - elements.getX());
                elements.setDraggingY(mouseY - elements.getY());
                elements.setDragging(true);
            }
        }

//        if (MouseUtility.isHovering(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
//            setDragging(!isDragging());
//        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        setDragging(false);
        for (AbstractArraylist elements : getComponents()) {
            elements.setDragging(false); // 停止拖拽
            elements.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
