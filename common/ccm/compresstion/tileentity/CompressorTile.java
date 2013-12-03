package ccm.compresstion.tileentity;

import net.minecraft.block.Block;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;
import ccm.compresstion.block.ModBlocks;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.tileentity.ActiveTE;
import ccm.nucleum.omnium.utils.helper.CCMLogger;
import ccm.nucleum.omnium.utils.helper.FunctionHelper;
import ccm.nucleum.omnium.utils.helper.NBTHelper;
import ccm.nucleum.omnium.utils.helper.item.ItemHelper;
import ccm.nucleum.omnium.utils.lib.Properties;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CompressorTile extends ActiveTE implements ISidedInventory
{
    public static final int IN = 0;
    public static final int FUEL = 1;
    public static final int OUT = 2;

    /** The number of ticks that the compressor will keep compressing */
    public int compressTime;

    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the compressor running for
     */
    public int currentCompressTime;

    /** The number of ticks that the current item has been compressing for */
    public int compressionTime;

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        boolean burning = compressTime > 0;
        boolean done = false;

        if (compressTime > 0)
        {
            --compressTime;
        }

        if (!worldObj.isRemote)
        {
            if ((compressTime == 0) && canRun())
            {
                currentCompressTime = compressTime = TileEntityFurnace.getItemBurnTime(getStackInSlot(FUEL));

                if (compressTime > 0)
                {
                    done = true;

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

            if ((compressTime > 0) && canRun())
            {
                ++compressionTime;

                if (compressionTime == 50)
                {
                    compressionTime = 0;
                    compressItem();
                    done = true;
                }
            } else
            {
                compressionTime = 0;
            }
            if (burning != (compressTime > 0))
            {
                done = true;
            }
        }
        if (done)
        {
            onInventoryChanged();
        }
    }

    @Override
    public boolean canRun()
    {
        if (getStackInSlot(IN) != null)
        {
            if (getStackInSlot(IN).stackSize >= 9)
            {
                // get the current Item
                ItemStack stack = getStackInSlot(IN);
                Block block = getBlock(stack);
                if (block != null)
                {
                    int meta = stack.getItemDamage();
                    // Reset for the actual output
                    stack = getCompressedItem();
                    if (stack != null)
                    {
                        if (FunctionHelper.isNormalBlock(block, meta) || (block.blockID == ModBlocks.compressedBlock.blockID))
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
        currentCompressTime = TileEntityFurnace.getItemBurnTime(getStackInSlot(FUEL));
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
        Block block = getBlock(stack);
        if (block != null)
        {
            byte meta = (byte) stack.getItemDamage();
            // if the stack is Ours
            if (block.blockID == ModBlocks.compressedBlock.blockID)
            {
                int tmp = meta + 1;
                if (tmp < 16)
                {
                    stack.setItemDamage(tmp);
                    if (!Properties.DEBUG)
                    {
                        stack.stackSize = 1;
                    }
                } else
                {
                    CCMLogger.DEFAULT_LOGGER.severe("WTF!!!!!!");
                    return null;
                }
            } else
            { // if it is not
                ItemStack tmp = new ItemStack(ModBlocks.compressedBlock);
                NBTHelper.setInteger(tmp, Archive.NBT_COMPRESSED_BLOCK_ID, block.blockID);
                NBTHelper.setByte(tmp, Archive.NBT_COMPRESSED_BLOCK_META, meta);
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
        return (compressionTime * scale) / 200;
    }

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel item, where 0 means that the item is exhausted and the passed
     * value means that the item is fresh
     */
    @SideOnly(Side.CLIENT)
    public int getCompressTimeScaled(int scale)
    {
        if (currentCompressTime == 0)
        {
            currentCompressTime = 200;
        }

        return (compressTime * scale) / currentCompressTime;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if (side == ForgeDirection.UP.ordinal())
        {
            return new int[]
            { IN };
        } else if (side == ForgeDirection.DOWN.ordinal())
        {
            return new int[]
            { OUT };
        } else
        {
            return new int[]
            { FUEL };
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side)
    {
        if (side != ForgeDirection.DOWN.ordinal())
        {
            if (side != ForgeDirection.UP.ordinal())
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
        return ((side == ForgeDirection.DOWN.ordinal()) && (slot == OUT)) ? true : false;
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