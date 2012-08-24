package com.sudosystems.xbmcontrol;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

import com.sudosystems.xbmcontrol.controllers.HomeController;
import com.sudosystems.xbmcontrol.services.NowPlayingService;
import com.sudosystems.xbmc.client.FilesClient.MediaType;

public class HomeActivity extends Activity 
{
    private HomeController cHome;
    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cHome = new HomeController(this);
        cHome.displayNowPlayingInfo();
        
        startService(new Intent(this, NowPlayingService.class));
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
    
    @Override
    public void onBackPressed() 
    {
        super.onBackPressed();
        getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
    }

    public void openAudioIntent(View view)
    {
        Intent intent = new Intent(this, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", MediaType.AUDIO);
        intent.putExtra("ACTIVITY_TITLE", "Music");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent);
        this.finish();
    }
    
    public void openVideoIntent(View view)
    {
        Intent intent = new Intent(this, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", MediaType.VIDEO);
        intent.putExtra("ACTIVITY_TITLE", "Video");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent);
        this.finish();
    }
    
    public void openPicturesIntent(View view)
    {
        Intent intent = new Intent(this, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", MediaType.PICTURES);
        intent.putExtra("ACTIVITY_TITLE", "Pictures");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent);
        this.finish();
    }
    
    public void openRemoteIntent(View view)
    {
        Intent intent = new Intent(this, RemoteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent);
        this.finish();
    }
}
