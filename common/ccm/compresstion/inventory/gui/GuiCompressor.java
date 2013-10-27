package ccm.compresstion.inventory.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

import ccm.compresstion.inventory.container.ContainerCompressor;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.inventory.gui.BaseGui;
import ccm.nucleum.omnium.tileentity.ProgressTE;

public class GuiCompressor extends BaseGui
{

    public GuiCompressor(Container container)
    {
        super(container, Archive.COMPRESSOR);
        // FINISH
    }
    
    @Override
    public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z)
    {
        return new GuiCompressor(new ContainerCompressor((ProgressTE)world.getBlockTileEntity(x, y, z)));
    }
}