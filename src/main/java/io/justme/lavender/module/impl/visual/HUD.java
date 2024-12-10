package io.justme.lavender.module.impl.visual;

import io.justme.lavender.La;
import io.justme.lavender.events.render.Event2DRender;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.ui.elements.AbstractElements;
import io.justme.lavender.utility.gl.RenderUtility;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import org.lwjglx.input.Keyboard;

import java.awt.*;

/**
 * @author JustMe.
 * @since 2024/4/28
 **/

@Getter
@ModuleInfo(name = "HUD", description = "IDK.", category = Category.VISUAL,key = Keyboard.KEY_H)
public class HUD extends Module {

    @Override
    public void onEnable(){
        super.onEnable();
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }

    @EventTarget
    public void on2D(Event2DRender event2DRender) {
//        La.getINSTANCE().getFontManager().getSFBold18().drawString(La.getINSTANCE().getLa(), 0,0,-1);

        for (AbstractElements abstractElements : La.getINSTANCE().getElementsManager().getElements()) {
            abstractElements.draw(event2DRender.getPartialTicks(), La.getINSTANCE().getMouseX(), La.getINSTANCE().getMouseY());
        }
    }

}
