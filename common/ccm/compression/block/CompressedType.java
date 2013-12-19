package ccm.compression.block;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import ccm.compression.utils.helper.recipe.DownConvertionRecipe;

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
    private DownConvertionRecipe recipe;

    public IRecipe getRecipe()
    {
        if (recipe == null)
        {
            recipe = new DownConvertionRecipe(this);
        }
        return recipe;
    }

    public void setIcon(Icon icon)
    {
        overlay = icon;
    }

    public Icon getOverlay()
    {
        return overlay;
    }

    public static Icon getOverlay(int meta)
    {
        return values()[meta].getOverlay();
    }

    @Override
    public String toString()
    {
        return StatCollector.translateToLocal("compressed." + name().toUpperCase() + ".name");
    }
}