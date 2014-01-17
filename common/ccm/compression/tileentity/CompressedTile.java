package ccm.compression.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ccm.compression.Compression;
import ccm.compression.api.ITileTick;
import ccm.compression.utils.lib.Archive;
import ccm.nucleum.omnium.tileentity.BaseTE;
import ccm.nucleum.omnium.utils.helper.NBTHelper;

public class CompressedTile extends BaseTE
{
    /*
     * Block data
     */
    /** original block id */
    private int id;
    /** original block meta data */
    private byte meta;
    /** Any meta data that the Item had */
    private NBTTagCompound data;

    public Block getBlock()
    {
        return Block.blocksList[id];
    }

    public void setBlockID(final int id)
    {
        this.id = id;
    }

    public void setBlockMeta(final byte meta)
    {
        this.meta = meta;
    }

    public void setData(NBTTagCompound nbt)
    {
        data = nbt;
    }

    public int getID()
    {
        return id;
    }

    public byte getMeta()
    {
        return meta;
    }

    public NBTTagCompound getData()
    {
        return data;
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

    public boolean tick()
    {
        return hasTick;
    }

    public void setTick(final String tickTile)
    {
        if (tickTile != null && !tickTile.equalsIgnoreCase(""))
        {
            hasTick = true;
            this.tickTile = tickTile;
        }
    }

    public void setTick(final ITileTick tick)
    {
        if (tick != null)
        {
            hasTick = true;
            this.tick = tick;
            tickTile = tick.getClass().getCanonicalName();
        }
    }

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
     * Conversion Stuff
     */
    public void loadFromItemStack(ItemStack item)
    {
        // Getting Origin
        NBTTagCompound origin = NBTHelper.getTag(item, Archive.NBT_ORIGEN);
        // Getting Block stuff
        setBlockID(NBTHelper.getInteger(origin, Archive.NBT_BLOCK_ID));
        setBlockMeta(NBTHelper.getByte(origin, Archive.NBT_BLOCK_META));
        setData(NBTHelper.getTag(origin, Archive.NBT_BLOCK_DATA));
        // Getting Tick Stuff
        setTick(NBTHelper.getString(origin, Archive.NBT_TICK));
        // Getting icons
        setIcons(NBTHelper.getTag(origin, Archive.NBT_ICONS));
    }

    public void saveToItemStack(ItemStack item)
    {
        NBTTagCompound origin = new NBTTagCompound();
        // Adding Block stuff
        origin.setInteger(Archive.NBT_BLOCK_ID, id);
        origin.setByte(Archive.NBT_BLOCK_META, meta);
        origin.setTag(Archive.NBT_BLOCK_DATA, data);
        // Done adding that. Adding Tick stuff
        origin.setBoolean(Archive.NBT_HAS_TICK, hasTick);
        origin.setString(Archive.NBT_TICK, tickTile);
        // Done adding that. Add Icons to nbt
        NBTTagCompound icons = new NBTTagCompound();
        icons.setTag(Archive.NBT_ICONS_DOWN, new NBTTagString(down));
        icons.setTag(Archive.NBT_ICONS_UP, new NBTTagString(up));
        icons.setTag(Archive.NBT_ICONS_NORTH, new NBTTagString(north));
        icons.setTag(Archive.NBT_ICONS_SOUTH, new NBTTagString(south));
        icons.setTag(Archive.NBT_ICONS_WEST, new NBTTagString(west));
        icons.setTag(Archive.NBT_ICONS_EAST, new NBTTagString(east));
        origin.setTag(Archive.NBT_ICONS, icons);
        // Done adding them
        NBTHelper.setTag(item, Archive.NBT_ORIGEN, origin);
    }

    public static void fakeSave(ItemStack item, int id, int meta)
    {
        Block block = Block.blocksList[id];
        CompressedTile tmp = new CompressedTile();
        tmp.setBlockID(id);
        tmp.setBlockMeta((byte) meta);
        tmp.setIcons(block, meta);
        tmp.saveToItemStack(item);
    }

    public static void save(ItemStack item, ItemStack other)
    {
        Block block = Block.blocksList[other.itemID];
        CompressedTile tmp = new CompressedTile();
        tmp.setBlockID(other.itemID);
        tmp.setBlockMeta((byte) other.getItemDamage());
        tmp.setData(other.getTagCompound());
        tmp.setIcons(block, other.getItemDamage());
        tmp.saveToItemStack(item);
    }

    public void setIcons(NBTTagCompound nbt)
    {
        down = NBTHelper.getString(nbt, Archive.NBT_ICONS_DOWN);
        up = NBTHelper.getString(nbt, Archive.NBT_ICONS_UP);
        north = NBTHelper.getString(nbt, Archive.NBT_ICONS_NORTH);
        south = NBTHelper.getString(nbt, Archive.NBT_ICONS_SOUTH);
        west = NBTHelper.getString(nbt, Archive.NBT_ICONS_WEST);
        east = NBTHelper.getString(nbt, Archive.NBT_ICONS_EAST);
    }

    /*
     * NBT STUFF
     */
    @Override
    public void writeToNBT(final NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        NBTTagCompound origin = new NBTTagCompound();
        // Adding Block stuff
        origin.setInteger(Archive.NBT_BLOCK_ID, id);
        origin.setByte(Archive.NBT_BLOCK_META, meta);
        origin.setTag(Archive.NBT_BLOCK_DATA, data);
        // Done adding that. Adding Tick stuff
        origin.setBoolean(Archive.NBT_HAS_TICK, hasTick);
        origin.setString(Archive.NBT_TICK, tickTile);
        // Done adding that. Add Icons to nbt
        NBTTagCompound icons = new NBTTagCompound();
        icons.setTag(Archive.NBT_ICONS_DOWN, new NBTTagString(down));
        icons.setTag(Archive.NBT_ICONS_UP, new NBTTagString(up));
        icons.setTag(Archive.NBT_ICONS_NORTH, new NBTTagString(north));
        icons.setTag(Archive.NBT_ICONS_SOUTH, new NBTTagString(south));
        icons.setTag(Archive.NBT_ICONS_WEST, new NBTTagString(west));
        icons.setTag(Archive.NBT_ICONS_EAST, new NBTTagString(east));
        origin.setTag(Archive.NBT_ICONS, icons);
        // Done adding them
        nbt.setTag(Archive.NBT_ORIGEN, origin);
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        // Getting Origin
        NBTTagCompound origin = NBTHelper.getTag(nbt, Archive.NBT_ORIGEN);
        // Getting Block stuff
        setBlockID(NBTHelper.getInteger(origin, Archive.NBT_BLOCK_ID));
        setBlockMeta(NBTHelper.getByte(origin, Archive.NBT_BLOCK_META));
        setData(NBTHelper.getTag(origin, Archive.NBT_BLOCK_DATA));
        // Getting Tick Stuff
        setTick(NBTHelper.getString(origin, Archive.NBT_TICK));
        // Getting icons
        setIcons(NBTHelper.getTag(origin, Archive.NBT_ICONS));
    }

    /*
     * Tile Overrides
     */
    @Override
    public void validate()
    {
        super.validate();
        if (tick())
        {
            tick.validate();
        }
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (tick())
        {
            tick.invalidate();
        }
    }

    @Override
    public boolean canUpdate()
    {
        return tick();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (tick())
        {
            tick.tick();
        } else
        {
            Compression.instance.logger().bug("Tile: %s\nRAN updateEntity() WITHOUT HAVING A tick()", this);
        }
    }
}