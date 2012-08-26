package com.sudosystems.xbmcontrol;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sudosystems.xbmcontrol.controllers.HomeController;
import com.sudosystems.xbmcontrol.services.NowPlayingService;

public class HomeActivity extends Activity 
{
    private HomeController cHome;
    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cHome = new HomeController(this);
        
        if(!cHome.isConfigured())
        {
            cHome.showInitConfigurationDialog();
            return;
        }
        
        //cHome.displayNowPlayingInfo();
        //startService(new Intent(this, NowPlayingService.class));
    }
    
    @Override
    public void onStop() 
    {
        super.onStop();
        //stopService(new Intent(this, NowPlayingService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        boolean isVolumeKey = (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP);
        return (isVolumeKey)? cHome.applyVolume(event.getKeyCode()) : super.dispatchKeyEvent(event) ;
    }
    
    @Override
    public void onBackPressed() 
    {
        super.onBackPressed();
        getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
    }

    public void openAudioIntent(View view)
    {
        cHome.openAudioIntent();
    }
    
    public void openVideoIntent(View view)
    {
        cHome.openVideoIntent();
    }
    
    public void openPicturesIntent(View view)
    {
        cHome.openPicturesIntent();
    }
    
    public void openRemoteIntent(View view)
    {
        cHome.openRemoteIntent();
    }
    
    public void openSettingsIntent(MenuItem menuItem)
    {
        cHome.openSettingsIntent();
    }
}
