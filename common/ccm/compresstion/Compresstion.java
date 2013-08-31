/**
 * CCM Modding, Compresstion
 */
package ccm.compresstion;

import static ccm.compresstion.utils.lib.Archive.MOD_ID;
import static ccm.compresstion.utils.lib.Archive.MOD_NAME;
import static ccm.compresstion.utils.lib.Archive.MOD_PREFIX;
import static ccm.compresstion.utils.lib.Locations.CLIENT_PROXY;
import static ccm.compresstion.utils.lib.Locations.SERVER_PROXY;

import net.minecraft.server.MinecraftServer;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

import ccm.compresstion.proxy.CommonProxy;
import ccm.compresstion.utils.handler.config.CompresstionConfig;
import ccm.nucleum.omnium.CCMMod;
import ccm.nucleum.omnium.IMod;
import ccm.nucleum.omnium.utils.handler.ModLoadingHandler;
import ccm.nucleum.omnium.utils.handler.config.ConfigurationHandler;

import lib.org.modstats.ModstatInfo;

@Mod(modid = MOD_ID, name = MOD_NAME, useMetadata = true)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
@ModstatInfo(prefix = MOD_PREFIX)
public class Compresstion extends CCMMod implements IMod
{

    @Instance(MOD_ID)
    public static Compresstion instance;

    @SidedProxy(serverSide = SERVER_PROXY, clientSide = CLIENT_PROXY)
    public static CommonProxy proxy;

    /**
     * The current MC Server Instance
     */
    public static MinecraftServer server;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        ModLoadingHandler.loadMod(this);

        initializeConfig(event);
        ConfigurationHandler.init(this, CompresstionConfig.class);
    }

    @EventHandler
    public void init(final FMLInitializationEvent event)
    {}

    @EventHandler
    public void PostInit(final FMLPostInitializationEvent event)
    {}
}