package ccm.compresstion.inventory.gui;

import java.util.ArrayList;
import java.util.List;

import lib.cofh.gui.element.ElementBase;

public class ProgressManager
{
    public interface IProgressBar {
        public ElementBase getElement();
    }
    List<IProgressBar> progresses = new ArrayList<IProgressBar>();
}