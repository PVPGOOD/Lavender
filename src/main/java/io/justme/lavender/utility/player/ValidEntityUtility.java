package io.justme.lavender.utility.player;

import io.justme.lavender.utility.interfaces.IMinecraft;
import lombok.experimental.UtilityClass;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author JustMe.
 * @since 2025/5/10
 **/
@UtilityClass
public class ValidEntityUtility implements IMinecraft {

    public boolean isOnSameTeam(EntityLivingBase entity) {
        if(mc.thePlayer.getTeam() != null && entity.getTeam() != null) {
            Character targetColor = entity.getDisplayName().getFormattedText().charAt(1);
            Character playerColor = mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
            return playerColor.equals(targetColor);
        } else {
            return false;
        }
    }


}
