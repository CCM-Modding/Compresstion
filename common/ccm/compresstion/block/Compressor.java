package ccm.compresstion.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import cpw.mods.fml.common.registry.GameRegistry;

import ccm.compresstion.Compresstion;
import ccm.compresstion.tileentity.CompressorTile;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.tileentity.InventoryTE;
import ccm.nucleum.omnium.utils.handler.TileHandler;

public class Compressor extends BlockContainer
{
    private Icon top, bottom, sides;

    public Compressor(final int id, String name)
    {
        super(id, Material.rock);
        setUnlocalizedName(name);
        GameRegistry.registerBlock(this, getUnlocalizedName());
        TileHandler.registerTile(getUnlocalizedName(), CompressorTile.class);
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        top = register.registerIcon(Archive.MOD_NAME.toLowerCase() + ":compressorTop");
        sides = register.registerIcon(Archive.MOD_NAME.toLowerCase()  + ":compressorSide");
        bottom = register.registerIcon(Archive.MOD_NAME.toLowerCase()  + ":compressorBottom");
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        if (side == ForgeDirection.UP.ordinal())
        {
            return top;
        } else if (side == ForgeDirection.DOWN.ordinal())
        {
            return bottom;
        } else
        {
            return sides;
        }
    }

    @Override
    public TileEntity createNewTileEntity(final World world)
    {
        return ((InventoryTE) TileHandler.getTileInstance(getUnlocalizedName())).setInventorySize(2);
    }
}