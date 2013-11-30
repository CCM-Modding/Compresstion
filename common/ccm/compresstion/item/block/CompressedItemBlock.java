package ccm.compresstion.item.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import ccm.compresstion.block.CompressedType;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.utils.helper.NBTHelper;
import ccm.nucleum.omnium.utils.lib.Properties;

public class CompressedItemBlock extends ItemBlockWithMetadata
{

    public CompressedItemBlock(int id, Block block)
    {
        super(id, block);
    }

    @Override
    public String getItemDisplayName(ItemStack item)
    {
        return getCompressedName(item);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List list, boolean par4)
    {
        CompressedType type = CompressedType.values()[item.getItemDamage()];

        list.add("A single Block of this type contains: " + ((long) Math.pow(9, (type.ordinal() + 1))));
        list.add(getCompressedName(item));

        if (Properties.DEBUG)
        {
            list.add("The Orginal Block has an ID of: " + NBTHelper.getInteger(item, Archive.NBT_COMPRESSED_BLOCK_ID));
            list.add("The Orginal Block has an Metadata of: " + NBTHelper.getByte(item, Archive.NBT_COMPRESSED_BLOCK_META));
        }
        super.addInformation(item, player, list, par4);
    }

    private String getCompressedName(ItemStack item)
    {
        if (item != null)
        {
            int blockID = NBTHelper.getInteger(item, Archive.NBT_COMPRESSED_BLOCK_ID);
            int blockMeta = NBTHelper.getByte(item, Archive.NBT_COMPRESSED_BLOCK_META);

            Block block = Block.blocksList[blockID];

            StringBuilder sb = new StringBuilder();
            sb.append(StatCollector.translateToLocalFormatted(CompressedType.values()[item.getItemDamage()].toString()));
            sb.append(" ");

            if (block == null)
            {
                sb.append(StatCollector.translateToLocalFormatted("compressed.name", "ERROR"));
            } else
            {
                List<ItemStack> list = new ArrayList<ItemStack>();
                block.getSubBlocks(blockID, null, list);

                ItemStack stack = null;

                for (ItemStack i : list)
                {
                    if (i.getItemDamage() == blockMeta)
                    {
                        stack = i;
                        break;
                    }
                }

                if (stack == null)
                {
                    stack = new ItemStack(block);
                }
                sb.append(StatCollector.translateToLocalFormatted("compressed.name", stack.getDisplayName()));
            }
            return sb.toString();
        }
        return "ITEM IS NULL";
    }
}