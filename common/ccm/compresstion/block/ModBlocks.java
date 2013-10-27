package ccm.compresstion.block;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

import ccm.compresstion.utils.lib.Properties;

public final class ModBlocks
{
    public static Block compressor;

    public static Block compressedBlock;

    public static void init()
    {
        compressor = new Compressor(Properties.BLOCK_COMPRESSOR_ID, "compressor").setCreativeTab(CreativeTabs.tabBlock);

        compressedBlock = new Compressed(Properties.BLOCK_COMPRESSED_ID, "compressed");
    }
}