package io.justme.lavender.module;

import io.justme.lavender.La;
import io.justme.lavender.events.game.EventKey;
import io.justme.lavender.module.impl.fight.KillAura;
import io.justme.lavender.module.impl.movements.SafeWalk;
import io.justme.lavender.module.impl.movements.Scaffold;
import io.justme.lavender.module.impl.movements.Sprint;
import io.justme.lavender.module.impl.visual.HUD;
import io.justme.lavender.utility.interfaces.Manager;
import lombok.Getter;
import net.lenni0451.asmevents.event.EventTarget;
import org.lwjglx.input.Keyboard;

import java.util.Arrays;

/**
 * @author JustMe.
 * @since 2023/12/23
 **/
@Getter
public class ModuleManager extends Manager<Module> {

    //fight
    private final KillAura killAura = new KillAura();
    //visual
    private final HUD hud = new HUD();
    //movements
    private final Scaffold scaffold = new Scaffold();
    private final Sprint sprint = new Sprint();
    private final SafeWalk safeWalk = new SafeWalk();

    public void onInitialization(){

        getElements().addAll(Arrays.asList(
                //fight
                getKillAura(),
                //visual
                getHud(),
                //movements
                getSprint(),
                getScaffold(),
                getSafeWalk()
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

        if (event.getKeyCode() == Keyboard.KEY_L) {
            La.getINSTANCE().getConfigsManager().load();
        }
    }
}
