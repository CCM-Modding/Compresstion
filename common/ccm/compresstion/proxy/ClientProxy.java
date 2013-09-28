/**
 * CCM Modding, Compresstion
 */
package ccm.compresstion.proxy;

import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.registry.RenderingRegistry;

import ccm.compresstion.client.renderer.block.CompressedBlockRenderer;
import ccm.compresstion.client.renderer.item.CompressedItemRenderer;
import ccm.compresstion.utils.lib.Properties;

/**
 * ClientProxy
 * <p>
 * 
 * @author Captain_Shadows
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenders()
    {
        RenderingRegistry.registerBlockHandler(new CompressedBlockRenderer());
        MinecraftForgeClient.registerItemRenderer(Properties.BLOCK_COMPRESSED_ID, new CompressedItemRenderer());
    }
}