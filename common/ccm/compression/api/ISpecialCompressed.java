package ccm.compression.api;

import java.util.Random;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * ISpecialCompressed
 * <p>
 * TODO UTILIZE
 * 
 * @author Captain_Shadows
 */
public interface ISpecialCompressed
{
    public Icon getBlockTexture(final IBlockAccess world, final int x, final int y, final int z, final int side);

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    public void onBlockClicked(final World world, final int x, final int y, final int z, final EntityPlayer player);

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand);

    /**
     * Goes straight to getLightBrightnessForSkyBlocks for Blocks, does some fancy computing for Fluids
     */
    @SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(final IBlockAccess world, final int x, final int y, final int z);

    /**
     * How bright to render this block based on the light its receiving. Args: iBlockAccess, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public float getBlockBrightness(final IBlockAccess world, final int x, final int y, final int z);

    /**
     * Can add to the passed in vector for a movement vector to be applied to the entity. Args: x, y, z, entity, vec3d
     */
    public void velocityToAddToEntity(final World world, final int x, final int y, final int z, final Entity entity, final Vec3 par6Vec3);

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World world, final int x, final int y, final int z);

    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a different metadata
     * value, but before the new metadata value
     * is set. Args: World, x, y, z, old block ID, old metadata
     */
    public void breakBlock(final World world, final int x, final int y, final int z, final int par5, final int par6);

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    public void onEntityWalking(final World world, final int x, final int y, final int z, final Entity entity);

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(final World world, final int x, final int y, final int z, final Random rand);

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(final World world,
                                    final int x,
                                    final int y,
                                    final int z,
                                    final EntityPlayer player,
                                    final int par6,
                                    final float par7,
                                    final float par8,
                                    final float par9);

    /**
     * Called upon the block being destroyed by an explosion
     */
    public void onBlockDestroyedByExplosion(final World world, final int x, final int y, final int z, final Explosion explosion);

    public float getExplosionResistance(final Entity entity,
                                        final World world,
                                        final int x,
                                        final int y,
                                        final int z,
                                        final double explosionX,
                                        final double explosionY,
                                        final double explosionZ);

    public float getBlockHardness(final World world, final int x, final int y, final int z);

    public void onNeighborBlockChange(World world, int x, int y, int z, int oldmeta);

    public void onNeighborTileChange(World world, int x, int y, int z, int tileX, int tileY, int tileZ);

    public boolean canBeReplacedByLeaves(World world, int x, int y, int z);

    public boolean canBlockStay(World world, int x, int y, int z);

    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side);

    public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity);

    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata);

    public boolean canSustainLeaves(World world, int x, int y, int z);

    public float getAmbientOcclusionLightValue(IBlockAccess world, int x, int y, int z);

    public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z);

    public int getComparatorInputOverride(World world, int x, int y, int z, int par5);

    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face);

    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face);

    public int getLightOpacity(World world, int x, int y, int z);

    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z);

    public boolean canPlaceBlockAt(World world, int x, int y, int z);

    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int par5, ItemStack item);

    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int par5);

    public boolean canPlaceTorchOnTop(World world, int x, int y, int z);

    public float getEnchantPowerBonus(World world, int x, int y, int z);

    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z);

    public ForgeDirection[] getValidRotations(World world, int x, int y, int z);

    public boolean isBeaconBase(World world, int x, int y, int z, int beaconX, int beaconY, int beaconZ);

    public boolean isBlockBurning(World world, int x, int y, int z);

    public boolean isBlockFoliage(World world, int x, int y, int z);

    public boolean isFertile(World world, int x, int y, int z);

    public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side);

    public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face);

    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int par5);

    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int par5);

    public boolean isWood(World world, int x, int y, int z);

    public boolean shouldCheckWeakPower(World world, int x, int y, int z, int side);

    @SideOnly(Side.CLIENT)
    public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer);

    public boolean addBlockHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer);
}