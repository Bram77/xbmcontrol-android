package com.sudosystems.xbmcontrol.controllers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sudosystems.xbmc.client.FilesClient.MediaType;
import com.sudosystems.xbmcontrol.RemoteActivity;
import com.sudosystems.xbmcontrol.SourceActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

public class HomeController extends GlobalController
{
    private SharedPreferences nowPlayingStorage;
    private Context applicationContext;
    private Context iContext;
    private Activity iActivity;
    private ScheduledExecutorService scheduleTaskExecutor;
    
    public HomeController(Context context)
    {
        super(context);
        iContext            = context;
        applicationContext  = context.getApplicationContext();
        nowPlayingStorage   = applicationContext.getSharedPreferences("NowPlaying", Context.MODE_PRIVATE);
        iActivity           = (Activity) context;
    }
    
    public void displayNowPlayingInfo()
    {
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() 
        {
            public void run() 
            {
                if(nowPlayingStorage.getBoolean("is_playing", false))
                {
                    Log.v("NOW_PLAYING", nowPlayingStorage.getString("media_data_json", "nothing"));
                }
            }
        }, 0, 15, TimeUnit.SECONDS);
    }
    
    public void cancelDisplayNowPlayingInfo()
    {
        scheduleTaskExecutor.shutdown();
    }
    
    public void openAudioIntent(View view)
    {
        Intent intent = new Intent(iContext, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", MediaType.AUDIO);
        intent.putExtra("ACTIVITY_TITLE", "Music");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        iActivity.startActivity(intent);
        iActivity.finish();
    }
    
    public void openVideoIntent(View view)
    {
        Intent intent = new Intent(iContext, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", MediaType.VIDEO);
        intent.putExtra("ACTIVITY_TITLE", "Video");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        iActivity.startActivity(intent);
        iActivity.finish();
    }
    
    public void openPicturesIntent(View view)
    {
        Intent intent = new Intent(iContext, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", MediaType.PICTURES);
        intent.putExtra("ACTIVITY_TITLE", "Pictures");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        iActivity.startActivity(intent);
        iActivity.finish();
    }
    
    public void openRemoteIntent(View view)
    {
        Intent intent = new Intent(iContext, RemoteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        iActivity.startActivity(intent);
        iActivity.finish();
    }
}
