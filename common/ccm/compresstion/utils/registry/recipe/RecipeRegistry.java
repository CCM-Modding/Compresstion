package ccm.compresstion.utils.registry.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;

import ccm.compresstion.block.CompressedType;
import ccm.compresstion.utils.lib.Properties;

public final class RecipeRegistry
{
    public static void init()
    {
        for (CompressedType type : CompressedType.values())
        {
            if (CompressedType.values()[type.ordinal() + 1] != null)
            {
                GameRegistry.addRecipe(new DownConvertionRecipe(type));
            }
        }
    }
}