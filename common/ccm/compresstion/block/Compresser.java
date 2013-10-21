package ccm.compresstion.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;

import ccm.compresstion.Compresstion;
import ccm.compresstion.tileentity.CompressorTile;
import ccm.nucleum.omnium.tileentity.InventoryTE;
import ccm.nucleum.omnium.utils.handler.TileHandler;

public class Compresser extends BlockContainer
{
    public Compresser(final int id, String name)
    {
        super(id, Material.rock);
        setUnlocalizedName(name);
        setTextureName(name);
        GameRegistry.registerBlock(this, getUnlocalizedName());
        TileHandler.registerTile(getUnlocalizedName(), CompressorTile.class);
    }

    @Override
    public TileEntity createNewTileEntity(final World world)
    {
        return ((InventoryTE) TileHandler.getTileInstance(getUnlocalizedName())).setInventorySize(2);
    }
    
    @Override
    public Block setTextureName(String name)
    {
        return super.setTextureName("compresstion:" + name);
    }
}