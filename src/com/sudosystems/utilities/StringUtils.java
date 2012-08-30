package com.sudosystems.utilities;

import org.apache.commons.validator.routines.UrlValidator;

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
}
