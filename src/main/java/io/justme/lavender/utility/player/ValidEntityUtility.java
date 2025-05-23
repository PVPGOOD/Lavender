package io.justme.lavender.utility.player;

import com.mojang.authlib.GameProfile;
import io.justme.lavender.utility.interfaces.IMinecraft;
import lombok.experimental.UtilityClass;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getTablist() {
        return mc.getNetHandler().getPlayerInfoMap().stream()
                .map(NetworkPlayerInfo::getGameProfile)
                .filter(profile -> profile.getId() != mc.thePlayer.getUniqueID())
                .map(GameProfile::getName)
                .collect(Collectors.toList());
    }


}
