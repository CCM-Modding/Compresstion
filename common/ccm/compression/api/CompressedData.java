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
    /*
     * NBT NAMES
     */
    public static final String NBT_PATH = "classpath";
    public static final String NBT_ORIGEN = "original";
    public static final String NBT_BLOCK_ID = "id";
    public static final String NBT_BLOCK_META = "meta";
    public static final String NBT_BLOCK_DATA = "data";
    public static final String NBT_TICK = "tick";
    public static final String NBT_SPECIAL = "special";
    public static final String NBT_RENDER = "render";
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
     * @return true if the tile is has a {@link ITileTick}
     */
    public boolean hasTick()
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
            if (!hasTick())
            {
                Compression.instance.logger().bug("getTick HAS BEEN CALLED ON A COMPRESSED TILE THAT DOESN'T TICK");
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
     * ISpecialCompressed
     */
    /** Weather it has an {@link ISpecialCompressed} */
    private boolean isSpecial = false;
    /** The path to it's {@link ISpecialCompressed} */
    private String special;
    /** A temporally stored version so that it dosen't have to be created every time */
    private transient ISpecialCompressed iSpecial = null;

    /**
     * @return true if the tile is has a {@link ISpecialCompressed}
     */
    public boolean isSpecial()
    {
        return isSpecial;
    }

    /**
     * @param special
     *            The Canonical Name of a class that implements {@link ISpecialCompressed}
     */
    public void setSpecial(final String special)
    {
        if ((special != null) && !special.equalsIgnoreCase(""))
        {
            isSpecial = true;
            this.special = special;
        }
    }

    /**
     * @param special
     *            An instance of {@link ISpecialCompressed}. It <b>MUST</b> be able to be reconstructed
     */
    public void setSpecial(final ISpecialCompressed special)
    {
        if (special != null)
        {
            iSpecial = special;
            setSpecial(special.getClass().getCanonicalName());
        }
    }

    /**
     * @return An instance of {@link ISpecialCompressed} or null if the tile doesn't have one
     */
    public ISpecialCompressed getSpecial()
    {
        if (iSpecial == null)
        {
            if (!isSpecial())
            {
                Compression.instance.logger().bug("getSpecial HAS BEEN CALLED ON A COMPRESSED TILE THAT ISN'T SPECIAL");
            } else
            {
                // Create the temporal variables for internal usage
                Class<? extends ISpecialCompressed> tmpHandler = null;
                try
                {// Try to find the ITileTick Class
                    tmpHandler = (Class<? extends ISpecialCompressed>) Class.forName(special, false, Loader.instance().getModClassLoader());
                } catch (ClassNotFoundException e)
                {
                    Compression.instance.logger().printCatch(e, "A compressed Tile has failed to find it's ISpecialCompressed @ %s", special);
                    return null;
                }
                try
                {// Try to crate a new instance of said Class
                    iSpecial = tmpHandler.newInstance();
                } catch (Exception e)
                {
                    Compression.instance.logger().printCatch(e,
                                                             "A compressed Tile has failed to create an instance of it's ISpecialCompressed @ %s",
                                                             special);
                    return null;
                }
            }
        }// Done
        return iSpecial;
    }

    /*
     * ISpecialRenderer
     */
    /** Weather it has an {@link ITileTick} */
    private boolean hasRender = false;
    /** The path to it's {@link ITileTick} */
    private String renderTile;
    /** A temporally stored version so that it dosen't have to be created every time */
    private transient ISpecialRenderer render = null;

    /**
     * @return true if the tile is has an {@link ISpecialRenderer}
     */
    public boolean hasRender()
    {
        return hasRender;
    }

    /**
     * @param renderTile
     *            The Canonical Name of a class that implements {@link ISpecialRenderer}
     */
    public void setRender(final String renderTile)
    {
        if ((renderTile != null) && !renderTile.equalsIgnoreCase(""))
        {
            hasRender = true;
            this.renderTile = renderTile;
        }
    }

    /**
     * @param render
     *            An instance of {@link ISpecialRenderer}. It <b>MUST</b> be able to be reconstructed
     */
    public void setRender(final ISpecialRenderer render)
    {
        if (render != null)
        {
            this.render = render;
            setRender(render.getClass().getCanonicalName());
        }
    }

    /**
     * @return An instance of {@link ISpecialRenderer} or null if the tile doesn't have one
     */
    public ISpecialRenderer getRender()
    {
        if (render == null)
        {
            if (!hasRender())
            {
                Compression.instance.logger().bug("getRender HAS BEEN CALLED ON A COMPRESSED TILE THAT DOESN'T HAVE A SPECIAL RENDER");
            } else
            {
                // Create the temporal variables for internal usage
                Class<? extends ISpecialRenderer> tmpHandler = null;
                try
                {// Try to find the ITileTick Class
                    tmpHandler = (Class<? extends ISpecialRenderer>) Class.forName(renderTile, false, Loader.instance().getModClassLoader());
                } catch (ClassNotFoundException e)
                {
                    Compression.instance.logger().printCatch(e, "A compressed Tile has failed to find it's ISpecialRenderer @ %s", renderTile);
                    return null;
                }
                try
                {// Try to crate a new instance of said Class
                    render = tmpHandler.newInstance();
                } catch (Exception e)
                {
                    Compression.instance.logger().printCatch(e,
                                                             "A compressed Tile has failed to create an ISpecialRenderer of it's ITileTick @ %s",
                                                             renderTile);
                    return null;
                }
            }
        }// Done
        return render;
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
        if (hasTick())
        {
            tick.setString(NBT_PATH, tickTile);
            getTick().writeToNBT(tick);
        }
        origin.setCompoundTag(NBT_TICK, tick);
        // Done adding that. Adding Special stuff
        NBTTagCompound special = new NBTTagCompound();
        if (hasTick())
        {
            special.setString(NBT_PATH, this.special);
        }
        origin.setCompoundTag(NBT_SPECIAL, special);
        // Done adding that. Adding Render stuff
        NBTTagCompound render = new NBTTagCompound();
        if (hasRender())
        {
            render.setString(NBT_PATH, renderTile);
        }
        origin.setCompoundTag(NBT_RENDER, render);
        // Done adding everything, now storing it
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
        setTick(NBTHelper.getString(tick, NBT_PATH));
        if (hasTick())
        {
            getTick().readFromNBT(tick);
        }
        // Getting Special Stuff
        NBTTagCompound special = NBTHelper.getTag(origin, NBT_SPECIAL);
        setSpecial(NBTHelper.getString(special, NBT_PATH));
        // Getting Render Stuff
        NBTTagCompound render = NBTHelper.getTag(origin, NBT_RENDER);
        setRender(NBTHelper.getString(render, NBT_PATH));
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

    /*
     * Object Stuff
     */
    private transient final TileEntity parent;

    public CompressedData()
    {
        parent = null;
        id = 0;
        meta = 0;
        data = null;
        hasTick = false;
        tickTile = null;
        isSpecial = false;
        special = null;
        hasRender = false;
        renderTile = null;
    }

    public CompressedData(TileEntity tile)
    {
        parent = tile;
        id = 0;
        meta = 0;
        data = null;
        hasTick = false;
        tickTile = null;
        isSpecial = false;
        special = null;
        hasRender = false;
        renderTile = null;
    }

    /**
     * @return The Parent TileEntity if it was created by it, otherwise NULL
     */
    public TileEntity getParent()
    {
        return parent;
    }
}