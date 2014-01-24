package ccm.compression.api;

import java.util.ArrayList;
import java.util.List;

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
    private static final List<ItemStack> whiteList = new ArrayList<ItemStack>();
    private static final List<ItemStack> blackList = new ArrayList<ItemStack>();

    public static void addWhiteList(ItemStack s)
    {
        if (s != null)
        {
            whiteList.add(s);
        }
    }

    public static ItemStack getWhiteListData(ItemStack s)
    {
        return s != null ? whiteList.get(whiteList.indexOf(s)) : null;
    }

    public static boolean whiteListed(ItemStack s)
    {
        return isListed(s, whiteList);
    }

    public static void addBlackList(ItemStack s)
    {
        if (s != null)
        {
            blackList.add(s);
        }
    }

    public static boolean blackListed(ItemStack s)
    {
        return isListed(s, blackList);
    }

    private static boolean isListed(ItemStack s, List<ItemStack> list)
    {
        if (s != null)
        {
            for (ItemStack tmp : list)
            {
                if (tmp != null)
                {
                    if (tmp.itemID == s.itemID)
                    {
                        if ((tmp.getItemDamage() == s.getItemDamage()) || (tmp.getItemDamage() == OreDictionary.WILDCARD_VALUE))
                        {
                            if ((tmp.getTagCompound() == null) && (s.getTagCompound() == null))
                            {
                                return true;
                            } else if ((tmp.getTagCompound() != null) && (s.getTagCompound() != null))
                            {
                                if (NBTHelper.getBoolean(tmp, "wildcard") || tmp.getTagCompound().equals(s.getTagCompound()))
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