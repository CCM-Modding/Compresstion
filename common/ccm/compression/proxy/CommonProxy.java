/**
 * CCM Modding, Compression
 */
package ccm.compression.proxy;

import ccm.compression.inventory.container.ContainerCompressor;
import ccm.compression.utils.lib.Archive;
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
        GuiHandler.addContainer(Archive.COMPRESSOR, new ContainerCompressor(null));
    }
}