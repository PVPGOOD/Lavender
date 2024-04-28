package io.justme.lavender.module;

import io.justme.lavender.La;
import io.justme.lavender.events.game.EventKey;
import io.justme.lavender.module.impl.movements.Sprint;
import io.justme.lavender.module.impl.visual.HUD;
import io.justme.lavender.utility.interfaces.Manager;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;

import java.util.Arrays;

/**
 * @author JustMe.
 * @since 2023/12/23
 **/
@Getter
public class ModuleManager extends Manager<Module> {

    private final Sprint sprint = new Sprint();
    private final HUD hud = new HUD();

    public void onInitialization(){

        getElements().addAll(Arrays.asList(
                //visual
                getHud(),
                //movements
               getSprint()
        ));

        getElements().forEach(Module::reflectValues);
        La.getINSTANCE().getEventManager().register(this);
    }

    public Module getModuleByName(String name){
        return getElements().stream().filter(mod -> mod.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @EventTarget
    public void onKey(EventKey event) {
        for (Module module : getElements()) {
            if (module.key == event.getKeyCode()) module.setStatus(!module.isToggle());
        }
    }
}
