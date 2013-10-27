package ccm.compresstion.block;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

import ccm.compresstion.Compresstion;
import ccm.compresstion.utils.lib.Archive;
import ccm.compresstion.utils.lib.Properties;

public final class ModBlocks
{
    public static Block compressor;

    public static Block compressedBlock;

    public static void init()
    {
        compressor = new Compressor(Compresstion.instance.getConfigHandler().getBlockId(Archive.COMPRESSOR), "compressor").setCreativeTab(CreativeTabs.tabBlock);

        compressedBlock = new Compressed(Compresstion.instance.getConfigHandler().getBlockId("Compressed"), "compressed");
    }
}