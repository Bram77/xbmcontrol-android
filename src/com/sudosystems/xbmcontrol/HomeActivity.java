package com.sudosystems.xbmcontrol;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.sudosystems.xbmcontrol.controllers.HomeController;
import com.sudosystems.xbmcontrol.controllers.RecentlyAddedController;
import com.sudosystems.xbmcontrol.services.NowPlayingService;

public class HomeActivity extends Activity 
{
    private HomeController iController;
    private RecentlyAddedController iRecentlyAddedController;
    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        iController                 = new HomeController(this);
        iRecentlyAddedController    = new RecentlyAddedController(this);
        
        if(!iController.isConfigured())
        {
            iController.showInitConfigurationDialog();
            return;
        }
        
        //iRecentlyAddedController.showMovies((LinearLayout) this.findViewById(R.id.recently_added_movies_container));
        //iRecentlyAddedController.showEpisodes((LinearLayout) this.findViewById(R.id.recently_added_episodes_container));
        //iRecentlyAddedController.showAlbums((LinearLayout) this.findViewById(R.id.recently_added_albums_container));
        
        //iController.displayNowPlayingInfo();
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
        return (isVolumeKey)? iController.applyVolume(event.getKeyCode()) : super.dispatchKeyEvent(event) ;
    }
    
    @Override
    public void onBackPressed() 
    {
        super.onBackPressed();
        getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
    }
    
    public void openHomeIntent(View view)
    {
        iController.openHomeIntent();
    }

    public void openAudioIntent(View view)
    {
        iController.openAudioIntent();
    }
    
    public void openVideoIntent(View view)
    {
        iController.openVideoIntent();
    }
    
    public void openPicturesIntent(View view)
    {
        iController.openPicturesIntent();
    }
    
    public void openConfigurationIntent(MenuItem menuItem)
    {
        iController.openConfigurationIntent();
    }
    
    //Playback controls
    public void playbackPrevious(View view)
    {
        iController.Remote.playbackPrevious();
        iController.iVibrator.vibrate(30);
    }
    
    public void playbackPause(View view)
    {
        iController.Remote.playbackPause();
        iController.iVibrator.vibrate(30);
    }
    
    public void playbackStart(View view)
    {
        iController.Remote.playbackStart();
        iController.iVibrator.vibrate(30);
    }
    
    public void playbackStop(View view)
    {
        iController.Remote.playbackStop();
        iController.iVibrator.vibrate(30);
    }
    
    public void playbackNext(View view)
    {
        iController.Remote.playbackNext();
        iController.iVibrator.vibrate(30);
    }
    
    public void openRemoteIntent(View view)
    {
        iController.openRemoteIntent();
    }
}
