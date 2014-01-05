package ccm.compression.utils.handler.compatibility;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import ccm.compression.Compression;
import ccm.compression.block.CompressedType;
import ccm.compression.block.ModBlocks;
import ccm.compression.utils.lib.Archive;
import ccm.compression.utils.lib.Properties;
import ccm.nucleum.omnium.utils.helper.FunctionHelper;
import ccm.nucleum.omnium.utils.helper.NBTHelper;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEICompressionConfig implements IConfigureNEI
{
    @Override
    public void loadConfig()
    {
        // API.hideItem(ModBlocks.compressedBlock.blockID);
        MultiItemRange tmp = new MultiItemRange();
        tmp.add(ModBlocks.compressedBlock, 0, CompressedType.values().length);
        API.addSetRange("Mod." + Compression.instance.name() + ".Compressed", tmp);
        for (int id : getPossibleIDs())
        {
            for (CompressedType type : CompressedType.values())
            {
                ItemStack stack = new ItemStack(ModBlocks.compressedBlock.blockID, 1, type.ordinal());
                NBTHelper.setInteger(stack, Archive.NBT_COMPRESSED_BLOCK_ID, id);
                NBTHelper.setByte(stack, Archive.NBT_COMPRESSED_BLOCK_META, (byte) 0);
                API.addNBTItem(stack);
            }
        }
    }

    private List<Integer> getPossibleIDs()
    {
        List<Integer> ids = new ArrayList<Integer>();
        for (Block b : Block.blocksList)
        {
            if (b != null)
            {
                if (b.blockID != 0)
                {
                    if (FunctionHelper.isNormalBlock(b, 0))
                    {
                        if (!Properties.blackList.contains(b.blockID))
                        {
                            ids.add(b.blockID);
                        }
                    }
                }
            }
        }
        return ids;
    }

    @Override
    public String getName()
    {
        return Compression.instance.name() + "'s NEI Compatibility";
    }

    @Override
    public String getVersion()
    {
        return Compression.instance.version();
    }
}
