package ccm.compresstion.utils.registry.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import ccm.compresstion.block.CompressedType;
import ccm.compresstion.utils.lib.Archive;
import ccm.compresstion.utils.lib.Properties;
import ccm.nucleum.omnium.utils.helper.NBTHelper;

public class DownConvertionRecipe implements IRecipe
{
    private int id = Properties.BLOCK_COMPRESSED_ID + 256;

    CompressedType currentType;
    CompressedType nextType;

    public DownConvertionRecipe(CompressedType type)
    {
        currentType = type;
        nextType = CompressedType.values()[type.ordinal() + 1];
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world)
    {
        if (inventory.getSizeInventory() == 1)
        {
            if (inventory.getStackInSlot(0).itemID == id)
            {
                if (inventory.getStackInSlot(0).getItemDamage() == nextType.ordinal())
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory)
    {
        ItemStack result = null;

        if (matches(inventory, null))
        {
            ItemStack stack = inventory.getStackInSlot(0);
            result = getRecipeOutput();
            result.setTagCompound(inventory.getStackInSlot(0).getTagCompound());
        }

        return result;
    }

    @Override
    public int getRecipeSize()
    {
        return 1;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return new ItemStack(id, 9, currentType.ordinal());
    }
}