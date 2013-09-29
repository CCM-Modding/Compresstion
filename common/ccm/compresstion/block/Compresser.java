package ccm.compresstion.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;

import ccm.compresstion.Compresstion;
import ccm.nucleum.omnium.tileentity.InventoryTE;
import ccm.nucleum.omnium.utils.handler.TileHandler;

public class Compresser extends BlockContainer
{
    public Compresser(final int id, final Material material)
    {
        super(id, material);
        Compresstion.instance.getLogger().debug(id);
        setUnlocalizedName("compresser");
        setTextureName("compresstion:compresser");
        GameRegistry.registerBlock(this, getUnlocalizedName());
    }

    @Override
    public TileEntity createNewTileEntity(final World world)
    {
        return ((InventoryTE) TileHandler.getTileInstance(getUnlocalizedName())).setInventorySize(2);
    }
}