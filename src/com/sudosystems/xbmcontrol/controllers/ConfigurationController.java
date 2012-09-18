package com.sudosystems.xbmcontrol.controllers;

import org.json.JSONException;
import org.json.JSONObject;

import com.sudosystems.xbmcontrol.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.EditText;

public class ConfigurationController
{
    private SharedPreferences iStorage;
    private EditText iHost;
    private EditText iHostPort;
    private EditText iConnectionTimeout;
    private EditText iUsername;
    private EditText iPassword;
    
    public ConfigurationController(Context context)
    {
        Activity iActivity  = (Activity) context;
        iStorage            = context.getApplicationContext().getSharedPreferences(StaticData.STORAGE_CONFIGURATION, Context.MODE_PRIVATE);
        iHost               = (EditText) iActivity.findViewById(R.id.settings_host_address);
        iHostPort           = (EditText) iActivity.findViewById(R.id.settings_host_port);
        iConnectionTimeout  = (EditText) iActivity.findViewById(R.id.settings_connection_timeout);
        iUsername           = (EditText) iActivity.findViewById(R.id.settings_username);
        iPassword           = (EditText) iActivity.findViewById(R.id.settings_password);
    }
    
    public boolean storeConnectionData()
    {
        JSONObject connectionParams = new JSONObject();

        try
        {
            connectionParams.put("host_address", iHost.getText().toString());
            connectionParams.put("host_port", iHostPort.getText().toString());
            connectionParams.put("connection_timeout", iConnectionTimeout.getText().toString());
            connectionParams.put("username", iUsername.getText().toString());
            connectionParams.put("password", iPassword.getText().toString());
        }
        catch(JSONException e)
        {
            Log.v("ConfigurationController::store", "Could not save configuration: " +e.getMessage());
            e.printStackTrace();
            
            return false;
        }
        
        Log.e("AFTER SAVE", iStorage.getString(StaticData.STORAGE_CONFIGURATION_CONNECTION, "empty"));
        
        Editor configurationEditor = iStorage.edit();
        configurationEditor.putString(StaticData.STORAGE_CONFIGURATION_CONNECTION, connectionParams.toString());
        configurationEditor.commit();

        return true;
    }

    public void loadConnectionData()
    {
        JSONObject connectionParams = getConnectionData();
        
        iHost.setText(connectionParams.optString("host_address", ""));
        iHostPort.setText(connectionParams.optString("host_port", "8080"));
        iConnectionTimeout.setText(connectionParams.optString("connection_timeout", "6000"));
        iUsername.setText(connectionParams.optString("username", ""));
        iPassword.setText(connectionParams.optString("password", ""));
    }
    
    public JSONObject getConnectionData()
    {
        try
        {
            return new JSONObject(iStorage.getString(StaticData.STORAGE_CONFIGURATION_CONNECTION, "{}"));
        }
        catch(JSONException e)
        {
            Log.e("ConfigurationController::get", "Could not load configuration data: " +e.getMessage());
            e.printStackTrace();
            
            return null;
        }
    }
    
    public String getConnectionValue(String key)
    {
        JSONObject data = getConnectionData();
        
        return data.optString(key, "");
    }
    
    public void setHideWatchedEnabled(boolean enable)
    {
        Editor configurationEditor = iStorage.edit();
        configurationEditor.putBoolean(StaticData.STORAGE_CONFIGURATION_HIDE_WATCHED, enable);
        configurationEditor.commit();
    }
    
    public void setHideWatchedEnabled()
    {
        setHideWatchedEnabled(true);
    }
    
    public boolean isHideWatchedEnabled()
    {
        return iStorage.getBoolean(StaticData.STORAGE_CONFIGURATION_HIDE_WATCHED, false);
    }
}
