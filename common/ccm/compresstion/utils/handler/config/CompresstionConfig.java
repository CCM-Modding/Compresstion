/**
 * CCM Modding, Compresstion
 */
package ccm.compresstion.utils.handler.config;

import static ccm.compresstion.utils.lib.Properties.BLOCK_COMPRESSED_ID;
import static ccm.compresstion.utils.lib.Properties.BLOCK_COMPRESSED_ID_DEFAULT;
import static ccm.compresstion.utils.lib.Properties.BLOCK_COMPRESSOR_ID;
import static ccm.compresstion.utils.lib.Properties.BLOCK_COMPRESSOR_ID_DEFAULT;

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
        BLOCK_COMPRESSOR_ID = config.getBlockId("Compressor");

        BLOCK_COMPRESSED_ID = config.getBlockId("Compressed");
    }
}