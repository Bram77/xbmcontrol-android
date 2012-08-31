package com.sudosystems.utilities;

import org.apache.commons.validator.routines.UrlValidator;

import android.util.Log;

public final class StringUtils
{   
    public final static boolean isValidWebUrl(String url)
    {
        String[] schemes            = {"http", "https", "ftp"};
        UrlValidator urlValidator   = new UrlValidator(schemes);
        
        return (urlValidator.isValid(url));
    }
    
    public final static boolean isValidSambaPath(String url)
    {
        String[] schemes            = {"\\\\", "smb"};
        UrlValidator urlValidator   = new UrlValidator(schemes, UrlValidator.ALLOW_ALL_SCHEMES);
        
        return (urlValidator.isValid(url));
    }
    
    public final static String getDirectoryNameFormUrl(String url)
    {
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }
    
    public final static String getFileNameFormUrl(String url, boolean removeExtension)
    {
        int slashIndex  = url.lastIndexOf("/");
        int dotIndex    = url.lastIndexOf(".");
        Log.v("DOT", Integer.toString(dotIndex));
        return (removeExtension && dotIndex > -1)? url.substring((slashIndex+1), dotIndex) : url.substring((slashIndex+1));
    }
    
    public final static String getFileNameFormUrl(String url)
    {
        return getFileNameFormUrl(url, false);
    }
}
