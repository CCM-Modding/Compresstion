package ccm.compression.utils.helper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ccm.compression.Compression;
import ccm.compression.api.ITileTick;
import ccm.nucleum.omnium.utils.helper.NBTHelper;

/**
 * CompressedData
 * <p>
 * TODO RENAME
 * 
 * @author Captain_Shadows
 */
public final class CompressedData
{
    /** Static Data */
    public static final String NBT_ORIGEN = "original";
    public static final String NBT_BLOCK_ID = "id";
    public static final String NBT_BLOCK_META = "meta";
    public static final String NBT_BLOCK_DATA = "data";
    public static final String NBT_TICK = "tick";
    public static final String NBT_TICK_NAME = "name";
    public static final String NBT_TICK_HAS = "has";
    public static final String NBT_ICONS = "icons";
    public static final String NBT_ICONS_DOWN = "DOWN";
    public static final String NBT_ICONS_UP = "UP";
    public static final String NBT_ICONS_NORTH = "NORTH";
    public static final String NBT_ICONS_SOUTH = "SOUTH";
    public static final String NBT_ICONS_WEST = "WEST";
    public static final String NBT_ICONS_EAST = "EAST";
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
    private ITileTick tick = null;

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
     * ICONS
     */
    /** -Y */
    private String down;
    /** +Y */
    private String up;
    /** -Z */
    private String north;
    /** +Z */
    private String south;
    /** -X */
    private String west;
    /** +X */
    private String east;

    public void setIcons(final Block block)
    {
        setIcons(block, 0);
    }

    public void setIcons(final Block block, final int meta)
    {
        setDown(block.getIcon(ForgeDirection.DOWN.ordinal(), meta).getIconName());
        setUp(block.getIcon(ForgeDirection.UP.ordinal(), meta).getIconName());
        setNorth(block.getIcon(ForgeDirection.NORTH.ordinal(), meta).getIconName());
        setSouth(block.getIcon(ForgeDirection.SOUTH.ordinal(), meta).getIconName());
        setWest(block.getIcon(ForgeDirection.WEST.ordinal(), meta).getIconName());
        setEast(block.getIcon(ForgeDirection.EAST.ordinal(), meta).getIconName());
    }

    public void setDown(final ResourceLocation icon)
    {
        setDown(icon.toString());
    }

    public void setUp(final ResourceLocation icon)
    {
        setUp(icon.toString());
    }

    public void setNorth(final ResourceLocation icon)
    {
        setNorth(icon.toString());
    }

    public void setSouth(final ResourceLocation icon)
    {
        setSouth(icon.toString());
    }

    public void setWest(final ResourceLocation icon)
    {
        setWest(icon.toString());
    }

    public void setEast(final ResourceLocation icon)
    {
        setEast(icon.toString());
    }

    public void setIcons(final ResourceLocation all)
    {
        setDown(all.toString());
        setUp(all.toString());
        setNorth(all.toString());
        setSouth(all.toString());
        setWest(all.toString());
        setEast(all.toString());
    }

    public void setDown(final String icon)
    {
        down = icon;
    }

    public void setUp(final String icon)
    {
        up = icon;
    }

    public void setNorth(final String icon)
    {
        north = icon;
    }

    public void setSouth(final String icon)
    {
        south = icon;
    }

    public void setWest(final String icon)
    {
        west = icon;
    }

    public void setEast(final String icon)
    {
        east = icon;
    }

    public void setIcons(final String all)
    {
        setDown(all);
        setUp(all);
        setNorth(all);
        setSouth(all);
        setWest(all);
        setEast(all);
    }

    @SideOnly(Side.CLIENT)
    public Icon getDown()
    {
        return ((TextureMap) Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.locationBlocksTexture)).getTextureExtry(down);
    }

    @SideOnly(Side.CLIENT)
    public Icon getUp()
    {
        return ((TextureMap) Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.locationBlocksTexture)).getTextureExtry(up);
    }

    @SideOnly(Side.CLIENT)
    public Icon getNorth()
    {
        return ((TextureMap) Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.locationBlocksTexture)).getTextureExtry(north);
    }

    @SideOnly(Side.CLIENT)
    public Icon getSouth()
    {
        return ((TextureMap) Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.locationBlocksTexture)).getTextureExtry(south);
    }

    @SideOnly(Side.CLIENT)
    public Icon getWest()
    {
        return ((TextureMap) Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.locationBlocksTexture)).getTextureExtry(west);
    }

    @SideOnly(Side.CLIENT)
    public Icon getEast()
    {
        return ((TextureMap) Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.locationBlocksTexture)).getTextureExtry(east);
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(final ForgeDirection dir)
    {
        switch (dir)
        {
            case DOWN:
                return getDown();
            case UP:
                return getUp();
            case NORTH:
                return getNorth();
            case SOUTH:
                return getSouth();
            case WEST:
                return getWest();
            case EAST:
                return getEast();
            default:
                return null;
        }
    }

    /*
     * NBT STUFF
     */
    public void setIcons(NBTTagCompound nbt)
    {
        down = NBTHelper.getString(nbt, NBT_ICONS_DOWN);
        up = NBTHelper.getString(nbt, NBT_ICONS_UP);
        north = NBTHelper.getString(nbt, NBT_ICONS_NORTH);
        south = NBTHelper.getString(nbt, NBT_ICONS_SOUTH);
        west = NBTHelper.getString(nbt, NBT_ICONS_WEST);
        east = NBTHelper.getString(nbt, NBT_ICONS_EAST);
    }

    public void writeToNBT(final NBTTagCompound nbt)
    {
        NBTTagCompound origin = new NBTTagCompound();
        // Adding Block stuff
        origin.setInteger(NBT_BLOCK_ID, getID());
        origin.setByte(NBT_BLOCK_META, getMeta());
        origin.setTag(NBT_BLOCK_DATA, getData());
        // Done adding that. Adding Tick stuff
        NBTTagCompound tick = new NBTTagCompound();
        tick.setBoolean(NBT_TICK_HAS, tick());
        tick.setString(NBT_TICK_NAME, tickTile);
        if (tick())
        {
            getTick().writeToNBT(tick);
        }
        origin.setCompoundTag(NBT_TICK, tick);
        // Done adding that. Add Icons to nbt
        NBTTagCompound icons = new NBTTagCompound();
        icons.setTag(NBT_ICONS_DOWN, new NBTTagString(down));
        icons.setTag(NBT_ICONS_UP, new NBTTagString(up));
        icons.setTag(NBT_ICONS_NORTH, new NBTTagString(north));
        icons.setTag(NBT_ICONS_SOUTH, new NBTTagString(south));
        icons.setTag(NBT_ICONS_WEST, new NBTTagString(west));
        icons.setTag(NBT_ICONS_EAST, new NBTTagString(east));
        origin.setCompoundTag(NBT_ICONS, icons);
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
        // Getting icons
        setIcons(NBTHelper.getTag(origin, NBT_ICONS));
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
        Block block = Block.blocksList[id];
        CompressedData tmp = new CompressedData();
        tmp.setBlockID(id);
        tmp.setBlockMeta((byte) meta);
        tmp.setIcons(block, meta);
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
        Block block = Block.blocksList[other.itemID];
        CompressedData tmp = new CompressedData();
        tmp.setBlockID(other.itemID);
        tmp.setBlockMeta((byte) other.getItemDamage());
        tmp.setData(other.getTagCompound());
        tmp.setIcons(block, other.getItemDamage());
        tmp.writeToItemStack(item);
    }

    private final TileEntity parent;

    public CompressedData()
    {
        parent = null;
    }

    public CompressedData(TileEntity tile)
    {
        parent = tile;
    }

    /**
     * @return The Parent TileEntity if it was created by it, otherwise NULL
     */
    public TileEntity getParent()
    {
        return parent;
    }
}