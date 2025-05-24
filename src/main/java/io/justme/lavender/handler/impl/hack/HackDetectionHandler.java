package io.justme.lavender.handler.impl.hack;

import io.justme.lavender.events.player.EventUpdate;
import io.justme.lavender.handler.AbstractHandler;
import io.justme.lavender.handler.impl.hack.sub.AutoBlockDetector;
import io.justme.lavender.handler.impl.hack.sub.NoSlowDownDetector;
import io.justme.lavender.handler.impl.hack.sub.SprintDetector;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author JustMe.
 * @since 2025/5/23
 **/
@Getter
@Setter
public class HackDetectionHandler extends AbstractHandler {

    private HashMap<EntityPlayer, ArrayList<HackType>> usingModuleArraylist = new HashMap<>();
    private ArrayList<AbstractHackDetection> detectionArrayList = new ArrayList<>();

    public HackDetectionHandler() {
        getDetectionArrayList().add(new NoSlowDownDetector());
        getDetectionArrayList().add(new SprintDetector());
        getDetectionArrayList().add(new AutoBlockDetector());
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            for (AbstractHackDetection abstractHackDetection : getDetectionArrayList()) {

                if ( !player.isDead) {
//                    if(ValidEntityUtility.isOnSameTeam(player) || ValidEntityUtility.getTablist().contains(player.getName())) {
//                        continue;
//                    }
                    abstractHackDetection.onUpdate(player);
                }
            }
        }
    }
}
