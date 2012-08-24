package com.sudosystems.xbmc.client;

import android.content.Context;

public class XbmcClient 
{
    public SystemClient System;
    public FilesClient Files;
    public PlayerClient Player;
    public HelperMethods Helper;
    public AudioLibraryClient AudioLibrary;
    public VideoLibraryClient VideoLibrary;
    public PlaylistClient Playlist;
    public boolean debug = true;
    
    public XbmcClient(Context context)
    {
        Helper          = new HelperMethods();
        System          = new SystemClient(context);
        Files           = new FilesClient(context);
        Player          = new PlayerClient(context);
        AudioLibrary    = new AudioLibraryClient(context);
        VideoLibrary    = new VideoLibraryClient(context);
        Playlist        = new PlaylistClient(context);
    }
    
    public void enableDebugging(boolean enable)
    {
        debug = enable;
    }
}
