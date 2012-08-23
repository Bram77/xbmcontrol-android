package com.sudosystems.xbmc.client;

import android.content.Context;

public class XbmcClient 
{
    public SystemClient System;
    public FilesClient Files;
    public boolean debug = true;
    
    public XbmcClient(Context context)
    {
        System  = new SystemClient(context);
        Files   = new FilesClient(context);
    }
    
    public void enableDebugging(boolean enable)
    {
        debug = enable;
    }
}
