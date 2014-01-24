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
    public boolean shouldTick();

    /**
     * runs when updateEntity is called on the owner tile
     */
    public void tick();

    /**
     * runs when validate is called on the owner tile
     */
    public void validate();

    /**
     * runs when invalidate is called on the owner tile
     */
    public void invalidate();

    /**
     * @param owner
     *            The owner {@link TileEntity}. THIS MIGHT BE NULL
     */
    public void setOwner(final TileEntity tile);

    /**
     * @param nbt
     *            Write all of YOUR data to this
     */
    public void writeToNBT(final NBTTagCompound nbt);

    /**
     * @param nbt
     *            Read all of YOUR data from this. Only Your data will appear here seriously don't try to get TE data from it.
     */
    public void readFromNBT(final NBTTagCompound nbt);
}