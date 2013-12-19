package ccm.compression.block;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import ccm.compression.Compression;
import ccm.compression.utils.lib.Archive;

public final class ModBlocks
{
    public static Block compressor;

    public static Block compressedBlock;

    public static void init()
    {
        compressor = new Compressor(Compression.instance.getConfigHandler().getBlockId(Archive.COMPRESSOR)).setCreativeTab(CreativeTabs.tabBlock);

        compressedBlock = new Compressed(Compression.instance.getConfigHandler().getBlockId(Compressed.name));
    }
}