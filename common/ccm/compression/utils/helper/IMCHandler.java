package ccm.compression.utils.helper;

import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;

public class IMCHandler
{
    public static void processIMCMessages(IMCEvent event)
    {
        for (IMCMessage imcMessage : event.getMessages())
        {
            processIMCMessage(imcMessage);
        }
    }

    private static void processIMCMessage(IMCMessage imcMessage)
    {
        String requestedOperation = imcMessage.key;
    }
}