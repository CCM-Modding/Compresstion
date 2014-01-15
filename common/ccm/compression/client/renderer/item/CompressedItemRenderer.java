package ccm.compression.client.renderer.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.ForgeDirection;

import ccm.compression.block.CompressedType;
import ccm.compression.utils.lib.Archive;
import ccm.nucleum.omnium.utils.helper.NBTHelper;

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
            RenderBlocks renderer = (RenderBlocks) data[0];
            Tessellator tessellator = Tessellator.instance;
            Block block = Block.stone;
            Block doner = Block.blocksList[NBTHelper.getInteger(item, Archive.NBT_COMPRESSED_BLOCK_ID)];
            if (doner != null)
            {
                int donerMeta = NBTHelper.getByte(item, Archive.NBT_COMPRESSED_BLOCK_META);
                Icon backup = doner.getIcon(0, donerMeta);
                Icon overlay = CompressedType.getOverlay(item.getItemDamage());
                if (backup == null)
                {
                    return;
                }
                renderer.setRenderBoundsFromBlock(block);
                tessellator.startDrawingQuads();
                Icon sided = doner.getIcon(ForgeDirection.DOWN.ordinal(), donerMeta);
                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                renderer.renderFaceYNeg(block, 0, 0, 0, sided != null ? sided : backup);
                renderer.renderFaceYNeg(block, 0, 0, 0, overlay);
                sided = doner.getIcon(ForgeDirection.UP.ordinal(), donerMeta);
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderer.renderFaceYPos(block, 0, 0, 0, sided != null ? sided : backup);
                renderer.renderFaceYPos(block, 0, 0, 0, overlay);
                sided = doner.getIcon(ForgeDirection.NORTH.ordinal(), donerMeta);
                tessellator.setNormal(0.0F, 0.0F, -1.0F);
                renderer.renderFaceZNeg(block, 0, 0, 0, sided != null ? sided : backup);
                renderer.renderFaceZNeg(block, 0, 0, 0, overlay);
                sided = doner.getIcon(ForgeDirection.SOUTH.ordinal(), donerMeta);
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderer.renderFaceZPos(block, 0, 0, 0, sided != null ? sided : backup);
                renderer.renderFaceZPos(block, 0, 0, 0, overlay);
                sided = doner.getIcon(ForgeDirection.WEST.ordinal(), donerMeta);
                tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                renderer.renderFaceXNeg(block, 0, 0, 0, sided != null ? sided : backup);
                renderer.renderFaceXNeg(block, 0, 0, 0, overlay);
                sided = doner.getIcon(ForgeDirection.EAST.ordinal(), donerMeta);
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderer.renderFaceXPos(block, 0, 0, 0, sided != null ? sided : backup);
                renderer.renderFaceXPos(block, 0, 0, 0, overlay);
                tessellator.draw();
            }
        }
    }
}