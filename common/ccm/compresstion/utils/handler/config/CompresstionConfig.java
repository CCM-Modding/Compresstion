/**
 * CCM Modding, Compresstion
 */
package ccm.compresstion.utils.handler.config;

import ccm.compresstion.utils.lib.Archive;
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
    public void init()
    {
        config.addBlockEntry(Archive.COMPRESSOR);

        config.addBlockEntry("Compressed");
    }
}