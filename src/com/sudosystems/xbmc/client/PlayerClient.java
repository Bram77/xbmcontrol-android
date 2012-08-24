package com.sudosystems.xbmc.client;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.xbmc.client.FilesClient.MediaType;
import com.sudosystems.xbmc.client.PlaylistClient.PlaylistType;

public class PlayerClient  extends JsonRpcClient
{
    private static final String NAMESPACE = "Player.";
    
    public PlayerClient(Context context)
    {
        super(context);
    }
    
    public void getNowPlayingItem(int playerId, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params = new JSONObject();

        try
        {
            params.put("playerid", playerId);
        }
        catch(JSONException e)
        {
            Log.v("FilesClient::getSources", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "GetItem", params, responseHandler);
    }
    
    public void getActivePlayers(JsonHttpResponseHandler responseHandler)
    {
        post(NAMESPACE+ "GetActivePlayers", responseHandler);
    }
    
    public void playFile(String filePath, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params       = new JSONObject();
        JSONObject itemParams   = new JSONObject();

        try
        {
            itemParams.put("file", filePath);
            params.put("item", itemParams);
        }
        catch(JSONException e)
        {
            Log.v("PlayerClient::playFile", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "open", params, responseHandler);
    }
    
    public void playDirectory(String directoryPath, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params       = new JSONObject();
        JSONObject itemParams   = new JSONObject();

        try
        {
            itemParams.put("file", directoryPath);
            params.put("item", itemParams);
        }
        catch(JSONException e)
        {
            Log.v("PlayerClient::playFile", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "open", params, responseHandler);
    }
    
    public void playPlaylist(String mediaType, int itemIndex, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params       = new JSONObject();
        JSONObject itemParams   = new JSONObject();

        try
        {
            itemParams.put("playlistid", PlaylistType.getPlaylistId(mediaType));
            itemParams.put("position", itemIndex);
            params.put("item", itemParams);
        }
        catch(JSONException e)
        {
            Log.v("PlayerClient::playFile", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "open", params, responseHandler);
    }
    
    public void playPlaylist(String mediaType, JsonHttpResponseHandler responseHandler)
    {
        playPlaylist(mediaType, 0, responseHandler);
    }
    
    public final static class PlayerType
    {
        public final static int VIDEO    = 1;
        public final static int AUDIO    = 0;
        public final static int PICTURES = 2;
        
        public static final int getPlayerId(String mediaType)
        {
            int playlistId = -1;
            
            if(mediaType.equals(MediaType.AUDIO))
            {
                playlistId = PlayerType.AUDIO;
            }
            else if(mediaType.equals(MediaType.VIDEO))
            {
                playlistId = PlayerType.VIDEO;
            }
            else if(mediaType.equals(MediaType.PICTURES))
            {
                playlistId = PlayerType.PICTURES;
            }
            
            return playlistId;
        }
    }
}
