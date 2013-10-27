package ccm.compresstion.utils.registry.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import ccm.compresstion.Compresstion;
import ccm.compresstion.block.CompressedType;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.utils.helper.NBTHelper;

public class DownConvertionRecipe implements IRecipe
{
    private int id = Compresstion.instance.getConfigHandler().getBlockId("Compressed") + 256;

    CompressedType currentType;
    CompressedType preType;

    public DownConvertionRecipe(CompressedType type)
    {
        currentType = type;
        try
        {
            preType = CompressedType.values()[type.ordinal() - 1];
        } catch (Exception e)
        {
            preType = null;
        }
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world)
    {
        if (inventory.getSizeInventory() == 1)
        {
            if (inventory.getStackInSlot(0).itemID == id)
            {
                if (inventory.getStackInSlot(0).getItemDamage() == currentType.ordinal())
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
            result = getRecipeOutput(inventory.getStackInSlot(0));
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
        return new ItemStack(id, 9, preType.ordinal());
    }

    public ItemStack getRecipeOutput(ItemStack item)
    {
        ItemStack stack = null;
        if (preType != null)
        {
            stack = new ItemStack(id, 9, preType.ordinal());
            stack.setTagCompound(item.getTagCompound());
        } else
        {
            int item_id = NBTHelper.getInteger(item, Archive.NBT_COMPRESSED_BLOCK_ID);
            int item_meta = NBTHelper.getInteger(item, Archive.NBT_COMPRESSED_BLOCK_META);
            stack = new ItemStack(item_id, 9, item_meta);
        }
        return stack;
    }
}