package ccm.compresstion.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;

import cpw.mods.fml.common.registry.GameRegistry;

import ccm.nucleum.omnium.inventory.container.element.TimedElement;
import ccm.nucleum.omnium.tileentity.InventoryTE;
import ccm.nucleum.omnium.tileentity.ProgressTE;

public class CompressorTile extends ProgressTE implements ISidedInventory
{
    public CompressorTile()
    {
        progresses = new TimedElement[2];
        progresses[0] = new TimedElement();
        progresses[1] = new TimedElement();
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if(canRun()){
            
        }
    }
    
    @Override
    public boolean canRun()
    {
        boolean bool = false;
        int burntime = TileEntityFurnace.getItemBurnTime(inventory[1]);
        //if()
        
        return bool;
    }
    
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
            { 2 };
        } else
        {
            return new int[]
            { 1 };
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side)
    {
        if(side != ForgeDirection.DOWN.ordinal()){
            if (side != ForgeDirection.UP.ordinal()){
                if(TileEntityFurnace.isItemFuel(item)){
                    return true;
                }
            }else{
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return side == ForgeDirection.DOWN.ordinal() ? true : false;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {
        if(slot != 2){
            if (slot != 0){
                if(TileEntityFurnace.isItemFuel(item)){
                    return true;
                }
            }else{
                return true;
            }
        }
        return false;
    }
}