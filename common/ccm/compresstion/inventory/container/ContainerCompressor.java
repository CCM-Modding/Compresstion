package ccm.compresstion.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import ccm.nucleum.omnium.inventory.container.ProgressContainer;
import ccm.nucleum.omnium.tileentity.ProgressTE;

public class ContainerCompressor extends ProgressContainer
{
    public ContainerCompressor(ProgressTE inventory)
    {
        super(inventory);
    }
    
    public ContainerCompressor(EntityPlayer player, ProgressTE inventory)
    {
        super(inventory, player.inventory, 8, 84);
        drawBoxInventory(tile, 0, 67, 35, 1, 1);
        drawBoxInventory(tile, 1, 23, 44, 1, 1);
        drawOutBoxInventory(tile, 2, 131, 35, 1, 1);
    }
    
    @Override
    public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z)
    {
        return new ContainerCompressor(player, (ProgressTE)world.getBlockTileEntity(x, y, z));
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        return null;
    }
}