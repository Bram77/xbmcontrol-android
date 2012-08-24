package com.sudosystems.xbmcontrol.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.xbmc.client.XbmcClient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

public class NowPlayingService extends Service
{
    private XbmcClient xbmc;
    public int activePlayerId           = -1;
    private SharedPreferences preferences;
    private Editor preferencesEditor    = null;
    
    @Override
    public void onCreate()
    {
        xbmc                = new XbmcClient(this);
        Context context     = getApplicationContext();
        preferences         = context.getSharedPreferences("NowPlaying", Context.MODE_PRIVATE);
        preferencesEditor   = preferences.edit();
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
        xbmc.Player.getActivePlayers(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                JSONArray result = response.optJSONArray("result");
                
                if(result != null && result.length() > 0)
                {
                    JSONObject activePlayer = result.optJSONObject(0);
                    
                    preferencesEditor.putBoolean("is_playing", true);
                    preferencesEditor.putInt("player_id", activePlayer.optInt("playerid"));
                    preferencesEditor.putString("player_type", activePlayer.optString("type"));
                    preferencesEditor.commit();
                    
                    xbmc.Player.getNowPlayingItem(activePlayer.optInt("playerid"), itemHandler);
                    
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
                    xbmc.AudioLibrary.getSongDetails(nowPlayingItem.optInt("id"), itemDetailsHandler);
                }
                else if(nowPlayingItem.optString("type").equals("movie"))
                {
                    xbmc.VideoLibrary.getMovieDetails(nowPlayingItem.optInt("id"), itemDetailsHandler);
                }
                else if(nowPlayingItem.optString("type").equals("episode"))
                {
                    xbmc.VideoLibrary.getEpisodeDetails(nowPlayingItem.optInt("id"), itemDetailsHandler);
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
        preferencesEditor.putBoolean("is_playing", false);
        preferencesEditor.putInt("player_id", -1);
        preferencesEditor.putString("player_type", "");
        preferencesEditor.putString("media_data_json", "");
        preferencesEditor.commit();
    }
    
    private void storeItemData(JSONObject nowPlayingData)
    {
        preferencesEditor.putString("playing_item_json", nowPlayingData.toString());
        preferencesEditor.commit();
        Log.v("NOW_PLAYING", nowPlayingData.toString());
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
