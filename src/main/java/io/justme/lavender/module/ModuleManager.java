package io.justme.lavender.module;

import io.justme.lavender.La;
import io.justme.lavender.module.impl.exploit.disabler.Disabler;
import io.justme.lavender.module.impl.exploit.packetDebugger.PacketDebugger;
import io.justme.lavender.module.impl.fight.KillAura;
import io.justme.lavender.module.impl.fight.Velocity;
import io.justme.lavender.module.impl.movements.*;
import io.justme.lavender.module.impl.movements.speed.Speed;
import io.justme.lavender.module.impl.player.*;
import io.justme.lavender.module.impl.visual.BlockStyle;
import io.justme.lavender.module.impl.visual.HUD;
import io.justme.lavender.utility.interfaces.Manager;
import lombok.Getter;

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
    private final NoSlowDown noslow = new NoSlowDown();
    private final SafeWalk safeWalk = new SafeWalk();
    private final KeepSprint keepSprint = new KeepSprint();
    private final Speed speed = new Speed();
    //player
    private final ChestStealer stealer = new ChestStealer();
    private final FastPlace fastPlace = new FastPlace();
    private final NoFall noFall = new NoFall();
    private final AutoPot autoPot = new AutoPot();
    private final InventoryCleaner inventoryCleaner = new InventoryCleaner();
    //exploit
    private final Disabler disabler = new Disabler();
    private final PacketDebugger packetDebugger = new PacketDebugger();

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
                getSpeed(),
                //player
                getStealer(),
                getFastPlace(),
                getAutoPot(),
                getInventoryCleaner(),
                //exploit
                getDisabler(),
                getPacketDebugger()
        ));

        getElements().forEach(Module::reflectValues);
        La.getINSTANCE().getEventManager().register(this);
    }

    public Module getModuleByName(String name){
        return getElements().stream().filter(mod -> mod.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
