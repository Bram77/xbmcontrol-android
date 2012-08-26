package com.sudosystems.xbmc.client;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class VideoLibraryClient  extends JsonRpcClient
{
    private static final String NAMESPACE = "AudioLibrary.";
    
    public VideoLibraryClient(Context context, Configuration configuration)
    {
        super(context, configuration);
    }
    
    public void getMovieDetails(int movieid, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params = new JSONObject();

        try
        {
            params.put("movieid", movieid);
        }
        catch(JSONException e)
        {
            Log.v("VideoLibraryClient::getMovieDetails", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "GetMovieDetails", params, responseHandler);
    }
    
    public void getEpisodeDetails(int episodeid, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params = new JSONObject();

        try
        {
            params.put("episodeid", episodeid);
        }
        catch(JSONException e)
        {
            Log.v("VideoLibraryClient::getEpisodeDetails", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "GetEpisodeDetails", params, responseHandler);
    }
}
