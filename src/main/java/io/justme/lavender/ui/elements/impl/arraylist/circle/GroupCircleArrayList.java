package io.justme.lavender.ui.elements.impl.arraylist.circle;

import io.justme.lavender.La;
import io.justme.lavender.ui.elements.AbstractElement;
import io.justme.lavender.ui.elements.impl.arraylist.circle.components.impl.CircleComponent;
import io.justme.lavender.ui.elements.impl.arraylist.circle.popup.AbstractPopUp;
import io.justme.lavender.ui.elements.impl.arraylist.circle.popup.impl.CirclePopUp;
import io.justme.lavender.ui.elements.quadrant.Quadrant;
import io.justme.lavender.ui.elements.quadrant.QuadrantEnum;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.math.MouseUtility;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2025/5/24
 **/
@Getter
@Setter
public class GroupCircleArrayList extends AbstractElement {

    private float x,y,width,height;

    public GroupCircleArrayList(String label) {
        super(label);
    }

    @Override
    public void draw(float partialTicks, int mouseX, int mouseY) {

        var interval = 0;

//        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(), 0.1f,new Color(0,0,0,32));
        La.getINSTANCE().getFontManager().getPingFang_Bold22().drawString(getLabel(),getX() + getWidth()/2f,getY(),-1);

        var fontRenderer = La.getINSTANCE().getFontManager().getPingFang_Heavy14();
        var quadrant = new Quadrant();

        //sort
        var modules = getCircleComponents()
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
        for (CircleComponent elements : modules) {

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


            elements.module.getAnimation().update();
            elements.module.getAnimation().animate(elements.module.isToggle() ? 1 : 0F, 0.5F, Easings.EXPO_OUT);

            animationInterval.animate(intervalX[0], 0.1f, Easings.LINEAR);
            animationInterval.update();

            int finalIndex = index[0];
            float finalX = x;
            float finalY = y;
            OGLUtility.scale(x + fontRenderer.getStringWidth(str) / 2f, y + fontRenderer.getHeight() / 2f, elements.module.getAnimation().getValue(), () -> {
                elements.setX(finalX + 10);
                elements.setY(finalY );
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
                    setHeight(intervalY[0] + fontRenderer.getHeight()  + 10);
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
                    setHeight(intervalY[0] + fontRenderer.getHeight() + 10);
                }
            }

            index[0]++;
        }

        for (AbstractPopUp popUp : La.getINSTANCE().getElementsManager().getGroupCircleArrayListManager().getPopUps()) {

            if (popUp.isDragging()) {
                popUp.setX(mouseX - popUp.getWidth() / 2);
                popUp.setY(mouseY - popUp.getHeight() / 2);
            } else {
                popUp.setX(getX() + getWidth() / 2 - popUp.getWidth() / 2);
                popUp.setY(getY() + getHeight() / 2 - popUp.getHeight() / 2);
            }

            popUp.draw(mouseX, mouseY);
        }



        setX(getPosX());
        setY(getPosY());
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        boolean componentClicked = false;

        for (CircleComponent circleComponent : getCircleComponents()) {
            if (!circleComponent.mouseClicked(mouseX, mouseY, mouseButton)) continue;

            componentClicked = true;

            circleComponent.getClickedTimerUtility().reset();
            circleComponent.setDragging(false);

            if (mouseButton != 0 || !circleComponent.isHover(mouseX, mouseY)) continue;

            circleComponent.setDragging(true);

            var circlePopUp = getCirclePopUp(circleComponent);
            La.getINSTANCE().getElementsManager()
                    .getGroupCircleArrayListManager()
                    .getPopUps()
                    .add(circlePopUp);

            circleComponent.getAbstractGroup()
                    .getCircleComponents()
                    .remove(circleComponent);
        }

        if (!componentClicked && MouseUtility.isHovering(getPosX(), getPosY(), getWidth(), 20, mouseX, mouseY)) {
            setDragging(true);
        }

        if (getCircleComponents().isEmpty()) {
            La.getINSTANCE().getElementsManager().getElements().remove(this);

            var elementsConfigs = La.getINSTANCE().getConfigsManager().getElementsConfigs();
            if (elementsConfigs.has(getElementName())) {
                elementsConfigs.remove(getElementName());
                elementsConfigs.save();
            }
        }

    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        setDragging(false);
    }

    private CirclePopUp getCirclePopUp(CircleComponent circleComponent) {
        var circlePopUp = new CirclePopUp(circleComponent.getModule());
        circlePopUp.setX(getX());
        circlePopUp.setY(getY());
        circlePopUp.setDragging(true);
        return circlePopUp;
    }


    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    public void addCircleComponent(CircleComponent circleComponent) {
        getCircleComponents().add(circleComponent);
    }
}
