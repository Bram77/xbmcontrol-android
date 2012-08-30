package com.sudosystems.utilities;

public class ImageDownload
{
    public ImageDownload(String imageUrl, DownloadCompleteListener callback)
    {
        new DownloadUtil("image", imageUrl, callback).execute();
    }
}
