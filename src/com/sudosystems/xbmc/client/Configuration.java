package com.sudosystems.xbmc.client;

import org.json.JSONObject;

public class Configuration
{
    private JSONObject configuration;
    
    public Configuration(JSONObject settingsStorage)
    {
        configuration = settingsStorage;
    }
    
    public void update(JSONObject settingsStorage)
    {
        configuration = settingsStorage;
    }
    
    public String getHost()
    {
        return configuration.optString("host_address", "");
    }
    
    public String getPort()
    {
        return configuration.optString("host_port", "8080");
    }
    
    public String getConnectionTimeout()
    {
        return configuration.optString("connection_timeout", "6000");
    }
    
    public String getUsername()
    {
        return configuration.optString("username", "");
    }
    
    public String getPassword()
    {
        return configuration.optString("password", "");
    }
}
