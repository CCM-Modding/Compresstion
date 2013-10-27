/**
 * CCM Modding, Compresstion
 */
package ccm.compresstion.proxy;

import ccm.compresstion.inventory.container.ContainerCompressor;
import ccm.compresstion.inventory.gui.GuiCompressor;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.utils.handler.gui.GuiHandler;

/**
 * CommonProxy
 * <p>
 * 
 * @author Captain_Shadows
 */
public class CommonProxy
{
    public void registerRenders()
    {}
    
    public void registerGUI()
    {
        GuiHandler.addGui(Archive.COMPRESSOR, new ContainerCompressor(null), new GuiCompressor(null));
    }
}