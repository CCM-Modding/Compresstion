package ccm.compresstion.tileentity;

import net.minecraft.block.Block;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;
import ccm.compresstion.block.ModBlocks;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.inventory.container.element.TimedElement;
import ccm.nucleum.omnium.tileentity.ProgressTE;
import ccm.nucleum.omnium.utils.helper.CCMLogger;
import ccm.nucleum.omnium.utils.helper.FunctionHelper;
import ccm.nucleum.omnium.utils.helper.NBTHelper;

public class CompressorTile extends ProgressTE implements ISidedInventory
{
    public static final int IN = 0;
    public static final int FUEL = 1;
    public static final int OUT = 2;

    public static final int progressBar = 0;
    public static final int burnBar = 1;
    private int currentItemBurnTime;

    public CompressorTile()
    {
        progresses = new TimedElement[2];
        progresses[progressBar] = new TimedElement();
        progresses[burnBar] = new TimedElement();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        boolean burning = getTimeLeft(burnBar) > 0;
        boolean done = false;

        if (getTimeLeft(burnBar) > 0)
        {
            update(burnBar, -1);
        }

        if (!worldObj.isRemote)
        {
            if ((getTimeLeft(burnBar) == 0) && canRun())
            {
                int burnTime = TileEntityFurnace.getItemBurnTime(getInventory()[1]);
                setTimeLeft(burnBar, burnTime);
                currentItemBurnTime = burnTime;

                if (getTimeLeft(burnBar) > 0)
                {
                    done = true;

                    if (getStackInSlot(FUEL) != null)
                    {
                        --getInventory()[FUEL].stackSize;

                        if (getStackInSlot(FUEL).stackSize == 0)
                        {
                            getInventory()[FUEL] = getStackInSlot(FUEL).getItem().getContainerItemStack(getStackInSlot(FUEL));
                        }
                    }
                }
            }

            if ((getTimeLeft(burnBar) > 0) && canRun())
            {
                update(progressBar, 1);

                if (getTimeLeft(progressBar) == 200)
                {
                    setTimeLeft(progressBar, 0);
                    compressItem();
                    done = true;
                }
            } else
            {
                setTimeLeft(burnBar, 0);
            }

            if (burning != (getTimeLeft(burnBar) > 0))
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
                        if (!ItemStack.areItemStacksEqual(getStackInSlot(OUT), stack))
                        {
                            return false;
                        }
                        int result = getStackInSlot(OUT).stackSize + stack.stackSize;
                        return ((result <= getInventoryStackLimit()) && (result <= stack.getMaxStackSize()));
                    }
                }
            }
        }
        return false;
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
            } else if (ItemStack.areItemStacksEqual(getStackInSlot(OUT), stack))
            {
                getInventory()[OUT].stackSize += stack.stackSize;
            }

            getInventory()[IN].stackSize -= 9;

            if (getStackInSlot(IN).stackSize <= 0)
            {
                setInventorySlotContents(IN, null);
            }
        }
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