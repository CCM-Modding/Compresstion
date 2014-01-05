/**
 * CCM Modding, Compression
 */
package ccm.compression.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import ccm.compression.Compression;
import ccm.compression.block.ModBlocks;
import ccm.compression.client.renderer.block.CompressedBlockRenderer;
import ccm.compression.client.renderer.item.CompressedItemRenderer;
import ccm.compression.inventory.container.ContainerCompressor;
import ccm.compression.inventory.gui.GuiCompressor;
import ccm.compression.utils.lib.Archive;
import ccm.nucleum.omnium.inventory.gui.element.FireElement;
import ccm.nucleum.omnium.inventory.gui.element.ProgressElement;
import ccm.nucleum.omnium.utils.handler.ResourceHandler;
import ccm.nucleum.omnium.utils.handler.gui.GuiHandler;
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
        GuiHandler.addGui(Archive.COMPRESSOR, new ContainerCompressor(null), new GuiCompressor(null));
        
        ResourceHandler.addGUI(Compression.instance, Archive.COMPRESSOR);
        ResourceHandler.addElement(Compression.instance, FireElement.ELEMENT_NAME, Archive.COMPRESSOR);
        ResourceHandler.addElement(Compression.instance, ProgressElement.ELEMENT_NAME, Archive.COMPRESSOR);
    }
}