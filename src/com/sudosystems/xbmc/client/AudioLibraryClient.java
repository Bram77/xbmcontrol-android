package com.sudosystems.xbmc.client;

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
}
