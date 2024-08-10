package io.justme.lavender.utility.player;

import io.justme.lavender.La;
import io.justme.lavender.utility.network.PacketUtility;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;

/**
 * @author JustMe.
 * @since 2024/8/10
 **/

@UtilityClass
@Getter
@Setter
public class InventoryUtility {

    @Getter
    private static final PacketUtility packetUtility = new PacketUtility();

    public void windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn)
    {
        short short1 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
        ItemStack itemstack = playerIn.openContainer.slotClick(slotId, mouseButtonClicked, mode, playerIn);
        La.getINSTANCE().print("Clicked !");
        getPacketUtility().sendPacketFromLa(new C0EPacketClickWindow(windowId, slotId, mouseButtonClicked, mode, itemstack, short1));
    }

}
