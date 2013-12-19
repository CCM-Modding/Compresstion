package ccm.compression.inventory.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import ccm.compression.inventory.container.ContainerCompressor;
import ccm.compression.tileentity.CompressorTile;
import ccm.compression.utils.lib.Archive;
import ccm.nucleum.omnium.inventory.container.BaseContainer;
import ccm.nucleum.omnium.inventory.gui.BaseGui;
import ccm.nucleum.omnium.inventory.gui.element.FireElement;
import ccm.nucleum.omnium.inventory.gui.element.ProgressElement;

public class GuiCompressor extends BaseGui
{
    private final FireElement fire;
    private final ProgressElement progress;
    private final CompressorTile tile;

    public GuiCompressor(BaseContainer container)
    {
        super(container, Archive.COMPRESSOR);
        tile = (CompressorTile) ((container != null) && (container.getIInventory() instanceof CompressorTile) ? container.getIInventory() : null);
        name = "tile.compressor.name";
        fire = new FireElement(this, 148, 65, Archive.COMPRESSOR);
        progress = new ProgressElement(this, 220, 72, Archive.COMPRESSOR);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        addElement(fire);
        addElement(progress);
    }

    @Override
    protected void updateElements()
    {
        super.updateElements();
        fire.setQuantity(tile.getCompressTimeScaled(14));
        progress.setQuantity(tile.getCompressProgressScaled(24));
    }

    @Override
    public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z)
    {
        return new GuiCompressor(new ContainerCompressor(player, (IInventory) world.getBlockTileEntity(x, y, z)));
    }
}