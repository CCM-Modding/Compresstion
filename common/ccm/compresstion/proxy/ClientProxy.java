/**
 * CCM Modding, Compresstion
 */
package ccm.compresstion.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import ccm.compresstion.Compresstion;
import ccm.compresstion.block.ModBlocks;
import ccm.compresstion.client.renderer.block.CompressedBlockRenderer;
import ccm.compresstion.client.renderer.item.CompressedItemRenderer;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.utils.handler.ResourceHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

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
        super.registerRenders();
        
        RenderingRegistry.registerBlockHandler(new CompressedBlockRenderer());
        
        MinecraftForgeClient.registerItemRenderer(Item.itemsList[ModBlocks.compressedBlock.blockID].itemID, new CompressedItemRenderer());
    }
    
    @Override
    public void registerGUI()
    {
        super.registerGUI();
        
        ResourceHandler.addGUI(Compresstion.instance, Archive.COMPRESSOR);
    }
}