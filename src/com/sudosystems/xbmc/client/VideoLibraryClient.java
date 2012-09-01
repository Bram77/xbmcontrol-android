package com.sudosystems.xbmc.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class VideoLibraryClient  extends JsonRpcClient
{
    private static final String NAMESPACE = "VideoLibrary.";
    
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
    
    public void GetRecentlyAddedEpisodes(JsonHttpResponseHandler responseHandler)
    {
        JSONObject params       = new JSONObject();
        JSONObject limits       = new JSONObject();
        JSONArray properties    = new JSONArray();

        try
        {
            limits.put("start", 0);
            limits.put("end", 5);
            
            //properties.put("title");
            //properties.put("season");
            //properties.put("episode");
            properties.put("showtitle");
            properties.put("playcount");
            properties.put("thumbnail");
            
            params.put("limits", limits);
            params.put("properties", properties);
        }
        catch(JSONException e)
        {
            Log.v("VideoLibraryClient::getEpisodeDetails", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "GetRecentlyAddedEpisodes", params, responseHandler);
    }
    
    public void GetRecentlyAddedMovies(JsonHttpResponseHandler responseHandler)
    {
        JSONObject params       = new JSONObject();
        JSONObject limits       = new JSONObject();
        JSONArray properties    = new JSONArray();

        try
        {
            limits.put("start", 0);
            limits.put("end", 5);
            
            //properties.put("title");
            properties.put("playcount");
            properties.put("thumbnail");
            properties.put("rating");
            //properties.put("trailer");
            
            params.put("limits", limits);
            params.put("properties", properties);
        }
        catch(JSONException e)
        {
            Log.v("VideoLibraryClient::getEpisodeDetails", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "GetRecentlyAddedMovies", params, responseHandler);
    }
}
