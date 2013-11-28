package ccm.compresstion.block;

import java.util.Locale;

import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import ccm.compresstion.utils.registry.recipe.DownConvertionRecipe;

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
    public DownConvertionRecipe recipe = new DownConvertionRecipe(this);

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
        return StatCollector.translateToLocal("compressed." + name().toUpperCase(Locale.US) + ".name");
    }
}