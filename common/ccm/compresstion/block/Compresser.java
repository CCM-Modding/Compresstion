package ccm.compresstion.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import ccm.compresstion.tileentity.CompressorTile;

public class Compresser extends BlockContainer
{
    public Compresser(final int id, final Material material)
    {
        super(id, material);
        setUnlocalizedName("compresser");
        setTextureName("compresser");
    }

    @Override
    public TileEntity createNewTileEntity(final World world)
    {
        return new CompressorTile().setInventorySize(2);
    }
}