package io.justme.lavender.ui.screens.clickgui.panel.category.chill;

import io.justme.lavender.La;
import io.justme.lavender.module.Category;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponents;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.utility.math.animation.Animation;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/5
 **/
@Getter
public class CategoryIcon extends AbstractControlsComponents {

    public Category category;

    public CategoryIcon(Category category) {
        abstractCategory = category;

        this.category = category;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int color = ((int)(getCategory() == La.getINSTANCE().getClickScreen().getCurrentCategory() ? 255 : getCategoryTypeBackgroundAlpha().getValue()));

        RenderUtility.drawRoundRect(getX(),getY(),getWidth(),getHeight(),6,new Color(250, 250, 250,color));

        RenderUtility.drawImage(
                new ResourceLocation("la/clickgui/category/" + getCategory().getName() + ".png"),
                getX() + (getWidth() / 5),
                getY() + (getHeight() / 5),
                16,
                16,
                new Color(255, 177, 210,255));

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
