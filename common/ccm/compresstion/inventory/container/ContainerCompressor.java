package ccm.compresstion.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ccm.compresstion.tileentity.CompressorTile;
import ccm.nucleum.omnium.inventory.container.BaseContainer;

public class ContainerCompressor extends BaseContainer
{
    public ContainerCompressor(IInventory inventory)
    {
        super(inventory);
    }

    public ContainerCompressor(EntityPlayer player, IInventory inventory)
    {
        super(inventory, player.inventory, 8, 84);
        drawBoxInventory(inventory, CompressorTile.IN, 67, 35, 1, 1);
        drawBoxInventory(inventory, CompressorTile.FUEL, 23, 44, 1, 1);
        drawOutBoxInventory(inventory, CompressorTile.OUT, 131, 35, 1, 1);
    }

    @Override
    public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z)
    {
        return new ContainerCompressor(player, (IInventory) world.getBlockTileEntity(x, y, z));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        return null;
    }
}