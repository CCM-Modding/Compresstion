package ccm.compression.client.renderer.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

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
        GL11.glPushMatrix();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        Block block = Block.blocksList[item.itemID];
        RenderBlocks renderer = (RenderBlocks) data[0];

        Icon original = Block.blocksList[NBTHelper.getInteger(item, Archive.NBT_COMPRESSED_BLOCK_ID)].getIcon(0, NBTHelper.getByte(item, Archive.NBT_COMPRESSED_BLOCK_META));
        Icon overlay = CompressedType.getOverlay(item.getItemDamage());

        renderer.setOverrideBlockTexture(original);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderBlockAsItem(block, item.getItemDamage(), 1.0F);
        renderer.clearOverrideBlockTexture();

        renderer.setOverrideBlockTexture(overlay);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderBlockAsItem(block, item.getItemDamage(), 1.0F);
        renderer.clearOverrideBlockTexture();

        GL11.glPopMatrix();
    }
}