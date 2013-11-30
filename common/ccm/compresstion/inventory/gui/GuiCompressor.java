package ccm.compresstion.inventory.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import ccm.compresstion.inventory.container.ContainerCompressor;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.inventory.gui.BaseGui;

public class GuiCompressor extends BaseGui
{

    public GuiCompressor(Container container)
    {
        super(container, Archive.COMPRESSOR);
        name = "tile.compressor.name";
        // FINISH
    }

    @Override
    public void initGui()
    {
        super.initGui();
        //addElement(new FireElement(this, 0, 0));
        //addElement(new ProgressElement(this, 50, 0));
    }
    
    @Override
    public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z)
    {
        return new GuiCompressor(new ContainerCompressor(player, (IInventory) world.getBlockTileEntity(x, y, z)));
    }
}