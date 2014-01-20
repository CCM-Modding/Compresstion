package ccm.compression.tileentity;

import net.minecraft.block.Block;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ccm.compression.Compression;
import ccm.compression.block.ModBlocks;
import ccm.compression.utils.helper.CompressedData;
import ccm.compression.utils.lib.Properties;
import ccm.nucleum.omnium.tileentity.ActiveTE;
import ccm.nucleum.omnium.utils.helper.FunctionHelper;
import ccm.nucleum.omnium.utils.helper.item.ItemHelper;

public class CompressorTile extends ActiveTE implements ISidedInventory
{
    public static final int IN = 0;
    public static final int FUEL = 1;
    public static final int OUT = 2;
    /** The number of ticks that the compressor will keep compressing */
    private int compressTime;
    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the compressor running for
     */
    private int totalCompressTime;
    /** The number of ticks that the current item has been compressing for */
    private int compressionTime;
    /** The maximum amount of time that you have to wait for the operation to be done */
    private final int maxTime = 400;

    /**
     * Returns true if the furnace is currently burning
     */
    public boolean isCompressing()
    {
        return compressTime > 0;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        boolean burning = isCompressing();
        boolean invChanged = false;
        if (isCompressing())
        {
            --compressTime;
        }
        if (!worldObj.isRemote)
        {
            if ((compressTime == 0) && canRun())
            {
                totalCompressTime = compressTime = TileEntityFurnace.getItemBurnTime(getStackInSlot(FUEL));
                // SEND PACKET to make sure tile is up to date for the client
                PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 10, worldObj.provider.dimensionId, getDescriptionPacket());
                if (isCompressing())
                {
                    invChanged = true;
                    if (getStackInSlot(FUEL) != null)
                    {
                        changeSize(FUEL, -1);
                        if (getStackInSlot(FUEL).stackSize == 0)
                        {
                            setInventorySlotContents(FUEL, getStackInSlot(FUEL).getItem().getContainerItemStack(getStackInSlot(FUEL)));
                        }
                    }
                }
            }
            if (isCompressing() && canRun())
            {
                ++compressionTime;
                if (compressionTime == maxTime)
                {
                    compressionTime = 0;
                    compressItem();
                    invChanged = true;
                }
            } else
            {
                compressionTime = 0;
            }
            if (burning != isCompressing())
            {
                invChanged = true;
            }
        }
        if (invChanged)
        {
            onInventoryChanged();
        }
    }

    @Override
    public boolean canRun()
    {
        if (getStackInSlot(IN) != null)
        {
            if ((getStackInSlot(IN).stackSize >= 9) && !Properties.blackList.contains(getStackInSlot(IN)))
            {// get the current Item
                ItemStack stack = getStackInSlot(IN);
                Block block = getBlock(stack);
                if (block != null)
                {
                    int meta = stack.getItemDamage();
                    // Reset for the actual output
                    stack = getCompressedItem();
                    if (stack != null)
                    {
                        if (FunctionHelper.isNormalBlock(block, meta)
                            || (block.blockID == ModBlocks.compressedBlock.blockID)
                            || Properties.whiteList.contains(getStackInSlot(IN)))
                        {
                            if (getStackInSlot(OUT) == null)
                            {
                                return true;
                            }
                            if (!ItemHelper.compareNoSize(getStackInSlot(OUT), stack))
                            {
                                return false;
                            }
                            int result = getStackInSlot(OUT).stackSize + stack.stackSize;
                            return ((result <= getInventoryStackLimit()) && (result <= stack.getMaxStackSize()));
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        compressTime = nbt.getShort("CompressTime");
        compressionTime = nbt.getShort("CompressionTime");
        totalCompressTime = TileEntityFurnace.getItemBurnTime(getStackInSlot(FUEL));
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setShort("CompressTime", (short) compressTime);
        nbt.setShort("CompressionTime", (short) compressionTime);
    }

    private Block getBlock(ItemStack stack)
    {
        Block block = null;
        if (stack.getItem() instanceof ItemBlock)
        {
            ItemBlock item = (ItemBlock) stack.getItem();
            block = Block.blocksList[item.getBlockID()];
        }
        return block;
    }

    private ItemStack getCompressedItem()
    {
        ItemStack stack = getStackInSlot(IN).copy();
        stack.stackSize = 1;
        Block block = getBlock(stack);
        if (block != null)
        {// if the stack is Ours
            if (block.blockID == ModBlocks.compressedBlock.blockID)
            {
                int meta = stack.getItemDamage() + 1;
                if (meta < 16)
                {
                    stack.setItemDamage(meta);
                } else
                {
                    Compression.instance.logger().debug("Compressing last compression...");
                    return null;
                }
            } else
            { // if it is not
                ItemStack tmp = new ItemStack(ModBlocks.compressedBlock);
                CompressedData.writeToItemStack(tmp, stack);
                stack = tmp;
            }
        }
        return stack;
    }

    /**
     * Turn one item from the compressor source stack into the appropriate compressed item in the compressor result stack
     */
    public void compressItem()
    {
        if (canRun())
        {
            ItemStack stack = getCompressedItem();
            // Below it does all the item checking
            if (getStackInSlot(OUT) == null)
            {
                setInventorySlotContents(OUT, stack.copy());
            } else if (ItemHelper.compareNoSize(getStackInSlot(OUT), stack))
            {
                changeSize(OUT, stack.stackSize);
            }
            changeSize(IN, -9);
            if (getStackInSlot(IN).stackSize <= 0)
            {
                setInventorySlotContents(IN, null);
            }
        }
    }

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely compressed
     */
    @SideOnly(Side.CLIENT)
    public int getCompressProgressScaled(int scale)
    {
        return (compressionTime * scale) / maxTime;
    }

    public int getCompressProgress()
    {
        return compressionTime;
    }

    public void updateCompressProgress(int progress)
    {
        compressionTime = progress;
    }

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel item, where 0 means
     * that the item is exhausted and the passed
     * value means that the item is fresh
     */
    @SideOnly(Side.CLIENT)
    public int getCompressTimeScaled(int scale)
    {
        if (totalCompressTime == 0)
        {
            totalCompressTime = maxTime;
        }
        return (compressTime * scale) / totalCompressTime;
    }

    public int getCompressTime()
    {
        return compressTime;
    }

    public void updateCompressTime(int time)
    {
        compressTime = time;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if (side == ForgeDirection.UP.ordinal())
        {
            return new int[] { IN };
        } else if (side == ForgeDirection.DOWN.ordinal())
        {
            return new int[] { OUT };
        } else
        {
            return new int[] { FUEL };
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side)
    {
        if (slot != OUT)
        {
            if (slot != IN)
            {
                if (TileEntityFurnace.isItemFuel(item))
                {
                    return true;
                }
            } else
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return (slot != OUT) ? false : true;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {
        if (slot != OUT)
        {
            if (slot != IN)
            {
                if (TileEntityFurnace.isItemFuel(item))
                {
                    return true;
                }
            } else
            {
                return true;
            }
        }
        return false;
    }
}