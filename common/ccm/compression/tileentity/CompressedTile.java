package ccm.compression.tileentity;

import net.minecraft.nbt.NBTTagCompound;

import ccm.compression.Compression;
import ccm.compression.api.CompressedData;
import ccm.nucleum.omnium.tileentity.BaseTE;

public class CompressedTile extends BaseTE
{
    /*
     * Data
     */
    /** All of the data for this TE */
    private CompressedData data;

    public CompressedData data()
    {
        if (data == null)
        {
            data = new CompressedData(this);
        }
        return data;
    }

    /*
     * Tile Overrides
     */
    @Override
    public void validate()
    {
        super.validate();
        if (data().tick())
        {
            data().getTick().validate();
        }
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (data().tick())
        {
            data().getTick().invalidate();
        }
    }

    @Override
    public boolean canUpdate()
    {
        return data().tick();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (data().tick())
        {
            data().getTick().tick();
        } else
        {
            Compression.instance.logger().bug("Tile: %s\nRAN updateEntity() WITHOUT HAVING A tick()", this);
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        data().writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        data().readFromNBT(nbt);
    }
}