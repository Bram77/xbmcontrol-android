package com.sudosystems.xbmc.client;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Context;
import android.util.Log;

public class SystemClient extends JsonRpcClient
{
    private static final String NAMESPACE   = "JSONRPC.";
    private static final String APP_NAME    = "XBMControl";
    
    public SystemClient(Context context)
    {
        super(context);
    }
    
    public void introspect(JsonHttpResponseHandler responseHandler)
    {
        post(NAMESPACE+ "Introspect", responseHandler);
    }
    
    public void ping(JsonHttpResponseHandler responseHandler)
    {
        post(NAMESPACE+ "Ping", responseHandler);
    }
    
    public void notify(String message, JsonHttpResponseHandler responseHandler)
    {
        JSONObject params = new JSONObject();
        
        try
        {
            params.put("sender", APP_NAME);
            params.put("message", message);
        }
        catch(JSONException e)
        {
            Log.v("SystemClient::notify", e.getMessage());
            e.printStackTrace();
        }
        
        post(NAMESPACE+ "NotifyAll", params, responseHandler);
    }
}