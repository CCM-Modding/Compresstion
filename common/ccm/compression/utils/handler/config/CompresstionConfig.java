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
    protected void initCommon(ConfigHandler config)
    {
        config.addBlock(Archive.COMPRESSOR);
        config.addBlock("Compressed");
    }
}