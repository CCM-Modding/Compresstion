package ccm.compresstion.block;

import net.minecraft.util.Icon;

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