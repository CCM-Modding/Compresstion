/**
 * CCM Modding, Compresstion
 */
package ccm.compresstion.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

import ccm.compresstion.Compresstion;
import ccm.compresstion.client.renderer.block.CompressedBlockRenderer;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.utils.handler.ResourceHandler;

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
    }
    
    public void registerGUI()
    {
        ResourceHandler.addGUI(Compresstion.instance, Archive.COMPRESSOR);
    }
}