package ccm.compression.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ccm.compression.api.CompressedData;
import ccm.compression.client.renderer.block.CompressedBlockRenderer;
import ccm.compression.item.block.CompressedItemBlock;
import ccm.compression.tileentity.CompressedTile;
import ccm.compression.utils.lib.Archive;
import ccm.nucleum.omnium.utils.handler.TileHandler;

public class CompressedBlock extends BlockContainer
{
    public CompressedBlock(final int id)
    {
        super(id, Material.rock);
        setUnlocalizedName(Archive.COMPRESSED);
        GameRegistry.registerBlock(this, CompressedItemBlock.class, getUnlocalizedName());
        TileHandler.registerTile(Archive.COMPRESSED, CompressedTile.class);
    }

    @Override
    public TileEntity createNewTileEntity(final World world)
    {
        return TileHandler.getTileInstance(Archive.COMPRESSED);
    }

    @Override
    public int getRenderType()
    {
        return CompressedBlockRenderer.id;
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        return Block.slowSand.getBlockTextureFromSide(side);
    }

    private static CompressedTile getTile(final IBlockAccess world, final int x, final int y, final int z)
    {
        TileEntity tmp = world.getBlockTileEntity(x, y, z);
        if (tmp != null)
        {
            if (tmp instanceof CompressedTile)
            {
                return (CompressedTile) tmp;
            }
        }
        return null;
    }

    private static CompressedData getData(final IBlockAccess world, final int x, final int y, final int z)
    {
        CompressedTile tmp = getTile(world, x, y, z);
        if (tmp != null)
        {
            return tmp.data();
        }
        return null;
    }

    private static Block getBlock(final IBlockAccess world, final int x, final int y, final int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null)
        {
            Block block = tmp.getBlock();
            if (block != null)
            {
                return block;
            }
        }
        return Block.sponge;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        ItemStack stack = new ItemStack(world.getBlockId(x, y, z), 1, world.getBlockMetadata(x, y, z));
        CompressedData data = getData(world, x, y, z);
        if (data != null)
        {
            data.writeToItemStack(stack);
        }
        return stack;
    }

    @Override
    public void getSubBlocks(int id, CreativeTabs tab, List list)
    {
        for (CompressedType type : CompressedType.values())
        {
            ItemStack stack = new ItemStack(blockID, 1, type.ordinal());
            CompressedData.writeToItemStack(stack, Block.cobblestone.blockID, 0);
            list.add(stack);
        }
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        for (CompressedType type : CompressedType.values())
        {
            type.setOverlay(register.registerIcon(Archive.MOD_ID + ":condensedOverlay_" + type.ordinal()));
        }
    }

    @Override
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack item)
    {
        super.onBlockPlacedBy(world, x, y, z, entity, item);
        CompressedData data = getData(world, x, y, z);
        if (data != null)
        {
            data.readFromNBT(item.getTagCompound());
        }
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(final World world, final int x, final int y, final int z, final int metadata, final int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        CompressedData data = getData(world, x, y, z);
        if (data != null)
        {
            int count = quantityDropped(metadata, fortune, world.rand);
            for (int i = 0; i < count; i++)
            {
                int id = idDropped(metadata, world.rand, fortune);
                if (id > 0)
                {
                    ItemStack stack = new ItemStack(id, 1, metadata);
                    data.writeToItemStack(stack);
                    ret.add(stack);
                }
            }
        }
        return ret;
    }

    @Override
    public Icon getBlockTexture(final IBlockAccess world, final int x, final int y, final int z, final int side)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getBlockTexture(world, x, y, z, side);
        }
        return getBlock(world, x, y, z).getIcon(side, ((CompressedTile) world.getBlockTileEntity(x, y, z)).data().getMeta());
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    @Override
    public void onBlockClicked(final World world, final int x, final int y, final int z, final EntityPlayer player)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            tmp.getSpecial().onBlockClicked(world, x, y, z, player);
            return;
        }
        getBlock(world, x, y, z).onBlockClicked(world, x, y, z, player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            tmp.getSpecial().randomDisplayTick(world, x, y, z, rand);
            return;
        }
        getBlock(world, x, y, z).randomDisplayTick(world, x, y, z, rand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Goes straight to getLightBrightnessForSkyBlocks for Blocks, does some fancy computing for Fluids
     */
    public int getMixedBrightnessForBlock(final IBlockAccess world, final int x, final int y, final int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getMixedBrightnessForBlock(world, x, y, z);
        }
        return getBlock(world, x, y, z).getMixedBrightnessForBlock(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * How bright to render this block based on the light its receiving. Args: iBlockAccess, x, y, z
     */
    public float getBlockBrightness(final IBlockAccess world, final int x, final int y, final int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getBlockBrightness(world, x, y, z);
        }
        return getBlock(world, x, y, z).getBlockBrightness(world, x, y, z);
    }

    /**
     * Can add to the passed in vector for a movement vector to be applied to the entity. Args: x, y, z, entity, vec3d
     */
    @Override
    public void velocityToAddToEntity(final World world, final int x, final int y, final int z, final Entity entity, final Vec3 par6Vec3)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            tmp.getSpecial().velocityToAddToEntity(world, x, y, z, entity, par6Vec3);
            return;
        }
        getBlock(world, x, y, z).velocityToAddToEntity(world, x, y, z, entity, par6Vec3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World world, final int x, final int y, final int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getSelectedBoundingBoxFromPool(world, x, y, z);
        }
        return getBlock(world, x, y, z).getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a different metadata
     * value, but before the new metadata value
     * is set. Args: World, x, y, z, old block ID, old metadata
     */
    @Override
    public void breakBlock(final World world, final int x, final int y, final int z, final int par5, final int par6)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            tmp.getSpecial().breakBlock(world, x, y, z, par5, par6);
            return;
        }
        getBlock(world, x, y, z).breakBlock(world, x, y, z, par5, par6);
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    @Override
    public void onEntityWalking(final World world, final int x, final int y, final int z, final Entity entity)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            tmp.getSpecial().onEntityWalking(world, x, y, z, entity);
            return;
        }
        getBlock(world, x, y, z).onEntityWalking(world, x, y, z, entity);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(final World world, final int x, final int y, final int z, final Random rand)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            tmp.getSpecial().updateTick(world, x, y, z, rand);
            return;
        }
        getBlock(world, x, y, z).updateTick(world, x, y, z, rand);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(final World world,
                                    final int x,
                                    final int y,
                                    final int z,
                                    final EntityPlayer player,
                                    final int par6,
                                    final float par7,
                                    final float par8,
                                    final float par9)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
        }
        return getBlock(world, x, y, z).onBlockActivated(world, x, y, z, player, 0, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    @Override
    public void onBlockDestroyedByExplosion(final World world, final int x, final int y, final int z, final Explosion explosion)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            tmp.getSpecial().onBlockDestroyedByExplosion(world, x, y, z, explosion);
            return;
        }
        getBlock(world, x, y, z).onBlockDestroyedByExplosion(world, x, y, z, explosion);
    }

    @Override
    public float getExplosionResistance(final Entity entity,
                                        final World world,
                                        final int x,
                                        final int y,
                                        final int z,
                                        final double explosionX,
                                        final double explosionY,
                                        final double explosionZ)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getExplosionResistance(entity, world, x, y, z, explosionX, explosionY, explosionZ);
        }
        int metadata = world.getBlockMetadata(x, y, z);
        return ((getBlock(world, x, y, z).getExplosionResistance(entity)) * ((int) Math.pow(2, 1 + metadata)));
    }

    @Override
    public float getBlockHardness(final World world, final int x, final int y, final int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getBlockHardness(world, x, y, z);
        }
        int metadata = world.getBlockMetadata(x, y, z);
        return ((getBlock(world, x, y, z).getBlockHardness(world, x, y, z)) * ((int) Math.pow(2, 1 + metadata)));
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int oldmeta)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            tmp.getSpecial().onNeighborBlockChange(world, x, y, z, oldmeta);
            return;
        }
        getBlock(world, x, y, z).onNeighborBlockChange(world, x, y, z, oldmeta);
    }

    @Override
    public void onNeighborTileChange(World world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            tmp.getSpecial().onNeighborTileChange(world, x, y, z, tileX, tileY, tileZ);
            return;
        }
        getBlock(world, x, y, z).onNeighborTileChange(world, x, y, z, tileX, tileY, tileZ);
    }

    @Override
    public boolean canBeReplacedByLeaves(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().canBeReplacedByLeaves(world, x, y, z);
        }
        return getBlock(world, x, y, z).canBeReplacedByLeaves(world, x, y, z);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().canBlockStay(world, x, y, z);
        }
        return getBlock(world, x, y, z).canBlockStay(world, x, y, z);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().canConnectRedstone(world, x, y, z, side);
        }
        return getBlock(world, x, y, z).canConnectRedstone(world, x, y, z, side);
    }

    @Override
    public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().canEntityDestroy(world, x, y, z, entity);
        }
        return getBlock(world, x, y, z).canEntityDestroy(world, x, y, z, entity);
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().canSilkHarvest(world, player, x, y, z, metadata);
        }
        return getBlock(world, x, y, z).canSilkHarvest(world, player, x, y, z, metadata);
    }

    @Override
    public boolean canSustainLeaves(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().canSustainLeaves(world, x, y, z);
        }
        return getBlock(world, x, y, z).canSustainLeaves(world, x, y, z);
    }

    @Override
    public float getAmbientOcclusionLightValue(IBlockAccess world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getAmbientOcclusionLightValue(world, x, y, z);
        }
        return getBlock(world, x, y, z).getAmbientOcclusionLightValue(world, x, y, z);
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getBlocksMovement(world, x, y, z);
        }
        return getBlock(world, x, y, z).getBlocksMovement(world, x, y, z);
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int par5)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getComparatorInputOverride(world, x, y, z, par5);
        }
        return getBlock(world, x, y, z).getComparatorInputOverride(world, x, y, z, par5);
    }

    @Override
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getFireSpreadSpeed(world, x, y, z, metadata, face);
        }
        return (getBlock(world, x, y, z).getFireSpreadSpeed(world, x, y, z, metadata, face)) / 9;
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getFlammability(world, x, y, z, metadata, face);
        }
        return (getBlock(world, x, y, z).getFlammability(world, x, y, z, metadata, face)) * 9;
    }

    @Override
    public int getLightOpacity(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getLightOpacity(world, x, y, z);
        }
        return getBlock(world, x, y, z).getLightOpacity(world, x, y, z);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().canCreatureSpawn(type, world, x, y, z);
        }
        return getBlock(world, x, y, z).canCreatureSpawn(type, world, x, y, z);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().canPlaceBlockAt(world, x, y, z);
        }
        return getBlock(world, x, y, z).canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int par5, ItemStack item)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().canPlaceBlockOnSide(world, x, y, z, par5, item);
        }
        return getBlock(world, x, y, z).canPlaceBlockOnSide(world, x, y, z, par5, item);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int par5)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().canPlaceBlockOnSide(world, x, y, z, par5);
        }
        return getBlock(world, x, y, z).canPlaceBlockOnSide(world, x, y, z, par5);
    }

    @Override
    public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().canPlaceTorchOnTop(world, x, y, z);
        }
        return getBlock(world, x, y, z).canPlaceTorchOnTop(world, x, y, z);
    }

    @Override
    public float getEnchantPowerBonus(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getEnchantPowerBonus(world, x, y, z);
        }
        return (getBlock(world, x, y, z).getEnchantPowerBonus(world, x, y, z)) * 9;
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getPlayerRelativeBlockHardness(player, world, x, y, z);
        }
        return getBlock(world, x, y, z).getPlayerRelativeBlockHardness(player, world, x, y, z);
    }

    @Override
    public ForgeDirection[] getValidRotations(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().getValidRotations(world, x, y, z);
        }
        return getBlock(world, x, y, z).getValidRotations(world, x, y, z);
    }

    @Override
    public boolean isBeaconBase(World world, int x, int y, int z, int beaconX, int beaconY, int beaconZ)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().isBeaconBase(world, x, y, z, beaconX, beaconY, beaconZ);
        }
        return getBlock(world, x, y, z).isBeaconBase(world, x, y, z, beaconX, beaconY, beaconZ);
    }

    @Override
    public boolean isBlockBurning(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().isBlockBurning(world, x, y, z);
        }
        return getBlock(world, x, y, z).isBlockBurning(world, x, y, z);
    }

    @Override
    public boolean isBlockFoliage(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().isBlockFoliage(world, x, y, z);
        }
        return getBlock(world, x, y, z).isBlockFoliage(world, x, y, z);
    }

    @Override
    public boolean isFertile(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().isFertile(world, x, y, z);
        }
        return getBlock(world, x, y, z).isFertile(world, x, y, z);
    }

    @Override
    public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().isFireSource(world, x, y, z, metadata, side);
        }
        return getBlock(world, x, y, z).isFireSource(world, x, y, z, metadata, side);
    }

    @Override
    public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().isFlammable(world, x, y, z, metadata, face);
        }
        return getBlock(world, x, y, z).isFlammable(world, x, y, z, metadata, face);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int par5)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().isProvidingStrongPower(world, x, y, z, par5);
        }
        return getBlock(world, x, y, z).isProvidingStrongPower(world, x, y, z, par5);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int par5)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().isProvidingWeakPower(world, x, y, z, par5);
        }
        return getBlock(world, x, y, z).isProvidingWeakPower(world, x, y, z, par5);
    }

    @Override
    public boolean isWood(World world, int x, int y, int z)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().isWood(world, x, y, z);
        }
        return getBlock(world, x, y, z).isWood(world, x, y, z);
    }

    @Override
    public boolean shouldCheckWeakPower(World world, int x, int y, int z, int side)
    {
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().shouldCheckWeakPower(world, x, y, z, side);
        }
        return getBlock(world, x, y, z).shouldCheckWeakPower(world, x, y, z, side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
    { // TODO REPLACE
        CompressedData tmp = getData(world, x, y, z);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().addBlockDestroyEffects(world, x, y, z, meta, effectRenderer);
        }
        return getBlock(world, x, y, z).addBlockDestroyEffects(world, x, y, z, meta, effectRenderer);
    }

    @Override
    public boolean addBlockHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
    { // TODO REDO
        CompressedData tmp = getData(world, target.blockX, target.blockY, target.blockZ);
        if (tmp != null && tmp.isSpecial())
        {
            return tmp.getSpecial().addBlockHitEffects(world, target, effectRenderer);
        }
        return getBlock(world, target.blockX, target.blockY, target.blockZ).addBlockHitEffects(world, target, effectRenderer);
    }
}