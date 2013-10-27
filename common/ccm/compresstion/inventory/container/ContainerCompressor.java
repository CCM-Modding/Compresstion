package ccm.compresstion.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

import ccm.nucleum.omnium.inventory.container.ProgressContainer;

public class ContainerCompressor extends ProgressContainer
{

    public ContainerCompressor(IInventory inventory)
    {
        super(inventory);
        // FINISH
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

}