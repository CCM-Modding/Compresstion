package ccm.compresstion.client.renderer.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;
import ccm.compresstion.block.CompressedType;
import ccm.compresstion.item.block.CompressedItemBlock;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.utils.helper.NBTHelper;

public class CompressedItemRenderer implements IItemRenderer 
{
	// TODO: you might want to format this	
	
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
		Block block = Block.blocksList[item.itemID];
		RenderBlocks renderer = (RenderBlocks) data[0];
		int metadata = 0;
		
		Icon original = Block.blocksList[NBTHelper.getInteger(item, Archive.NBT_COMPRESSED_BLOCK_ID)].getIcon(NBTHelper.getByte(item, Archive.NBT_COMPRESSED_BLOCK_META), 0);
        Icon overlay = CompressedType.values()[item.getItemDamage()].getOverlay();
		
		renderer.setOverrideBlockTexture(original);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderBlockAsItem(block, item.getItemDamage(), 1.0F);
        renderer.clearOverrideBlockTexture();
        
        renderer.setOverrideBlockTexture(overlay);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderBlockAsItem(block, item.getItemDamage(), 1.0F);
        renderer.clearOverrideBlockTexture();
	}

}
