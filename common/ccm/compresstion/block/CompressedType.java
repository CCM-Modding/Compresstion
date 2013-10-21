package ccm.compresstion.block;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;

import ccm.compresstion.utils.lib.Properties;
import ccm.compresstion.utils.registry.recipe.DownConvertionRecipe;
import ccm.nucleum.omnium.utils.helper.JavaHelper;

public enum CompressedType
{
    SINGLE,
    DOUBLE,
    TRIPLE,
    QUADRUPLE,
    QUINTUPLE,
    SEXTUPLE,
    SEPTUPLE,
    OCTUPLE,
    NONUPLE,
    DECUPLE,
    UNDECUPLE,
    DUODECUPLE,
    TREDECUPLE,
    QUATTORDECUPLE,
    QUINDECOUPLE,
    SEDECOUPLE;

    private Icon overlay;

    public Icon getIcon()
    {
        return overlay;
    }

    public void setIcon(Icon icon)
    {
        overlay = icon;
    }

    @Override
    public String toString()
    {
        return JavaHelper.titleCase(name().toLowerCase());
    }
}