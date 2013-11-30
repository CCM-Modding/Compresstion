package ccm.compresstion.inventory.gui;

import lib.cofh.gui.GuiBase;
import lib.cofh.gui.element.ElementDualScaled;

public class ProgressElement extends ElementDualScaled
{
    public ProgressElement(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
        setMode(Mode.RIGHT);
    }
}