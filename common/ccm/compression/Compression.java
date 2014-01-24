/**
 * CCM Modding, Compression
 */
package ccm.compression;

import static ccm.compression.utils.lib.Archive.MOD_ID;
import static ccm.compression.utils.lib.Locations.CLIENT_PROXY;
import static ccm.compression.utils.lib.Locations.SERVER_PROXY;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

import ccm.compression.block.CompressedType;
import ccm.compression.block.ModBlocks;
import ccm.compression.proxy.CommonProxy;
import ccm.compression.utils.handler.CompresstionConfig;
import ccm.compression.utils.handler.IMCHandler;
import ccm.nucleum.omnium.CCMMod;

@Mod(modid = MOD_ID, useMetadata = true, dependencies = "required-after:nucleum_omnium")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class Compression extends CCMMod
{
    @Instance(MOD_ID)
    public static Compression instance;
    @SidedProxy(serverSide = SERVER_PROXY, clientSide = CLIENT_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    @SuppressWarnings("unused")
    public void preInit(final FMLPreInitializationEvent event)
    {
        loadMod(this, new CompresstionConfig());
        ModBlocks.init();
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void init(final FMLInitializationEvent event)
    {
        proxy.registerRenders();
        proxy.registerGUI();
        for (CompressedType type : CompressedType.values())
        {
            GameRegistry.addRecipe(type.getRecipe());
        }
        // THIS IS WHAT HAPPENS WHEN I MAKE THE RECIPE!!
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.compressor),
                                                   "xax",
                                                   "i i",
                                                   "xxx",
                                                   'a',
                                                   Block.anvil,
                                                   'i',
                                                   Item.ingotIron,
                                                   'x',
                                                   "cobblestone"));
    }

    @EventHandler
    public void handleIMCMessages(IMCEvent event)
    {
        IMCHandler.processIMCMessages(event);
    }
}