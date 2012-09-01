package com.sudosystems.utilities;

public class ImageDownload
{
    public ImageDownload(String imageUrl, String username, String password, DownloadCompleteListener callback)
    {
        new DownloadUtil("image", imageUrl, username, password, callback).execute();
    }
    
    /*
    public ImageDownload(String imageUrl, DownloadCompleteListener callback)
    {
        new DownloadUtil("image", imageUrl, null, null, callback).execute();
    }
    */
}
