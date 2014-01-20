package ccm.compression.utils.helper;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import ccm.compression.api.CompressedData;
import ccm.compression.block.CompressedType;
import ccm.compression.block.ModBlocks;

public class DownConvertionRecipe implements IRecipe
{
    private CompressedType type;

    public DownConvertionRecipe(CompressedType type)
    {
        this.type = type;
    }

    public boolean matches(ItemStack s)
    {
        if (s != null)
        {
            if (s.itemID == ModBlocks.compressedBlock.blockID)
            {
                if (s.getItemDamage() == type.ordinal())
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world)
    {
        for (int i = 0; i <= inventory.getSizeInventory(); i++)
        {
            if (matches(inventory.getStackInSlot(i)))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory)
    {
        for (int i = 0; i <= inventory.getSizeInventory(); i++)
        {
            ItemStack current = inventory.getStackInSlot(i);
            if (matches(current))
            {
                return getRecipeOutput(current);
            }
        }
        return null;
    }

    @Override
    public int getRecipeSize()
    {
        return 1;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return new ItemStack(ModBlocks.compressedBlock, 9, type.hasParent() ? type.getParent().ordinal() : 0);
    }

    public ItemStack getRecipeOutput(ItemStack item)
    {
        ItemStack stack = null;
        if (type.hasParent())
        {
            stack = item.copy();
            stack.setItemDamage(type.getParent().ordinal());
            stack.stackSize = 9;
        } else
        {
            CompressedData tmp = new CompressedData();
            tmp.readFromNBT(item.getTagCompound());
            stack = new ItemStack(tmp.getID(), 9, tmp.getMeta());
            stack.setTagCompound(tmp.getData());
        }
        return stack;
    }
}