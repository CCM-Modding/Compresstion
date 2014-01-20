package ccm.compression.block;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ccm.compression.utils.helper.DownConvertionRecipe;

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
    @SideOnly(Side.CLIENT)
    private Icon overlay;
    private DownConvertionRecipe recipe;

    public IRecipe getRecipe()
    {
        if (recipe == null)
        {
            recipe = new DownConvertionRecipe(this);
        }
        return recipe;
    }

    public boolean hasParent()
    {
        return !(this == SINGLE);
    }

    public CompressedType getParent()
    {
        if (hasParent())
        {
            return values()[ordinal() - 1];
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void setOverlay(Icon icon)
    {
        overlay = icon;
    }

    @SideOnly(Side.CLIENT)
    public Icon overlay()
    {
        return overlay;
    }

    @SideOnly(Side.CLIENT)
    public static Icon getOverlay(int meta)
    {
        return values()[meta].overlay();
    }

    @Override
    public String toString()
    {
        return StatCollector.translateToLocal("compressed.stage." + (ordinal() + 1));
    }
}