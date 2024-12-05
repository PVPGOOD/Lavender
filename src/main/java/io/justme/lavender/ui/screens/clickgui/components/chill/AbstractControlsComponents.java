package io.justme.lavender.ui.screens.clickgui.components.chill;

import io.justme.lavender.module.Category;
import io.justme.lavender.ui.screens.clickgui.components.AbstractComponent;
import io.justme.lavender.utility.math.animation.Animation;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/


//实现value
@Getter
@Setter
public abstract class AbstractControlsComponents extends AbstractComponent {

    //不仅限category
    public Category abstractCategory;
    private final Animation categoryTypeBackgroundAlpha = new Animation(155);

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
