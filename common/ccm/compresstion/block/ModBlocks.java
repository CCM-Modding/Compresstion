package ccm.compresstion.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import cpw.mods.fml.common.registry.GameRegistry;

import ccm.compresstion.tileentity.CompressedTile;
import ccm.compresstion.tileentity.CompressorTile;
import ccm.compresstion.utils.lib.Properties;
import ccm.nucleum.omnium.utils.handler.TileHandler;

public class ModBlocks
{
    public static Block compresser;

    public static Block compressedBlock;

    public static void init()
    {
        compresser = new Compresser(Properties.BLOCK_COMPRESSER_ID, Material.rock).setCreativeTab(CreativeTabs.tabBlock);
        compressedBlock = new Compressed(Properties.BLOCK_COMPRESSED_ID, Material.rock);
        
        TileHandler.registerTile("CCM.COMPRESSED.TILE.COMPRESSED", new CompressedTile());
        TileHandler.registerTile("CCM.COMPRESSED.TILE.COMPRESSER", new CompressorTile().setInventorySize(2));
    }
}