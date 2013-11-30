package ccm.compresstion.inventory.gui;

import lib.cofh.gui.GuiBase;
import lib.cofh.gui.element.ElementDualScaled;

public class FireElement extends ElementDualScaled
{
    public FireElement(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
        setMode(Mode.UP);
    }
    
    
}