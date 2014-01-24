package ccm.compression.client.renderer.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.ForgeDirection;

import ccm.compression.api.CompressedData;
import ccm.compression.block.CompressedType;

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
                if (!cData.hasRender())
                {
                    Icon backup = cData.getBlock().getIcon(0, cData.getMeta());
                    Icon overlay = CompressedType.getOverlay(item.getItemDamage());
                    if (backup == null)
                    {
                        return;
                    }
                    renderer.setRenderBoundsFromBlock(block);
                    tessellator.startDrawingQuads();
                    Icon sided = cData.getBlock().getIcon(ForgeDirection.DOWN.ordinal(), cData.getMeta());
                    tessellator.setNormal(0.0F, -1.0F, 0.0F);
                    renderer.renderFaceYNeg(block, 0, 0, 0, sided != null ? sided : backup);
                    renderer.renderFaceYNeg(block, 0, 0, 0, overlay);
                    sided = cData.getBlock().getIcon(ForgeDirection.UP.ordinal(), cData.getMeta());
                    tessellator.setNormal(0.0F, 1.0F, 0.0F);
                    renderer.renderFaceYPos(block, 0, 0, 0, sided != null ? sided : backup);
                    renderer.renderFaceYPos(block, 0, 0, 0, overlay);
                    sided = cData.getBlock().getIcon(ForgeDirection.NORTH.ordinal(), cData.getMeta());
                    tessellator.setNormal(0.0F, 0.0F, -1.0F);
                    renderer.renderFaceZNeg(block, 0, 0, 0, sided != null ? sided : backup);
                    renderer.renderFaceZNeg(block, 0, 0, 0, overlay);
                    sided = cData.getBlock().getIcon(ForgeDirection.SOUTH.ordinal(), cData.getMeta());
                    tessellator.setNormal(0.0F, 0.0F, 1.0F);
                    renderer.renderFaceZPos(block, 0, 0, 0, sided != null ? sided : backup);
                    renderer.renderFaceZPos(block, 0, 0, 0, overlay);
                    sided = cData.getBlock().getIcon(ForgeDirection.WEST.ordinal(), cData.getMeta());
                    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                    renderer.renderFaceXNeg(block, 0, 0, 0, sided != null ? sided : backup);
                    renderer.renderFaceXNeg(block, 0, 0, 0, overlay);
                    sided = cData.getBlock().getIcon(ForgeDirection.EAST.ordinal(), cData.getMeta());
                    tessellator.setNormal(1.0F, 0.0F, 0.0F);
                    renderer.renderFaceXPos(block, 0, 0, 0, sided != null ? sided : backup);
                    renderer.renderFaceXPos(block, 0, 0, 0, overlay);
                    tessellator.draw();
                } else
                {
                    cData.getRender().renderItem(type, item, data);
                }
            }
        }
    }
}