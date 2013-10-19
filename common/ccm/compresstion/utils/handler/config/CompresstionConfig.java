/**
 * CCM Modding, Compresstion
 */
package ccm.compresstion.utils.handler.config;

import static ccm.compresstion.utils.lib.Properties.BLOCK_COMPRESSED_ID;
import static ccm.compresstion.utils.lib.Properties.BLOCK_COMPRESSED_ID_DEFAULT;
import static ccm.compresstion.utils.lib.Properties.BLOCK_COMPRESSER_ID;
import static ccm.compresstion.utils.lib.Properties.BLOCK_COMPRESSER_ID_DEFAULT;

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
        BLOCK_COMPRESSER_ID = config.getBlock("Compresser", BLOCK_COMPRESSER_ID_DEFAULT, "This is the ID of the Compresser").getInt();

        BLOCK_COMPRESSED_ID = config.getBlock("Compressed", BLOCK_COMPRESSED_ID_DEFAULT, "This is the ID of all the compressed blocks").getInt();
    }
}