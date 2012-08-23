package com.sudosystems.xbmc.client;

import android.content.Context;

public class XbmcClient 
{
    public SystemClient System;
    public FilesClient Files;
    public HelperMethods Helper;
    public boolean debug = true;
    
    public XbmcClient(Context context)
    {
        Helper  = new HelperMethods();
        System  = new SystemClient(context);
        Files   = new FilesClient(context);
    }
    
    public void enableDebugging(boolean enable)
    {
        debug = enable;
    }
}
