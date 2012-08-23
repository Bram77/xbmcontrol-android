package com.sudosystems.xbmc.client;

public class Helper
{
    public Helper()
    {}
    
    public String getOneDirectoryUp(String psCurrentPath)
    {
        return psCurrentPath.substring(0, psCurrentPath.lastIndexOf('/'));
    }
}
