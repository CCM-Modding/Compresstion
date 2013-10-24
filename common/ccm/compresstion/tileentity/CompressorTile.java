package ccm.compresstion.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

import ccm.nucleum.omnium.tileentity.InventoryTE;

public class CompressorTile extends InventoryTE implements ISidedInventory
{
    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if (side == ForgeDirection.UP.ordinal())
        {
            return new int[]
            { 0 };
        } else if (side == ForgeDirection.DOWN.ordinal())
        {
            return new int[]
            { 1 };
        } else
        {
            return new int[] {};
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side)
    {
        return side == ForgeDirection.UP.ordinal() ? true : false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return side == ForgeDirection.DOWN.ordinal() ? true : false;
    }
}