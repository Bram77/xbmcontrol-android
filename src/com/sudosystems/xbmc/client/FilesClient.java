package com.sudosystems.xbmc.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class FilesClient  extends JsonRpcClient
{
    private static final String NAMESPACE   = "Files.";
    
    public FilesClient(Context context)
    {
        super(context);
    }
    
    public void getSources(String mediaType, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params = new JSONObject();

        try
        {
            params.put("media", mediaType);
        }
        catch(JSONException e)
        {
            Log.v("FilesClient::getSources", e.getMessage());
            e.printStackTrace();
        }

        post(NAMESPACE+ "GetSources", params, responseHandler);
    }
    
    public void getDirectory(String mediaType, String directory, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params = new JSONObject();

        try
        {
            JSONObject sortParams = new JSONObject();
            
            if(mediaType != null)
            {
                params.put("media", mediaType);
            }
            
            params.put("directory", directory);
            sortParams.put("order", "ascending");
            sortParams.put("method", "file");
            params.put("sort", sortParams);
        }
        catch(JSONException e)
        {
            Log.v("FilesClient::getDirectory", e.getMessage());
            e.printStackTrace();
        }

        post(NAMESPACE+ "GetDirectory", params, responseHandler);
    }
    
    public void getDirectory(String directory, JsonHttpResponseHandler responseHandler)
    {
        getDirectory(null, directory, responseHandler);
    }

    
    public final class MediaType
    {
        public final static String VIDEO    = "video";
        public final static String AUDIO    = "music";
        public final static String PICTURES = "pictures";
    }
}
