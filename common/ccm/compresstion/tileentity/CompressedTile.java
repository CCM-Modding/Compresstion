package ccm.compresstion.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import ccm.compresstion.block.CompressedType;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.tileentity.BaseTE;
import ccm.nucleum.omnium.utils.helper.NBTHelper;

public class CompressedTile extends BaseTE
{
    private int id;
    private byte meta;
    private CompressedType type;

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

    public byte getMeta()
    {
        return meta;
    }
    
    public void setBlockType(final CompressedType type)
    {
        this.type = type;
    }
    
    public void setBlockType(final int type)
    {
        setBlockType(CompressedType.values()[type]);
    }
    
    public CompressedType type(){
        return type;
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger(Archive.NBT_COMPRESSED_BLOCK_ID, id);
        nbt.setByte(Archive.NBT_COMPRESSED_BLOCK_META, meta);
        nbt.setInteger(Archive.NBT_COMPRESSED_BLOCK_TYPE, type.ordinal());
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        id = NBTHelper.getInteger(nbt, Archive.NBT_COMPRESSED_BLOCK_ID);
        meta = NBTHelper.getByte(nbt, Archive.NBT_COMPRESSED_BLOCK_META);
        type = CompressedType.values()[NBTHelper.getInteger(nbt, Archive.NBT_COMPRESSED_BLOCK_TYPE)];
    }
}