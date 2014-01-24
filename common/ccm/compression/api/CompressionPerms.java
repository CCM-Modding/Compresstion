package ccm.compression.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import ccm.nucleum.omnium.utils.helper.NBTHelper;

/**
 * CompressionPerms
 * <p>
 * 
 * @author Captain_Shadows
 */
public final class CompressionPerms
{
    private static final Map<ItemStack, ItemStack> whiteList = new HashMap<ItemStack, ItemStack>();
    private static final List<ItemStack> blackList = new ArrayList<ItemStack>();

    public static void addWhiteList(ItemStack key, ItemStack value)
    {
        if (key != null)
        {
            whiteList.put(key, value);
        }
    }

    public static ItemStack getWhiteListData(ItemStack key)
    {
        return whiteList.get(key);
    }

    public static boolean whiteListed(ItemStack key)
    {
        return isListed(key, whiteList.keySet());
    }

    public static void addBlackList(ItemStack item)
    {
        if (item != null)
        {
            blackList.add(item);
        }
    }

    public static boolean blackListed(ItemStack item)
    {
        return isListed(item, blackList);
    }

    private static boolean isListed(ItemStack item, Collection<ItemStack> collection)
    {
        if (item != null)
        {
            for (ItemStack tmp : collection)
            {
                if (tmp != null)
                {
                    if (tmp.itemID == item.itemID)
                    {
                        if ((tmp.getItemDamage() == item.getItemDamage()) || (tmp.getItemDamage() == OreDictionary.WILDCARD_VALUE))
                        {
                            if ((tmp.getTagCompound() == null) && (item.getTagCompound() == null))
                            {
                                return true;
                            } else if ((tmp.getTagCompound() != null) && (item.getTagCompound() != null))
                            {
                                if (NBTHelper.getBoolean(tmp, "wildcard") || tmp.getTagCompound().equals(item.getTagCompound()))
                                {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}