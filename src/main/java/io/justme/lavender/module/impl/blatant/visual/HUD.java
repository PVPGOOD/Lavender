package io.justme.lavender.module.impl.blatant.visual;

import io.justme.lavender.La;
import io.justme.lavender.events.render.Event2DRender;
import io.justme.lavender.module.Category;
import io.justme.lavender.module.Module;
import io.justme.lavender.module.ModuleInfo;
import io.justme.lavender.ui.elements.AbstractElement;
import io.justme.lavender.ui.elements.impl.arraylist.circle.GroupCircleArrayList;
import io.justme.lavender.value.impl.ModeValue;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import org.lwjglx.input.Keyboard;

/**
 * @author JustMe.
 * @since 2024/4/28
 **/

@Getter
@ModuleInfo(name = "HUD", description = "IDK.", category = Category.VISUAL,key = Keyboard.KEY_H)
public class HUD extends Module {

    private final ModeValue
            arrayListMode = new ModeValue("arrayList Mode", new String[]{"Legacy", "Circle"}, "Circle");

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
        La.getINSTANCE().getFontManager().getSFBold18().drawString("我的世界免费辅助 -> 获取 加Q 1697183819 By Rir ", 0,0,-1);

        var elements = La.getINSTANCE().getElementsManager();

        if (getArrayListMode().getValue().equals("Legacy")) {
            elements.getLegacyArrayList().draw(event2DRender.getPartialTicks(), La.getINSTANCE().getMouseX(), La.getINSTANCE().getMouseY());
        }

        if (getArrayListMode().getValue().equals("Circle")) {
//            elements.getCircleArrayListElement().draw(event2DRender.getPartialTicks(), La.getINSTANCE().getMouseX(), La.getINSTANCE().getMouseY());
        }

        for (AbstractElement element : elements.getElements()) {
            if (element instanceof GroupCircleArrayList) {
                element.draw(event2DRender.getPartialTicks(), La.getINSTANCE().getMouseX(), La.getINSTANCE().getMouseY());
            }
        }

        elements.getTargetListElement().draw(event2DRender.getPartialTicks(), La.getINSTANCE().getMouseX(), La.getINSTANCE().getMouseY());
    }

}
