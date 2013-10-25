package ccm.compresstion.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

import ccm.nucleum.omnium.inventory.container.ProgressContainer;
import ccm.nucleum.omnium.tileentity.interfaces.IGUITileLogic;

public class ContainerCompressor extends ProgressContainer
{
    public ContainerCompressor(IInventory inventory, IGUITileLogic guiLogic, InventoryPlayer player, int x, int y)
    {
        super(inventory, guiLogic, player, x, y);
    }
}