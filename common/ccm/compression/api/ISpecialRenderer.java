package ccm.compression.api;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

/**
 * ISpecialRenderer
 * <p>
 * 
 * @author Captain_Shadows
 */
public interface ISpecialRenderer
{
    public boolean renderBlock(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer);

    public void renderItem(ItemRenderType type, ItemStack item, Object... data);
}