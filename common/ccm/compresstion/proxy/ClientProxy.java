/**
 * CCM Modding, Compresstion
 */
package ccm.compresstion.proxy;

import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.registry.RenderingRegistry;

import ccm.compresstion.Compresstion;
import ccm.compresstion.client.renderer.block.CompressedBlockRenderer;
import ccm.compresstion.client.renderer.item.CompressedItemRenderer;
import ccm.compresstion.inventory.container.ContainerCompressor;
import ccm.compresstion.inventory.gui.GuiCompressor;
import ccm.compresstion.utils.lib.Properties;
import ccm.nucleum.omnium.utils.handler.ResourceHandler;
import ccm.nucleum.omnium.utils.handler.gui.GuiHandler;

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
        MinecraftForgeClient.registerItemRenderer(Properties.BLOCK_COMPRESSED_ID + 256, new CompressedItemRenderer());
    }
    
    public void registerGUI()
    {
        // TODO Finish
        ResourceHandler.addGUI(Compresstion.instance, "");
    }
}