package com.sudosystems.xbmcontrol.controllers;

public class Util
{
    public Util()
    {}
    
    public static String getOneDirectoryUp(String psCurrentPath)
    {
        String lsWithoutTrailingSlash = psCurrentPath.substring(0, psCurrentPath.lastIndexOf('/'));
        return psCurrentPath.substring(0, lsWithoutTrailingSlash.lastIndexOf('/'))+"/";
    }
}
