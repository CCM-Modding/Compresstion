package ccm.compression.utils.handler;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import ccm.compression.Compression;
import ccm.compression.block.ModBlocks;

// TODO REDO
public class NEICompressionConfig implements IConfigureNEI
{
    @Override
    public void loadConfig()
    {
        API.hideItem(ModBlocks.compressedBlock.blockID);
        // MultiItemRange tmp = new MultiItemRange();
        // tmp.add(ModBlocks.compressedBlock, 0, CompressedType.values().length);
        // API.addSetRange("Mod." + Compression.instance.name() + ".Compressed", tmp);
        // for (int id : getPossibleIDs())
        // {
        // for (CompressedType type : CompressedType.values())
        // {
        // ItemStack stack = new ItemStack(ModBlocks.compressedBlock.blockID, 1, type.ordinal());
        // CompressedData.writeToItemStack(stack, id, 0);
        // API.addNBTItem(stack);
        // }
        // }
    }

    /*
     * private List<Integer> getPossibleIDs()
     * {
     * List<Integer> ids = new ArrayList<Integer>();
     * for (Block b : Block.blocksList)
     * {
     * if (b != null)
     * {
     * if (b.blockID != 0)
     * {
     * if (FunctionHelper.isNormalBlock(b, 0))
     * {
     * if (!Properties.blackList.contains(b.blockID))
     * {
     * ids.add(b.blockID);
     * }
     * }
     * }
     * }
     * }
     * return ids;
     * }
     */
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