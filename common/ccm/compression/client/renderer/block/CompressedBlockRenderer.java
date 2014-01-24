package ccm.compression.client.renderer.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

import ccm.compression.block.CompressedType;
import ccm.compression.tileentity.CompressedTile;

public class CompressedBlockRenderer implements ISimpleBlockRenderingHandler
{
    public final static int id = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public int getRenderId()
    {
        return id;
    }

    @Override
    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    // Had to do that cause the else clause IS important
    @Override
    @SuppressWarnings("all")
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (!renderer.hasOverrideBlockTexture())
        {
            TileEntity temp = world.getBlockTileEntity(x, y, z);
            if (temp != null)
            {
                if (temp instanceof CompressedTile)
                {
                    CompressedTile tile = (CompressedTile) temp;
                    if (tile.data().getBlock() != null)
                    {
                        if (!tile.data().hasRender())
                        {
                            renderer.setRenderBoundsFromBlock(block);
                            renderer.renderStandardBlock(block, x, y, z);
                            renderer.setOverrideBlockTexture(CompressedType.getOverlay(tile.getBlockMetadata()));
                            renderer.setRenderBoundsFromBlock(block);
                            renderer.renderStandardBlock(block, x, y, z);
                            renderer.clearOverrideBlockTexture();
                            return true;
                        } else
                        {
                            return tile.data().getRender().renderBlock(world, x, y, z, block, renderer);
                        }
                    }
                }
            }
        }
        renderer.renderStandardBlock(block, x, y, z);
        return false;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.DOWN.ordinal(), metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.UP.ordinal(), metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.NORTH.ordinal(), metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.SOUTH.ordinal(), metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.WEST.ordinal(), metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.EAST.ordinal(), metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
}