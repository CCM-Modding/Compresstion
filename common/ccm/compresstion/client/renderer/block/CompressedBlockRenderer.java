package ccm.compresstion.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

import ccm.compresstion.block.CompressedType;

public class CompressedBlockRenderer implements ISimpleBlockRenderingHandler
{
    public final static int id = RenderingRegistry.getNextAvailableRenderId();

    public static byte currentRenderPass;

    @Override
    public void renderInventoryBlock(final Block block, final int metadata, final int modelID, final RenderBlocks renderer)
    {}

    @Override
    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block, final int modelId, final RenderBlocks renderer)
    {
        // which render pass are we doing?
        if (currentRenderPass == 0)
        {
            // we are on the solid block render pass, lets render the default texture
            renderer.renderStandardBlock(block, x, y, z);
        } else
        {
            // we are on the alpha render pass, draw the overlay
            renderer.renderBlockUsingTexture(block, x, y, z, CompressedType.values()[world.getBlockMetadata(x, y, z)].getOverlay());
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory()
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return id;
    }
}