package com.sudosystems.xbmc.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class FilesClient extends JsonRpcClient
{
    private static final String NAMESPACE   = "Files.";
    
    public FilesClient(Context context, Configuration configuration)
    {
        super(context, configuration);
    }
    
    public void getSources(String mediaType, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params       = new JSONObject();

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

    public void getDirectory(boolean isLibraryMode, String mediaType, String directory, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params       = new JSONObject();
        String mediaRequested   = (isLibraryMode)? mediaType : "files";

        try
        {
            if(mediaType != null)
            {
                params.put("media", mediaRequested);
            }
            
            params.put("directory", directory);
            
            JSONObject sortParams = new JSONObject();
            sortParams.put("method", "label");
            sortParams.put("order", "ascending");
            
            JSONArray properties = new JSONArray();
            //properties.put("title");
            properties.put("file");
            properties.put("track");
            properties.put("episode");
            //properties.put("thumbnail");
            properties.put("playcount");
            
            params.put("sort", sortParams);
            params.put("properties", properties);
        }
        catch(JSONException e)
        {
            Log.v("FilesClient::getDirectory", e.getMessage());
            e.printStackTrace();
        }

        post(NAMESPACE+ "GetDirectory", params, responseHandler);
    }
    
    public void getDirectory(String mediaType, String directory, JsonHttpResponseHandler responseHandler)
    {
        getDirectory(true, mediaType, directory, responseHandler);
    }
    
    public void getDirectory(String directory, JsonHttpResponseHandler responseHandler)
    {
        getDirectory(null, directory, responseHandler);
    }
}
