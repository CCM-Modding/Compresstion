package ccm.compresstion.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

import ccm.nucleum.omnium.inventory.container.ProgressContainer;
import ccm.nucleum.omnium.tileentity.ProgressTE;

public class ContainerCompressor extends ProgressContainer
{

    public ContainerCompressor(ProgressTE inventory)
    {
        super(inventory);
        // FINISH
    }

    @Override
    public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z)
    {
        return new ContainerCompressor((ProgressTE)world.getBlockTileEntity(x, y, z));
    }
}