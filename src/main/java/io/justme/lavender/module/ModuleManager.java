package io.justme.lavender.module;

import io.justme.lavender.La;
import io.justme.lavender.events.game.EventKey;
import io.justme.lavender.module.impl.fight.KillAura;
import io.justme.lavender.module.impl.fight.Velocity;
import io.justme.lavender.module.impl.movements.*;
import io.justme.lavender.module.impl.player.ChestStealer;
import io.justme.lavender.module.impl.player.FastPlace;
import io.justme.lavender.module.impl.visual.BlockStyle;
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
    private final Velocity velocity = new Velocity();
    //visual
    private final HUD hud = new HUD();
    private final BlockStyle blockStyle = new BlockStyle();
    //movements
    private final Scaffold scaffold = new Scaffold();
    private final Sprint sprint = new Sprint();
    private final iII1IiiII1IIii1i noslow = new iII1IiiII1IIii1i();
    private final SafeWalk safeWalk = new SafeWalk();
    private final KeepSprint keepSprint = new KeepSprint();
    //player
    private final ChestStealer stealer = new ChestStealer();
    private final FastPlace fastPlace = new FastPlace();

    public void onInitialization(){

        getElements().addAll(Arrays.asList(
                //fight
                getKillAura(),
                getVelocity(),
                //visual
                getHud(),
                getBlockStyle(),
                //movements
                getNoslow(),
                getSprint(),
                getScaffold(),
                getSafeWalk(),
                getKeepSprint(),
                //player
                getStealer(),
                getFastPlace()
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
