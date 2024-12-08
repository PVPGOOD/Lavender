package io.justme.lavender.ui.screens.clickgui.components.chill;

import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.utility.math.TimerUtility;
import io.justme.lavender.utility.math.animation.Animation;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

//大杂烩
@Getter
@Setter
public abstract class AbstractControlsComponent extends AbstractComponent {

    private final TimerUtility clickedTimerUtility = new TimerUtility();
    private float draggingX,draggingY;
    private boolean dragging,shouldPop,PoppingUp;

    //category
    public Category abstractCategory;
    private final Animation categoryTypeBackgroundAlpha = new Animation(155);

    //module
    private Module module;
    private final Animation moduleButtonPosXAnimation = new Animation();
    private final Animation moduleButtonPosYAnimation = new Animation();
    private final Animation draggingAnimation = new Animation();
    private final Animation popUpAnimation = new Animation(1);

    public boolean isHover(int mouseX, int mouseY) {
        return super.isHover(mouseX, mouseY);
    }

    @Override
    public abstract void initGui();
    @Override
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    @Override
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    @Override
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    @Override
    public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
}
