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

    private static Block getBlock(final IBlockAccess world, final int x, final int y, final int z)
    {
        CompressedTile tmp = getTile(world, x, y, z);
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
        CompressedTile tile = getTile(world, x, y, z);
        if (tile != null)
        {
            tile.saveToItemStack(stack);
        }
        return stack;
    }

    @Override
    public void getSubBlocks(int id, CreativeTabs tab, List list)
    {
        for (CompressedType type : CompressedType.values())
        {
            ItemStack stack = new ItemStack(blockID, 1, type.ordinal());
            CompressedTile.fakeSave(stack, Block.cobblestone.blockID, 0);
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
        CompressedTile tile = getTile(world, x, y, z);
        if (tile != null)
        {
            tile.loadFromItemStack(item);
        }
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(final World world, final int x, final int y, final int z, final int metadata, final int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        CompressedTile tile = getTile(world, x, y, z);
        if (tile != null)
        {
            int count = quantityDropped(metadata, fortune, world.rand);
            for (int i = 0; i < count; i++)
            {
                int id = idDropped(metadata, world.rand, fortune);
                if (id > 0)
                {
                    ItemStack stack = new ItemStack(id, 1, metadata);
                    tile.saveToItemStack(stack);
                    ret.add(stack);
                }
            }
        }
        return ret;
    }

    @Override
    public Icon getBlockTexture(final IBlockAccess world, final int x, final int y, final int z, final int side)
    {
        return getBlock(world, x, y, z).getIcon(side, ((CompressedTile) world.getBlockTileEntity(x, y, z)).getMeta());
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    @Override
    public void onBlockClicked(final World world, final int x, final int y, final int z, final EntityPlayer player)
    {
        getBlock(world, x, y, z).onBlockClicked(world, x, y, z, player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand)
    {
        getBlock(world, x, y, z).randomDisplayTick(world, x, y, z, rand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Goes straight to getLightBrightnessForSkyBlocks for Blocks, does some fancy computing for Fluids
     */
    public int getMixedBrightnessForBlock(final IBlockAccess world, final int x, final int y, final int z)
    {
        return getBlock(world, x, y, z).getMixedBrightnessForBlock(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * How bright to render this block based on the light its receiving. Args: iBlockAccess, x, y, z
     */
    public float getBlockBrightness(final IBlockAccess world, final int x, final int y, final int z)
    {
        return getBlock(world, x, y, z).getBlockBrightness(world, x, y, z);
    }

    /**
     * Can add to the passed in vector for a movement vector to be applied to the entity. Args: x, y, z, entity, vec3d
     */
    @Override
    public void velocityToAddToEntity(final World world, final int x, final int y, final int z, final Entity entity, final Vec3 par6Vec3)
    {
        getBlock(world, x, y, z).velocityToAddToEntity(world, x, y, z, entity, par6Vec3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World world, final int x, final int y, final int z)
    {
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
        getBlock(world, x, y, z).breakBlock(world, x, y, z, par5, par6);
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    @Override
    public void onEntityWalking(final World world, final int x, final int y, final int z, final Entity entity)
    {
        getBlock(world, x, y, z).onEntityWalking(world, x, y, z, entity);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(final World world, final int x, final int y, final int z, final Random rand)
    {
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
        return getBlock(world, x, y, z).onBlockActivated(world, x, y, z, player, 0, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    @Override
    public void onBlockDestroyedByExplosion(final World world, final int x, final int y, final int z, final Explosion explosion)
    {
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
        int metadata = world.getBlockMetadata(x, y, z);
        return ((getBlock(world, x, y, z).getExplosionResistance(entity)) * ((int) Math.pow(2, 1 + metadata)));
    }

    @Override
    public float getBlockHardness(final World world, final int x, final int y, final int z)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        return ((getBlock(world, x, y, z).getBlockHardness(world, x, y, z)) * ((int) Math.pow(2, 1 + metadata)));
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int oldmeta)
    {
        getBlock(world, x, y, z).onNeighborBlockChange(world, x, y, z, oldmeta);
    }

    @Override
    public void onNeighborTileChange(World world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        getBlock(world, x, y, z).onNeighborTileChange(world, x, y, z, tileX, tileY, tileZ);
    }

    @Override
    public boolean canBeReplacedByLeaves(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).canBeReplacedByLeaves(world, x, y, z);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).canBlockStay(world, x, y, z);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        return getBlock(world, x, y, z).canConnectRedstone(world, x, y, z, side);
    }

    @Override
    public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity)
    {
        return getBlock(world, x, y, z).canEntityDestroy(world, x, y, z, entity);
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
        return getBlock(world, x, y, z).canSilkHarvest(world, player, x, y, z, metadata);
    }

    @Override
    public boolean canSustainLeaves(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).canSustainLeaves(world, x, y, z);
    }

    @Override
    public float getAmbientOcclusionLightValue(IBlockAccess world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).getAmbientOcclusionLightValue(world, x, y, z);
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).getBlocksMovement(world, x, y, z);
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int par5)
    {
        return getBlock(world, x, y, z).getComparatorInputOverride(world, x, y, z, par5);
    }

    @Override
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face)
    {
        return (getBlock(world, x, y, z).getFireSpreadSpeed(world, x, y, z, metadata, face)) / 9;
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
    {
        return (getBlock(world, x, y, z).getFlammability(world, x, y, z, metadata, face)) * 9;
    }

    @Override
    public int getLightOpacity(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).getLightOpacity(world, x, y, z);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).canCreatureSpawn(type, world, x, y, z);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int par5, ItemStack par6ItemStack)
    {
        return getBlock(world, x, y, z).canPlaceBlockOnSide(world, x, y, z, par5, par6ItemStack);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int par5)
    {
        return getBlock(world, x, y, z).canPlaceBlockOnSide(world, x, y, z, par5);
    }

    @Override
    public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).canPlaceTorchOnTop(world, x, y, z);
    }

    @Override
    public float getEnchantPowerBonus(World world, int x, int y, int z)
    {
        return (getBlock(world, x, y, z).getEnchantPowerBonus(world, x, y, z)) * 9;
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).getPlayerRelativeBlockHardness(player, world, x, y, z);
    }

    @Override
    public ForgeDirection[] getValidRotations(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).getValidRotations(world, x, y, z);
    }

    @Override
    public boolean isBeaconBase(World world, int x, int y, int z, int beaconX, int beaconY, int beaconZ)
    {
        return getBlock(world, x, y, z).isBeaconBase(world, x, y, z, beaconX, beaconY, beaconZ);
    }

    @Override
    public boolean isBlockBurning(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).isBlockBurning(world, x, y, z);
    }

    @Override
    public boolean isBlockFoliage(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).isBlockFoliage(world, x, y, z);
    }

    @Override
    public boolean isFertile(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).isFertile(world, x, y, z);
    }

    @Override
    public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side)
    {
        return getBlock(world, x, y, z).isFireSource(world, x, y, z, metadata, side);
    }

    @Override
    public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
    {
        return getBlock(world, x, y, z).isFlammable(world, x, y, z, metadata, face);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int par5)
    {
        return getBlock(world, x, y, z).isProvidingStrongPower(world, x, y, z, par5);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int par5)
    {
        return getBlock(world, x, y, z).isProvidingWeakPower(world, x, y, z, par5);
    }

    @Override
    public boolean isWood(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).isWood(world, x, y, z);
    }

    @Override
    public boolean shouldCheckWeakPower(World world, int x, int y, int z, int side)
    {
        return getBlock(world, x, y, z).shouldCheckWeakPower(world, x, y, z, side);
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

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
    { // TODO REPLACE
        return getBlock(world, x, y, z).addBlockDestroyEffects(world, x, y, z, meta, effectRenderer);
    }

    @Override
    public boolean addBlockHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
    { // TODO REDO
        return getBlock(world, target.blockX, target.blockY, target.blockZ).addBlockHitEffects(world, target, effectRenderer);
    }
}