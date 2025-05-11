package io.justme.lavender.module;

import io.justme.lavender.La;
import io.justme.lavender.module.impl.blatant.exploit.disabler.Disabler;
import io.justme.lavender.module.impl.blatant.fight.*;
import io.justme.lavender.module.impl.blatant.misc.AutoTool;
import io.justme.lavender.module.impl.blatant.misc.FireBallWarning;
import io.justme.lavender.module.impl.blatant.movements.*;
import io.justme.lavender.module.impl.blatant.movements.noslowdown.NoSlowDown;
import io.justme.lavender.module.impl.blatant.movements.speed.Speed;
import io.justme.lavender.module.impl.blatant.player.*;
import io.justme.lavender.module.impl.blatant.visual.*;
import io.justme.lavender.module.impl.blatant.world.AntiVoid;
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
    private final Rotations rotations = new Rotations();
    private final NameTag nameTag = new NameTag();
    private final Chams chams = new Chams();
    private final FreeCam freeCam = new FreeCam();
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
    private final HitBox hitBox = new HitBox();

    //misc
    private final AutoTool autoTool = new AutoTool();
    private final FireBallWarning fireBallWarning = new FireBallWarning();
    //exploit
    private final Disabler disabler = new Disabler();
//    private final PacketDebugger packetDebugger = new PacketDebugger();

    //world
    private final AntiVoid antiVoid = new AntiVoid();

    //blatant
    private final AutoClicker autoClicker = new AutoClicker();
    private final AimAssist aimAssist = new AimAssist();
    private final Reach reach = new Reach();

    public void onInitialization(){

        getElements().addAll(Arrays.asList(
                getRotations(),
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
                getNameTag(),
                getChams(),
                getFreeCam(),
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
                getHitBox(),
                //exploit
                getDisabler(),
//                getPacketDebugger()

                //misc
                getAutoTool(),
                getFireBallWarning(),
                //world
                getAntiVoid(),

                //legit
                getAutoClicker(),
                getAimAssist(),
                getReach()
        ));

        getElements().forEach(Module::reflectValues);
        La.getINSTANCE().getEventManager().register(this);
    }

    public Module getModuleByName(String name){
        return getElements().stream().filter(mod -> mod.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
