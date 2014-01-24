package ccm.compression.utils.handler;

import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;

import ccm.compression.api.CompressedData;
import ccm.compression.api.CompressionPerms;

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
        String rOperation = imcMessage.key;
        // if (rOperation.equalsIgnoreCase(CompressedData.IMC_LIST_WHITE))
        // {
        // if (imcMessage.isItemStackMessage())
        // {
        // TODO FIX
        // CompressionPerms.addWhiteList(imcMessage.getItemStackValue(), imcMessage.getItemStackValue());
        // }
        // }
        if (rOperation.equalsIgnoreCase(CompressedData.IMC_LIST_BLACK))
        {
            if (imcMessage.isItemStackMessage())
            {
                CompressionPerms.addBlackList(imcMessage.getItemStackValue());
            }
        }
    }
}