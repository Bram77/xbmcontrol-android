package com.sudosystems.xbmcontrol;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

import com.sudosystems.xbmc.client.XbmcClient;
import com.sudosystems.xbmc.client.FilesClient.MediaType;

public class HomeActivity extends Activity 
{
    private SharedPreferences nowPlayingData;
    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this, NowPlayingService.class));
        
        Context context     = getApplicationContext();
        nowPlayingData      = context.getSharedPreferences("NowPlaying", Context.MODE_PRIVATE);

        ScheduledExecutorService scheduleTaskExecutor   = Executors.newScheduledThreadPool(5);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() 
        {
            public void run() 
            {
                if(nowPlayingData.getBoolean("is_playing", false))
                {
                    Log.v("NOW_PLAYING", nowPlayingData.getString("media_data_json", "nothing"));
                }
            }
        }, 0, 15, TimeUnit.SECONDS);

    }
    
    @Override
    public void onStop() 
    {
        super.onStop();
        stopService(new Intent(this, NowPlayingService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }

    public void openAudioIntent(View view)
    {
        Intent intent = new Intent(this, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", MediaType.AUDIO);
        intent.putExtra("ACTIVITY_TITLE", "Music");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent); 
    }
    
    public void openVideoIntent(View view)
    {
        Intent intent = new Intent(this, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", MediaType.VIDEO);
        intent.putExtra("ACTIVITY_TITLE", "Video");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent);
    }
    
    public void openPicturesIntent(View view)
    {
        Intent intent = new Intent(this, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", MediaType.PICTURES);
        intent.putExtra("ACTIVITY_TITLE", "Pictures");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent);
    }
    
    public void openRemoteIntent(View view)
    {
        Intent intent = new Intent(this, RemoteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent);
    }
}
