package com.sudosystems.xbmc.client;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.xbmc.client.FilesClient.MediaType;

public class PlaylistClient  extends JsonRpcClient
{
    private static final String NAMESPACE = "Playlist.";
    
    public PlaylistClient(Context context)
    {
        super(context);
    }
    
    private void add(String mediaType, String path, boolean isDirectory, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params       = new JSONObject();
        JSONObject itemParams   = new JSONObject();
        int playlistId          = PlaylistType.getPlaylistId(mediaType);
        String sourceType       = (isDirectory)? "directory" : "file";

        try
        {
            params.put("playlistid", playlistId);
            itemParams.put(sourceType, path);
            params.put("item", itemParams);
        }
        catch(JSONException e)
        {
            Log.v("PlaylistClient::add", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "Add", params, responseHandler);
    }
    
    public void addDirectory(String mediaType, String path, boolean clearPlaylist, JsonHttpResponseHandler responseHandler)
    {
        if(clearPlaylist)
        {
            clear(mediaType);
        }
        
        add(mediaType, path, true, responseHandler);
    }
    
    public void addFile(String mediaType, String path, boolean clearPlaylist, JsonHttpResponseHandler responseHandler)
    {
        if(clearPlaylist)
        {
            clear(mediaType);
        }
        
        add(mediaType, path, false, responseHandler);
    }
    
    public void addDirectory(String mediaType, String path, JsonHttpResponseHandler responseHandler)
    {
        add(mediaType, path, true, responseHandler);
    }
    
    public void addFile(String mediaType, String path, JsonHttpResponseHandler responseHandler)
    {
        add(mediaType, path, false, responseHandler);
    }
    
    public void clear(String mediaType)
    {
        JSONObject params = new JSONObject();
        int playlistId    = PlaylistType.getPlaylistId(mediaType);

        try
        {
            params.put("playlistid", playlistId);
        }
        catch(JSONException e)
        {
            Log.v("FilesClient::getSources", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "Clear", params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONObject result)
            {
                Log.v("Playlist.Clear", result.toString());
            }
            
            @Override
            public void onFailure(Throwable e, String response)
            {
                Log.e("Playlist.Clear", e.toString());
            }
        });
    }
    
    public final static class PlaylistType
    {
        public final static int VIDEO    = 1;
        public final static int AUDIO    = 0;
        public final static int PICTURES = 2;
        
        public static final int getPlaylistId(String mediaType)
        {
            int playlistId = -1;
            
            if(mediaType.equals(MediaType.AUDIO))
            {
                playlistId = PlaylistType.AUDIO;
            }
            else if(mediaType.equals(MediaType.VIDEO))
            {
                playlistId = PlaylistType.VIDEO;
            }
            else if(mediaType.equals(MediaType.PICTURES))
            {
                playlistId = PlaylistType.PICTURES;
            }
            
            return playlistId;
        }
    }
}
