package com.sudosystems.xbmc.client;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

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
    
    public void open(String mediaType, JsonHttpResponseHandler responseHandler)
    {
        
    }
    
    public final class MediaType
    {
        public final static String VIDEO    = "video";
        public final static String AUDIO    = "music";
        public final static String PICTURES = "pictures";
    }
}
