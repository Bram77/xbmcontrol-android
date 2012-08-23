package com.sudosystems.xbmc.client;

public class HelperMethods
{
    public HelperMethods()
    {}
    
    public String getOneDirectoryUp(String psCurrentPath)
    {
        String lsWithoutTrailingSlash = psCurrentPath.substring(0, psCurrentPath.lastIndexOf('/'));
        return psCurrentPath.substring(0, lsWithoutTrailingSlash.lastIndexOf('/'))+"/";
    }
}
