package ccm.compresstion.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import ccm.compresstion.block.CompressedType;
import ccm.compresstion.tileentity.CompressedTile;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

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
    
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if(!renderer.hasOverrideBlockTexture())
        {
        	TileEntity temp = world.getBlockTileEntity(x, y, z);
	        if (temp != null)
	        {
	            if (temp instanceof CompressedTile)
	            {
	                CompressedTile tile = (CompressedTile) temp;
	
	                if (tile.getBlock() != null)
	                {
	                    Icon original = tile.getBlock().getIcon(0, tile.getMeta());
	                    Icon overlay = CompressedType.getOverlay(tile.getBlockMetadata());
	
	                    renderer.setOverrideBlockTexture(original);
	                    renderer.setRenderBoundsFromBlock(block);
	                    renderer.renderStandardBlock(block, x, y, z);
	                    renderer.clearOverrideBlockTexture();
	
	                    renderer.setOverrideBlockTexture(overlay);
	                    renderer.setRenderBoundsFromBlock(block);
	                    renderer.renderStandardBlock(block, x, y, z);
	                    renderer.clearOverrideBlockTexture();
	
	                    return true;
	                }
	            }
	        }
	        
        renderer.renderStandardBlock(Block.sponge, x, y, z);
        }
        
        else
        {
        	renderer.renderStandardBlock(block, x, y, z);
        }
        
        return false;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
}