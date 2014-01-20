package ccm.compression.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.common.Loader;

import ccm.compression.Compression;
import ccm.nucleum.omnium.utils.helper.NBTHelper;

/**
 * CompressedData
 * 
 * @author Captain_Shadows
 */
public final class CompressedData
{
    /* NBT NAMES */
    public static final String NBT_ORIGEN = "original";
    public static final String NBT_BLOCK_ID = "id";
    public static final String NBT_BLOCK_META = "meta";
    public static final String NBT_BLOCK_DATA = "data";
    public static final String NBT_TICK = "tick";
    public static final String NBT_TICK_NAME = "name";
    public static final String NBT_TICK_HAS = "has";
    /*
     * Block data
     */
    /** original block id */
    private int id;
    /** original block meta data */
    private byte meta;
    /** Any meta data that the Item had */
    private NBTTagCompound data;

    /**
     * @param id
     *            The ID of original the block
     */
    public void setBlockID(final int id)
    {
        this.id = id;
    }

    /**
     * @param meta
     *            The Meta Data of the original block
     */
    public void setBlockMeta(final int meta)
    {
        this.meta = (byte) meta;
    }

    /**
     * @param nbt
     *            The {@link NBTTagCompound} of the ItemStack that was used to create this
     */
    public void setData(NBTTagCompound nbt)
    {
        data = nbt;
    }

    /** @return The ID of the original block */
    public int getID()
    {
        return id;
    }

    /** @return The Meta data of the original block */
    public byte getMeta()
    {
        return meta;
    }

    /** @return The {@link NBTTagCompound} of the ItemStack that was used to create this */
    public NBTTagCompound getData()
    {
        return data;
    }

    /** @return The {@link Block} instance that belongs to the stored ID */
    public Block getBlock()
    {
        return Block.blocksList[id];
    }

    /*
     * ITileTick
     */
    /** Weather it has an {@link ITileTick} */
    private boolean hasTick = false;
    /** The path to it's {@link ITileTick} */
    private String tickTile;
    /** A temporally stored version so that it dosen't have to be created every time */
    private transient ITileTick tick = null;

    /**
     * @return True if the Tile is allowed to tick
     */
    public boolean tick()
    {
        return hasTick;
    }

    /**
     * @param tickTile
     *            The Canonical Name of a class that implements {@link ITileTick}
     */
    public void setTick(final String tickTile)
    {
        if ((tickTile != null) && !tickTile.equalsIgnoreCase(""))
        {
            hasTick = true;
            this.tickTile = tickTile;
        }
    }

    /**
     * @param tick
     *            An instance of {@link ITileTick}. It <b>MUST</b> be able to be reconstructed
     */
    public void setTick(final ITileTick tick)
    {
        if (tick != null)
        {
            this.tick = tick;
            setTick(tick.getClass().getCanonicalName());
        }
    }

    /**
     * @return An instance of {@link ITileTick} or null if the tile doesn't tick
     */
    public ITileTick getTick()
    {
        if (tick == null)
        {
            if (!tick())
            {
                Compression.instance.logger().bug("GET TICK HAS BEEN CALLED ON A COMPRESSED TILE THAT DOESN'T TICK");
            } else
            {
                // Create the temporal variables for internal usage
                Class<? extends ITileTick> tmpHandler = null;
                try
                {// Try to find the ITileTick Class
                    tmpHandler = (Class<? extends ITileTick>) Class.forName(tickTile, false, Loader.instance().getModClassLoader());
                } catch (ClassNotFoundException e)
                {
                    Compression.instance.logger().printCatch(e, "A compressed Tile has failed to find it's ITileTick @ %s", tickTile);
                    return null;
                }
                try
                {// Try to crate a new instance of said Class
                    tick = tmpHandler.newInstance();
                } catch (Exception e)
                {
                    Compression.instance.logger()
                                        .printCatch(e, "A compressed Tile has failed to create an instance of it's ITileTick @ %s", tickTile);
                    return null;
                }
                tick.setOwner(parent);
            }
        }// Done
        return tick;
    }

    /*
     * NBT STUFF
     */
    public void writeToNBT(final NBTTagCompound nbt)
    {
        NBTTagCompound origin = new NBTTagCompound();
        // Adding Block stuff
        origin.setInteger(NBT_BLOCK_ID, getID());
        origin.setByte(NBT_BLOCK_META, getMeta());
        if (getData() != null)
        {
            origin.setTag(NBT_BLOCK_DATA, getData());
        }
        // Done adding that. Adding Tick stuff
        NBTTagCompound tick = new NBTTagCompound();
        tick.setBoolean(NBT_TICK_HAS, tick());
        if (tick())
        {
            tick.setString(NBT_TICK_NAME, tickTile);
            getTick().writeToNBT(tick);
        }
        origin.setCompoundTag(NBT_TICK, tick);
        // Done adding them
        nbt.setCompoundTag(NBT_ORIGEN, origin);
    }

    public void readFromNBT(final NBTTagCompound nbt)
    {
        // Getting Origin
        NBTTagCompound origin = NBTHelper.getTag(nbt, NBT_ORIGEN);
        // Getting Block stuff
        setBlockID(NBTHelper.getInteger(origin, NBT_BLOCK_ID));
        setBlockMeta(NBTHelper.getByte(origin, NBT_BLOCK_META));
        setData(NBTHelper.getTag(origin, NBT_BLOCK_DATA));
        // Getting Tick Stuff
        NBTTagCompound tick = NBTHelper.getTag(origin, NBT_TICK);
        setTick(NBTHelper.getString(tick, NBT_TICK_NAME));
        if (tick())
        {
            getTick().readFromNBT(tick);
        }
    }

    /*
     * Conversion Stuff
     */
    /**
     * @param item
     *            Write all the currently stored data to the item
     */
    public void writeToItemStack(ItemStack item)
    {
        NBTTagCompound tmp = new NBTTagCompound();
        writeToNBT(tmp);
        item.setTagCompound(tmp);
    }

    /**
     * @param item
     *            The item to store data in
     * @param id
     *            The original block ID
     * @param meta
     *            The original block meta data
     */
    public static void writeToItemStack(ItemStack item, int id, int meta)
    {
        CompressedData tmp = new CompressedData();
        tmp.setBlockID(id);
        tmp.setBlockMeta((byte) meta);
        tmp.writeToItemStack(item);
    }

    /**
     * @param item
     *            The item to store data in
     * @param other
     *            The ItemStack containing all of the original block's data
     */
    public static void writeToItemStack(ItemStack item, ItemStack other)
    {
        CompressedData tmp = new CompressedData();
        tmp.setBlockID(other.itemID);
        tmp.setBlockMeta((byte) other.getItemDamage());
        tmp.setData(other.getTagCompound());
        tmp.writeToItemStack(item);
    }

    private transient final TileEntity parent;

    public CompressedData()
    {
        parent = null;
        id = 0;
        meta = 0;
        data = null;
        hasTick = false;
        tickTile = null;
    }

    public CompressedData(TileEntity tile)
    {
        parent = tile;
        id = 0;
        meta = 0;
        data = null;
        hasTick = false;
        tickTile = null;
    }

    /**
     * @return The Parent TileEntity if it was created by it, otherwise NULL
     */
    public TileEntity getParent()
    {
        return parent;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((data == null) ? 0 : data.hashCode());
        result = (prime * result) + (hasTick ? 1231 : 1237);
        result = (prime * result) + id;
        result = (prime * result) + meta;
        result = (prime * result) + ((tickTile == null) ? 0 : tickTile.hashCode());
        return result;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CompressedData [id=");
        builder.append(id);
        builder.append(", meta=");
        builder.append(meta);
        builder.append(", data=");
        builder.append(data);
        builder.append(", hasTick=");
        builder.append(hasTick);
        builder.append(", tickTile=");
        builder.append(tickTile);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof CompressedData))
        {
            return false;
        }
        CompressedData other = (CompressedData) obj;
        if (data == null)
        {
            if (other.data != null)
            {
                return false;
            }
        } else if (!data.equals(other.data))
        {
            return false;
        }
        if (hasTick != other.hasTick)
        {
            return false;
        }
        if (id != other.id)
        {
            return false;
        }
        if (meta != other.meta)
        {
            return false;
        }
        if (tickTile == null)
        {
            if (other.tickTile != null)
            {
                return false;
            }
        } else if (!tickTile.equals(other.tickTile))
        {
            return false;
        }
        return true;
    }
}