/**
 * CCM Modding, Compresstion
 */
package ccm.compresstion.utils.handler.config;

import ccm.nucleum.omnium.configuration.AdvConfiguration;
import ccm.nucleum.omnium.utils.handler.config.IConfig;

/**
 * CompresstionConfig
 * <p>
 * 
 * @author Captain_Shadows
 */
public class CompresstionConfig implements IConfig
{
    private AdvConfiguration config;

    @Override
    public AdvConfiguration getConfiguration()
    {
        return config;
    }

    @Override
    public IConfig setConfiguration(final AdvConfiguration config)
    {
        this.config = config;
        return this;
    }

    @Override
    public void init()
    {}
}