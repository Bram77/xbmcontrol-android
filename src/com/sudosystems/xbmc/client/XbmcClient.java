package com.sudosystems.xbmc.client;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class XbmcClient 
{
    public SystemClient System;
    public FilesClient Files;
    public PlayerClient Player;
    public AudioLibraryClient AudioLibrary;
    public VideoLibraryClient VideoLibrary;
    public PlaylistClient Playlist;
    
    public XbmcClient(Context context, JSONObject configurationData)
    {
        Configuration configuration = new Configuration(configurationData);
        System                      = new SystemClient(context, configuration);
        Files                       = new FilesClient(context, configuration);
        Player                      = new PlayerClient(context, configuration);
        AudioLibrary                = new AudioLibraryClient(context, configuration);
        VideoLibrary                = new VideoLibraryClient(context, configuration);
        Playlist                    = new PlaylistClient(context, configuration);
    }
}
