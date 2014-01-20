package ccm.compression.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * ITileTick
 * <p>
 * ANY class that implements this interface MUST save all relevant data to NBT and load it from NBT. It must also be able to be created
 * using reflection, AKA a No argument constructor
 * 
 * @author Captain_Shadows
 */
public interface ITileTick
{
    /**
     * @param The
     *            owner {@link TileEntity}. THIS MIGHT BE NULL
     */
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