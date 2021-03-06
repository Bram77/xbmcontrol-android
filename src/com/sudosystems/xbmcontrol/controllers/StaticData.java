package com.sudosystems.xbmcontrol.controllers;

public final class StaticData
{
    //Storage
    public static final String STORAGE_CONFIGURATION    = "configuration";
    public static final String STORAGE_NOWPLAYING       = "now_playing";
    
    //Storage entries
    public static final String STORAGE_CONFIGURATION_CONNECTION     = "connection";
    public static final String STORAGE_CONFIGURATION_HIDE_WATCHED   = "hide_watched";
    
    //Media types
    public final static String MEDIA_TYPE_VIDEO     = "video";
    public final static String MEDIA_TYPE_AUDIO     = "music";
    public final static String MEDIA_TYPE_PICTURES  = "pictures";
    public final static String PLAYLISTS_TYPE_AUDIO     = "special://musicplaylists";
    public final static String PLAYLISTS_TYPE_VIDEO     = "special://videoplaylists";
    
    //Other
    public final static int PING_INTERVAL = 10; //seconds
}
