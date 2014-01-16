package ccm.compression.block;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

import ccm.compression.Compression;
import ccm.compression.utils.lib.Archive;

public final class ModBlocks
{
    public static Block compressor;
    public static CompressedBlock compressedBlock;

    public static void init()
    {
        compressor = new CompressorBlock(getID(Archive.COMPRESSOR)).setCreativeTab(CreativeTabs.tabBlock);
        compressedBlock = new CompressedBlock(getID(Archive.COMPRESSED));
    }

    private static int getID(String name)
    {
        int id = Compression.instance.config().getBlockID(name);
        return id;
    }
}