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
    private static AsyncHttpClient iClient;
    private Context iContext;
    private String baseUrl;
    
    public JsonRpcClient(Context context, Configuration configuration)
    {
        iContext    = context;
        baseUrl     = "http://" +configuration.getHost()+ ":" +configuration.getPort()+ "/jsonrpc";
        iClient     = new AsyncHttpClient();
        iClient.setTimeout(Integer.parseInt(configuration.getConnectionTimeout()));
        iClient.setBasicAuth(configuration.getUsername(), configuration.getPassword());
        iClient.addHeader("Accept", StaticData.CONNTECTION_CONTENT_TYPE);
        iClient.addHeader("Content-Type", StaticData.CONNTECTION_CONTENT_TYPE);
    }
    
    public void cancelRequest()
    {
        iClient.cancelRequests(iContext, true);
    }
    
    public void post(String method, JSONObject params, JsonHttpResponseHandler responseHandler)
    {
        JSONObject rpcParams = buildRpcObject(method, params);
        
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
        
        iClient.post(iContext, baseUrl, oRawPostParams, StaticData.CONNTECTION_CONTENT_TYPE, responseHandler);
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
            rpcParams.put("jsonrpc", StaticData.JSONRPC_VERSION);
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
