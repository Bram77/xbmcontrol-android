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
        JSONArray properties    = new JSONArray();
        String mediaRequested   = (isLibraryMode)? mediaType : "files";
        String sortMethod       = "label";

        try
        {
            if(mediaType != null)
            {
                params.put("media", mediaRequested);
            }

            if(mediaType.equals(StaticData.MEDIA_TYPE_VIDEO))
            {
                properties.put("episode");
                //sortMethod = "episode";
            }
            else if(mediaType.equals(StaticData.MEDIA_TYPE_AUDIO))
            {
                properties.put("track");
                //sortMethod = "track";
            }
            
            params.put("directory", directory);
            
            JSONObject sortParams = new JSONObject();
            sortParams.put("method", sortMethod);
            sortParams.put("order", "ascending");

            properties.put("file");
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
