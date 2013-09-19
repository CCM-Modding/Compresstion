package ccm.compresstion.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import ccm.compresstion.tileentity.CompressorTile;

public class Compresser extends BlockContainer
{
    public Compresser(final int id, final Material material)
    {
        super(id, material);
        setCreativeTab(CreativeTabs.tabRedstone);
        setUnlocalizedName("compresser");
        setTextureName("compresser");
        System.out.println("\n \n \n \n");
        System.out.println(getUnlocalizedName());
        System.out.println("\n \n \n \n");
        System.out.println(getTextureName());
        System.out.println("\n \n \n \n");
    }

    @Override
    public TileEntity createNewTileEntity(final World world)
    {
        return new CompressorTile().setInventorySize(2);
    }
}