package io.justme.lavender.module;

import io.justme.lavender.La;
import io.justme.lavender.module.impl.blatant.exploit.disabler.Disabler;
//import io.justme.lavender.module.impl.blatant.exploit.packetDebugger.PacketDebugger;
import io.justme.lavender.module.impl.blatant.fight.KillAura;
import io.justme.lavender.module.impl.blatant.fight.Velocity;
import io.justme.lavender.module.impl.blatant.movements.*;
import io.justme.lavender.module.impl.blatant.movements.noslowdown.NoSlowDown;
import io.justme.lavender.module.impl.blatant.movements.speed.Speed;
import io.justme.lavender.module.impl.blatant.player.*;
import io.justme.lavender.module.impl.blatant.visual.*;
import io.justme.lavender.module.impl.legit.fight.AutoClicker;
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
    private final NoCamClip noCamClip = new NoCamClip();
    private final NoHurtCam noHurtCam = new NoHurtCam();
    private final ClickGui clickGui = new ClickGui();
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
    private final InventoryMove inventoryMove = new InventoryMove();
    private final NoJumpDelay noJumpDelay = new NoJumpDelay();
    //exploit
    private final Disabler disabler = new Disabler();
//    private final PacketDebugger packetDebugger = new PacketDebugger();


    //blatant
    private final AutoClicker autoClicker = new AutoClicker();

    public void onInitialization(){

        getElements().addAll(Arrays.asList(
                //blatant

                //fight
                getKillAura(),
                getVelocity(),
                //visual
                getHud(),
                getBlockStyle(),
                getNoHurtCam(),
                getNoCamClip(),
                getClickGui(),
                //movements
                getNoslow(),
                getSprint(),
                getScaffold(),
                getSafeWalk(),
                getKeepSprint(),
                getSpeed(),
                getInventoryMove(),
                //player
                getStealer(),
                getFastPlace(),
                getAutoPot(),
                getInventoryCleaner(),
                getNoJumpDelay(),
                getNoFall(),
                //exploit
                getDisabler(),
//                getPacketDebugger()

                //legit
                getAutoClicker()
        ));

        getElements().forEach(Module::reflectValues);
        La.getINSTANCE().getEventManager().register(this);
    }

    public Module getModuleByName(String name){
        return getElements().stream().filter(mod -> mod.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
