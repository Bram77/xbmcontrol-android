package com.sudosystems.xbmcontrol;

import com.sudosystems.xbmcontrol.R.id;
import com.sudosystems.xbmcontrol.controllers.HomeController;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.support.v4.app.NavUtils;

public class RemoteActivity extends Activity
{
    private HomeController iController;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        
        iController                     = new HomeController(this);
        String title                    = getResources().getString(R.string.title_activity_remote);
        
        //Hide openRemoteIntent button
        ImageButton openRemoteButton    = (ImageButton) this.findViewById(id.open_remote);
        openRemoteButton.setVisibility(View.GONE);
        
        setTitle(getResources().getString(R.string.title_global, title, title.length()));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_remote, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        
        return super.onOptionsItemSelected(item);
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
        iController.openHomeIntent();
    }
    
    @Override
    public void onPause() 
    {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void remoteUp(View view)
    {
        iController.Remote.up();
        iController.iVibrator.vibrate(30);
    }
    
    public void remoteDown(View view)
    {
        iController.Remote.down();
        iController.iVibrator.vibrate(30);
    }
    
    public void remoteLeft(View view)
    {
        iController.Remote.left();
        iController.iVibrator.vibrate(30);
    }
    
    public void remoteRight(View view)
    {
        iController.Remote.right();
        iController.iVibrator.vibrate(30);
    }
    
    public void remoteBack(View view)
    {
        iController.Remote.back();
        iController.iVibrator.vibrate(30);
    }
    
    public void remoteHome(View view)
    {
        iController.Remote.home();
        iController.iVibrator.vibrate(30);
    }
    
    public void remoteContextMenu(View view)
    {
        iController.Remote.contextMenu();
        iController.iVibrator.vibrate(30);
    }
    
    public void remoteMenu(View view)
    {
        iController.Remote.menu();
        iController.iVibrator.vibrate(30);
    }
    
    public void remoteInfo(View view)
    {
        iController.Remote.info();
        iController.iVibrator.vibrate(30);
    }
    
    public void remoteSelect(View view)
    {
    	iController.Remote.select();
        iController.iVibrator.vibrate(30);
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
    
    public void openRemoteIntent(View view)
    {
        iController.openRemoteIntent();
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
}
