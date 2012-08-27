package com.sudosystems.xbmcontrol.controllers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

public class HomeController extends GlobalController
{
    private SharedPreferences nowPlayingStorage;
    private ScheduledExecutorService scheduleTaskExecutor;
    private ConfigurationController iConfiguration;
    
    public HomeController(Context context)
    {
        super(context);
        iConfiguration      = new ConfigurationController(context);
        nowPlayingStorage   = iContext.getSharedPreferences(StaticData.STORAGE_NOWPLAYING, Context.MODE_PRIVATE);
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
    
    public void showInitConfigurationDialog()
    {
        new AlertDialog.Builder(iContext)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Configuration required")
            .setMessage("Please configure your XBMC connection")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    openConfigurationIntent();    
                }
    
            })
            .setNegativeButton("Close", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    iActivity.finish();    
                }
    
            })
            .show();
    }
    
    public boolean isConfigured()
    {
        return (!iConfiguration.getConnectionValue("host_address").equals(""));
    }
}
