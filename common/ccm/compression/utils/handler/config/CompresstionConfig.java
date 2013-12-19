/**
 * CCM Modding, Compression
 */
package ccm.compression.utils.handler.config;

import lib.cofh.util.ConfigHandler;
import ccm.compression.utils.lib.Archive;
import ccm.nucleum.omnium.utils.handler.configuration.IConfig;

/**
 * CompresstionConfig
 * <p>
 * 
 * @author Captain_Shadows
 */
public class CompresstionConfig extends IConfig
{
    @Override
    public void init(ConfigHandler config)
    {
        config.addBlockEntry(Archive.COMPRESSOR);

        config.addBlockEntry("Compressed");
    }
}