package ccm.compresstion.utils.registry.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import ccm.compresstion.block.CompressedType;
import ccm.compresstion.block.ModBlocks;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.utils.helper.NBTHelper;

public class DownConvertionRecipe implements IRecipe
{
    private final int id = ModBlocks.compressedBlock.blockID;

    CompressedType currentType;
    CompressedType previusType;

    public DownConvertionRecipe(CompressedType type)
    {
        currentType = type;

        if (currentType == CompressedType.SINGLE)
        {
            previusType = null;
        } else
        {
            previusType = CompressedType.values()[type.ordinal() - 1];
        }
    }

    public boolean matches(ItemStack s)
    {
        if (s.itemID == id)
        {
            if (s.getItemDamage() == currentType.ordinal())
            {
                return true;
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
        ItemStack result = null;

        for (int i = 0; i <= inventory.getSizeInventory(); i++)
        {
            if (matches(inventory.getStackInSlot(i)))
            {
                result = getRecipeOutput(inventory.getStackInSlot(i));
            }
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
        return new ItemStack(id, 9, previusType.ordinal());
    }

    public ItemStack getRecipeOutput(ItemStack item)
    {
        ItemStack stack = null;
        if (previusType != null)
        {
            stack = new ItemStack(id, 9, previusType.ordinal());
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