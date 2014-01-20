package ccm.compression.client.renderer.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.ForgeDirection;

import ccm.compression.block.CompressedType;
import ccm.compression.utils.helper.CompressedData;

public class CompressedItemRenderer implements IItemRenderer
{
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        if (item.getTagCompound() != null)
        {
            CompressedData cData = new CompressedData();
            cData.readFromNBT(item.getTagCompound());
            RenderBlocks renderer = (RenderBlocks) data[0];
            Tessellator tessellator = Tessellator.instance;
            Block block = Block.stone;
            if (cData.getBlock() != null)
            {
                Icon overlay = CompressedType.getOverlay(item.getItemDamage());
                renderer.setRenderBoundsFromBlock(block);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                renderer.renderFaceYNeg(block, 0, 0, 0, cData.getIcon(ForgeDirection.DOWN));
                renderer.renderFaceYNeg(block, 0, 0, 0, overlay);
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderer.renderFaceYPos(block, 0, 0, 0, cData.getIcon(ForgeDirection.UP));
                renderer.renderFaceYPos(block, 0, 0, 0, overlay);
                tessellator.setNormal(0.0F, 0.0F, -1.0F);
                renderer.renderFaceZNeg(block, 0, 0, 0, cData.getIcon(ForgeDirection.NORTH));
                renderer.renderFaceZNeg(block, 0, 0, 0, overlay);
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderer.renderFaceZPos(block, 0, 0, 0, cData.getIcon(ForgeDirection.SOUTH));
                renderer.renderFaceZPos(block, 0, 0, 0, overlay);
                tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                renderer.renderFaceXNeg(block, 0, 0, 0, cData.getIcon(ForgeDirection.WEST));
                renderer.renderFaceXNeg(block, 0, 0, 0, overlay);
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderer.renderFaceXPos(block, 0, 0, 0, cData.getIcon(ForgeDirection.EAST));
                renderer.renderFaceXPos(block, 0, 0, 0, overlay);
                tessellator.draw();
            }
        }
    }
}