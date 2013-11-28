package ccm.compresstion.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ccm.compresstion.tileentity.CompressorTile;
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
        drawBoxInventory(tile, CompressorTile.IN, 67, 35, 1, 1);
        drawBoxInventory(tile, CompressorTile.FUEL, 23, 44, 1, 1);
        drawOutBoxInventory(tile, CompressorTile.OUT, 131, 35, 1, 1);
    }

    @Override
    public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z)
    {
        return new ContainerCompressor(player, (ProgressTE) world.getBlockTileEntity(x, y, z));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        return null;
    }
}