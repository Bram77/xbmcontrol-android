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
    private static final int PING_TIMEOUT 	= 2000;
    
    public SystemClient(Context context, Configuration configuration)
    {
        super(context, configuration);
    }
    
    public void introspect(JsonHttpResponseHandler responseHandler)
    {
        post(NAMESPACE+ "Introspect", responseHandler);
    }
    
    public void ping(final PingResponseHandler pingResponseHandler)
    {
    	setConnectionTimeout(PING_TIMEOUT);
    	
        post(NAMESPACE+ "Ping", new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
            	pingResponseHandler.onSuccess();
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
            	pingResponseHandler.onError();
            }
            
            @Override
            public void onFinish()
            {
            	setDefaultConnectionTimeout();
            }
        });
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
