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
        compressor = new Compressor(getID(Archive.COMPRESSOR)).setCreativeTab(CreativeTabs.tabBlock);
        compressedBlock = new Compressed(getID(Compressed.name));
    }

    private static int getID(String name)
    {
        int id = Compression.instance.config().getBlockID(name);
        Compression.instance.logger().debug("Getting id: %s for block: %s", id, name);
        return id;
    }
}