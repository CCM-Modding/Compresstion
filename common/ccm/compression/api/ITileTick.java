package ccm.compression.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public interface ITileTick
{
    public void setOwner(TileEntity tile);

    /**
     * runs when updateEntity is called on the owner tile
     */
    public void tick();

    public void writeToNBT(final NBTTagCompound nbt);

    public void readFromNBT(final NBTTagCompound nbt);

    /**
     * runs when invalidate is called on the owner tile
     */
    public void invalidate();

    /**
     * runs when validate is called on the owner tile
     */
    public void validate();
}