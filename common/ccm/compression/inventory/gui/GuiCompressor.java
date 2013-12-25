package ccm.compression.inventory.gui;

import lib.cofh.gui.element.ElementDualScaled;
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
    private final CompressorTile tile;
    private ElementDualScaled fire;
    private ElementDualScaled progress;

    public GuiCompressor(BaseContainer container)
    {
        super(container, Archive.COMPRESSOR);
        tile = (CompressorTile) ((container != null) && (container.getIInventory() instanceof CompressorTile) ? container.getIInventory() : null);
        name = "tile.compressor.name";
    }

    @Override
    public void initGui()
    {
        super.initGui();
        fire = (ElementDualScaled) addElement(new FireElement(this, 24, 28, Archive.COMPRESSOR));
        progress = (ElementDualScaled) addElement(new ProgressElement(this, 95, 35, Archive.COMPRESSOR));
    }

    @Override
    protected void updateElements()
    {
        super.updateElements();
        fire.setQuantity(tile.getCompressTimeScaled(12));
        progress.setQuantity(tile.getCompressProgressScaled(24));
    }

    @Override
    public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z)
    {
        return new GuiCompressor(new ContainerCompressor(player, (IInventory) world.getBlockTileEntity(x, y, z)));
    }
}