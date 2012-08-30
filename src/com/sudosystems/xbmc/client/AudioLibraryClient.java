package com.sudosystems.xbmc.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class AudioLibraryClient  extends JsonRpcClient
{
    private static final String NAMESPACE = "AudioLibrary.";
    
    public AudioLibraryClient(Context context, Configuration configuration)
    {
        super(context, configuration);
    }
    
    public void getSongDetails(int songid, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params = new JSONObject();

        try
        {
            params.put("songid", songid);
        }
        catch(JSONException e)
        {
            Log.v("AudioLibraryClient::getSongDetails", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "GetSongDetails", params, responseHandler);
    }
    
    public void GetRecentlyAddedAlbums(JsonHttpResponseHandler responseHandler)
    {
        JSONObject params       = new JSONObject();
        JSONObject limits       = new JSONObject();
        JSONArray properties    = new JSONArray();

        try
        {
            limits.put("start", 0);
            limits.put("end", 3);
            
            //properties.put("title");
            properties.put("artist");
            properties.put("thumbnail");
            //properties.put("type");
            //properties.put("artistid");
            
            params.put("limits", limits);
            params.put("properties", properties);
        }
        catch(JSONException e)
        {
            Log.v("VideoLibraryClient::getEpisodeDetails", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "GetRecentlyAddedAlbums", params, responseHandler);
    }
}
