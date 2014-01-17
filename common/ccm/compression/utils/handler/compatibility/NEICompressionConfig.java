package ccm.compression.utils.handler.compatibility;

import java.util.ArrayList;
import java.util.List;

import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import ccm.compression.Compression;
import ccm.compression.block.CompressedType;
import ccm.compression.block.ModBlocks;
import ccm.compression.tileentity.CompressedTile;
import ccm.compression.utils.lib.Properties;
import ccm.nucleum.omnium.utils.helper.FunctionHelper;

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
                CompressedTile.fakeSave(stack, id, 0);
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