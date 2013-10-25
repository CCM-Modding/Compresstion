package ccm.compresstion.block;

import java.util.Locale;

import net.minecraft.util.StatCollector;

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

    @Override
    public String toString()
    {
        return StatCollector.translateToLocal("compressed." + name().toUpperCase(Locale.US) + ".name");
    }
}