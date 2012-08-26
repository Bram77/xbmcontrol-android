package com.sudosystems.xbmcontrol.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.xbmc.client.XbmcClient;
import com.sudosystems.xbmcontrol.controllers.StaticData;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

public class NowPlayingService extends Service
{
    private XbmcClient iXbmc            = null;
    public int activePlayerId           = -1;
    private Editor nowPlayingEditor     = null;
    private Context applicationContext;
    
    @Override
    public void onCreate()
    {
       
        applicationContext                      = getApplicationContext();
        SharedPreferences nowPlayingStorage     = applicationContext.getSharedPreferences(StaticData.STORAGE_NOWPLAYING, Context.MODE_PRIVATE);
        SharedPreferences configurationStorage  = applicationContext.getSharedPreferences(StaticData.STORAGE_CONFIGURATION, Context.MODE_PRIVATE);
        nowPlayingEditor                        = nowPlayingStorage.edit();
        
        try
        {
            JSONObject connectionConfigurationData  = new JSONObject(configurationStorage.getString(StaticData.STORAGE_CONFIGURATION_CONNECTION, "{}"));
            iXbmc                                   = new XbmcClient(this, connectionConfigurationData);
        }
        catch(JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
    
    @Override
    public void onStart(Intent intent, int startId) 
    {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() 
        {
            public void run() 
            {
                setNowPlayingData();
            }
        }, 0, 15, TimeUnit.SECONDS);
    }
    
    private void setNowPlayingData()
    {
        iXbmc.Player.getActivePlayers(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                JSONArray result = response.optJSONArray("result");
                
                if(result != null && result.length() > 0)
                {
                    JSONObject activePlayer = result.optJSONObject(0);
                    
                    nowPlayingEditor.putBoolean("is_playing", true);
                    nowPlayingEditor.putInt("player_id", activePlayer.optInt("playerid"));
                    nowPlayingEditor.putString("player_type", activePlayer.optString("type"));
                    nowPlayingEditor.commit();
                    
                    iXbmc.Player.getNowPlayingItem(activePlayer.optInt("playerid"), itemHandler);
                    
                    return;
                }
                
                resetData();
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
                resetData();
            }
        });
    }

    private JsonHttpResponseHandler itemHandler = new JsonHttpResponseHandler()
    {
        @Override
        public void onSuccess(JSONObject nowPlayingData)
        {
            JSONObject mediaData = nowPlayingData.optJSONObject("result");
            
            if(mediaData != null)
            {
                JSONObject nowPlayingItem = mediaData.optJSONObject("item");
                storeItemData(nowPlayingItem);
                
                if(nowPlayingItem.optString("type").equals("song"))
                {
                    iXbmc.AudioLibrary.getSongDetails(nowPlayingItem.optInt("id"), itemDetailsHandler);
                }
                else if(nowPlayingItem.optString("type").equals("movie"))
                {
                    iXbmc.VideoLibrary.getMovieDetails(nowPlayingItem.optInt("id"), itemDetailsHandler);
                }
                else if(nowPlayingItem.optString("type").equals("episode"))
                {
                    iXbmc.VideoLibrary.getEpisodeDetails(nowPlayingItem.optInt("id"), itemDetailsHandler);
                }
                
                return;
            }
        }
        
        @Override
        public void onFailure(Throwable e, String response) 
        {
            Log.v("NowPlayingService::itemHandler", e.getMessage());
        }
    };
    
    private JsonHttpResponseHandler itemDetailsHandler = new JsonHttpResponseHandler()
    {
        @Override
        public void onSuccess(JSONObject response)
        {
            Log.v("LIBRARY_DATA", response.toString());
        }
        
        @Override
        public void onFailure(Throwable e, String response) 
        {
            Log.v("NowPlayingService::itemDetailsHandler", e.getMessage());
        }
    };
    
    private void resetData()
    {
        nowPlayingEditor.putBoolean("is_playing", false);
        nowPlayingEditor.putInt("player_id", -1);
        nowPlayingEditor.putString("player_type", "");
        nowPlayingEditor.putString("media_data_json", "");
        nowPlayingEditor.commit();
    }
    
    private void storeItemData(JSONObject nowPlayingData)
    {
        nowPlayingEditor.putString("playing_item_json", nowPlayingData.toString());
        nowPlayingEditor.commit();
        Log.v("NOW_PLAYING", nowPlayingData.toString());
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
