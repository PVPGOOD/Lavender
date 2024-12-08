package io.justme.lavender.ui.screens.clickgui.controls.scrollbar;

import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponent;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/8
 **/
@Getter
@Setter
public class ScrollbarControls extends AbstractControlsComponent {

    private float backgroundHeight;
    private float sliderY,sliderHeight;
    private float contentHeight,viewHeight;
    private float scrollOffset,maxScrollOffset;
    private int dragOffset;
    private boolean isDragging;

    @Override
    public void initGui() {

    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        setBackgroundHeight((getViewHeight() / getContentHeight()) * getHeight());
        setSliderHeight(getViewHeight() / getContentHeight() * getBackgroundHeight());
        setSliderY((-getScrollOffset() / (getContentHeight() - getViewHeight()) * (getBackgroundHeight() - getSliderHeight())));

        if ((getViewHeight() < getContentHeight() - 10)) {
           RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getBackgroundHeight(), 2, new Color(255, 255, 255));
           RenderUtility.drawRoundRect(getX(), getY() + getSliderY(), getWidth(), getSliderHeight(), 2, new Color(123, 113, 255));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
