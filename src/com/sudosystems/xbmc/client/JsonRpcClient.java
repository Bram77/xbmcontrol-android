package com.sudosystems.xbmc.client;

import java.io.UnsupportedEncodingException;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class JsonRpcClient
{
    private static final String BASE_URL            = "http://donda.nl:8080/jsonrpc";
    private static final String AUTH_USERNAME       = "xbmc";
    private static final String AUTH_PASSWORD       = "11983bvoo";
    private static final String CONTENT_TYPE        = "application/json";
    private static final String JSONRPC_VERSION     = "2.0";
    private static final Integer CONNECTION_TIMEOUT = 5000;
    private static AsyncHttpClient iClient           = new AsyncHttpClient();
    private Context iContext;
    
    public JsonRpcClient(Context context)
    {
        iContext            = context;
        String basicAuth    = "";
        iClient.addHeader("Accept", CONTENT_TYPE);
        iClient.addHeader("Content-Type", CONTENT_TYPE);
        iClient.setTimeout(CONNECTION_TIMEOUT);
        iClient.setBasicAuth(AUTH_USERNAME, AUTH_PASSWORD);

        try
        {
            basicAuth = android.util.Base64.encodeToString((AUTH_USERNAME + ":" + AUTH_PASSWORD).getBytes("UTF-8"), android.util.Base64.NO_WRAP);
        }
        catch(UnsupportedEncodingException e2)
        {
            Log.v("JsonRpcClient", e2.getMessage());
            e2.printStackTrace();
        }
        
        iClient.addHeader("Authorization", "Basic " +basicAuth);
    }
    
    public void post(String method, JSONObject params, JsonHttpResponseHandler responseHandler)
    {
        JSONObject rpcParams = buildRpcObject(method, params);
        Log.v("POST params", rpcParams.toString());
        
        StringEntity oRawPostParams;
        
        try
        {
            oRawPostParams = new StringEntity(rpcParams.toString());
        }
        catch(UnsupportedEncodingException e)
        {
            Log.v("JsonRpcClient::post", e.getMessage());
            oRawPostParams = null;
        } 
        
        iClient.cancelRequests(iContext, true);
        iClient.post(iContext, BASE_URL, oRawPostParams, CONTENT_TYPE, responseHandler);
    }
    
    public void post(String method, JsonHttpResponseHandler responseHandler)
    {
        post(method, new JSONObject(), responseHandler);
    }
    
    private JSONObject buildRpcObject(String method, JSONObject params)
    {
        JSONObject rpcParams = new JSONObject();
        
        try
        {
            rpcParams.put("jsonrpc", JSONRPC_VERSION);
            rpcParams.put("method", method);
            
            if(params.length() > 0)
            {
                rpcParams.put("params", params);
            }
            
            rpcParams.put("id", 1);
        }
        catch(JSONException e)
        {
            Log.v("JsonRpcClient::buildRpcObject", e.getMessage());
            e.printStackTrace();
        }
        
        return rpcParams;
    }
}