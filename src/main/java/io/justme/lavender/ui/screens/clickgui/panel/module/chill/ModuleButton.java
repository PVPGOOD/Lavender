package io.justme.lavender.ui.screens.clickgui.panel.module.chill;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.chill.AbstractControlsComponent;
import io.justme.lavender.module.Module;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.gl.RenderUtility;
import javax.swing.Timer;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;

/**
 * @author JustMe.
 * @since 2024/12/6
 **/
@Getter
@Setter
public class ModuleButton extends AbstractControlsComponent {

    private Module module;

    public ModuleButton(Module module) {
        setModule(module);
        this.module = module;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        OGLUtility.scale(getX() + getWidth() / 2f, getY() + getHeight() / 2f, getPopUpAnimation().getValue(), () -> {
            RenderUtility.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 12, getModule().isToggle() ? new Color(255, 255, 255, 255) : new Color(201, 201, 201, 255));

            FontDrawer fontManager = La.getINSTANCE().getFontManager().getPingFang_Medium22();

            fontManager.drawString(getModule().getName(),
                    getX() + getWidth() / 2f - (fontManager.getStringWidth(getModule().getName()) / 2f),
                    getY() + getHeight() / 2f - (fontManager.getHeight() / 2f) + 5,
                    new Color(129, 57, 80, 255).getRGB());
        });

        getPopUpAnimation().update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        //这边得延迟12毫秒才能与 ModulePanel::drawScreen() 同步 因为判断在那边 这边立刻执行会导致不同步
        if (mouseButton == 0) {
            if (isHover(mouseX, mouseY)) {
                Timer timer = new Timer(130, e -> {
                    if (isDragging()) {
                        getPopUpAnimation().animate(0.8f, 0.1f);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            if (isHover(mouseX, mouseY) && isPoppingUp()) {
                getPopUpAnimation().animate(0.0f, 0.1f);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
