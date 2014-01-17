/**
 * CCM Modding, Compression
 */
package ccm.compression.utils.handler.configuration;

import net.minecraft.block.Block;

import ccm.compression.utils.lib.Archive;
import ccm.compression.utils.lib.Properties;
import ccm.nucleum.omnium.utils.handler.configuration.IConfig;

import lib.cofh.util.ConfigHandler;

/**
 * CompresstionConfig
 * 
 * @author Captain_Shadows
 */
public class CompresstionConfig extends IConfig
{
    @Override
    protected void initCommon(ConfigHandler config)
    {
        config.addBlock(Archive.COMPRESSOR);
        config.addBlock(Archive.COMPRESSED);
        String blackList = config.get(UNIVERSAL, "BlackList", getBlackList());
        String[] ids = blackList.split(",");
        for (String s : ids)
        {
            Properties.blackList.add(Integer.valueOf(s));
        }
    }

    private String getBlackList()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Block.lockedChest.blockID);
        sb.append(",");
        sb.append(Block.grass.blockID);
        sb.append(",");
        sb.append(Block.pumpkin.blockID);
        sb.append(",");
        sb.append(Block.pumpkinLantern.blockID);
        return sb.toString();
    }
}