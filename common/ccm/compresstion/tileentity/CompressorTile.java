package ccm.compresstion.tileentity;

import net.minecraft.block.Block;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;

import ccm.compresstion.Compresstion;
import ccm.compresstion.block.ModBlocks;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.inventory.container.element.TimedElement;
import ccm.nucleum.omnium.tileentity.ProgressTE;
import ccm.nucleum.omnium.utils.helper.CCMLogger;
import ccm.nucleum.omnium.utils.helper.NBTHelper;
import ccm.nucleum.omnium.utils.helper.item.ItemHelper;

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
            if (getTimeLeft(burnBar) == 0 && canRun())
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

            if (getTimeLeft(burnBar) > 0 && canRun())
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

            if (burning != getTimeLeft(burnBar) > 0)
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
                if (isNormalBlock(block, meta))
                {
                    if (getStackInSlot(OUT) == null)
                        return true;
                    if (!getStackInSlot(OUT).isItemEqual(stack))
                        return false;
                    int result = getStackInSlot(OUT).stackSize + stack.stackSize;
                    return (result <= getInventoryStackLimit() && result <= stack.getMaxStackSize());
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
                if (meta + 1 <= 15)
                {
                    stack.setItemDamage(meta + 1);
                } else
                {
                    CCMLogger.DEFAULT_LOGGER.severe("WTF!!!!!!");
                    return null;
                }
            } else
            // if it is not
            {
                ItemStack tmp = new ItemStack(ModBlocks.compressedBlock, 1);
                NBTHelper.setInteger(tmp, Archive.NBT_COMPRESSED_BLOCK_ID, block.blockID);
                NBTHelper.setByte(stack, Archive.NBT_COMPRESSED_BLOCK_META, meta);
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
                getInventory()[OUT] = stack.copy();
            } else if (getStackInSlot(OUT).isItemEqual(stack))
            {
                getInventory()[OUT].stackSize += stack.stackSize;
            }

            --getInventory()[IN].stackSize;

            if (getStackInSlot(IN).stackSize <= 0)
            {
                getInventory()[IN] = null;
            }
        }
    }

    // TODO Move to Nucleum
    public static boolean isNormalBlock(Block block, int meta)
    {
        return (block.isOpaqueCube() && (block.getRenderType() == 0) && (!block.hasTileEntity(meta)) && block.renderAsNormalBlock() && hasNormalBounds(block));
    }

    // TODO Move to Nucleum
    public static boolean hasNormalBounds(Block block)
    {
        return ((block.getBlockBoundsMaxX() == 1) && (block.getBlockBoundsMaxY() == 1) && (block.getBlockBoundsMaxZ() == 1) && (block.getBlockBoundsMinX() == 0)
                && (block.getBlockBoundsMinY() == 0) && (block.getBlockBoundsMinZ() == 0));
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if (side == ForgeDirection.UP.ordinal())
        {
            return new int[]
            { 0 };
        } else if (side == ForgeDirection.DOWN.ordinal())
        {
            return new int[]
            { 2 };
        } else
        {
            return new int[]
            { 1 };
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
        return side == ForgeDirection.DOWN.ordinal() ? true : false;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {
        if (slot != 2)
        {
            if (slot != 0)
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
