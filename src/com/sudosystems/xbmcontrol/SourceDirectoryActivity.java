package com.sudosystems.xbmcontrol;

import com.sudosystems.xbmcontrol.controllers.*;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class SourceDirectoryActivity extends Activity 
{
    private SourceDirectoryController iController;
    private Bundle iActivityParams;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_directory);
 
        iActivityParams     = getIntent().getExtras();
        iController         = new SourceDirectoryController(this);
        iController.displayDirectoryContent();
        //iController.addNavigationToLayout();
        iController.highlightNavigationButton();

        setTitle(getResources().getString(R.string.title_empty, iActivityParams.getString("ACTIVITY_TITLE"), iActivityParams.getString("ACTIVITY_TITLE").length()));
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View sourceRow, ContextMenuInfo menuInfo) 
    {
      super.onCreateContextMenu(menu, sourceRow, menuInfo);
      
      iController.setContextMenuRow((TableRow) sourceRow);
      TextView sourceTitle  = (TextView) ((ViewGroup) sourceRow).getChildAt(1);
      menu.setHeaderTitle("'" +sourceTitle.getText()+ "'");
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.source_directory_context_menu, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_source_directory, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) 
    {
        iController.prepareMenu(menu);
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
    public void onPause() 
    {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    
    @Override
    public void onBackPressed() 
    {
        iController.showDirectoryUpIntent();
    }
    
    public void playDirectory(MenuItem item)
    {
        iController.playDirectory(item, null, 0);
    }

    public void enqueDirectory(MenuItem menuItem)
    {
        
    }
    
    public void openConfigurationIntent(MenuItem menuItem)
    {
        iController.openConfigurationIntent();
    }
    
    public void hideWatchedVideos(MenuItem item)
    {
        iController.Configuration.setHideWatchedEnabled(true);
        iController.openSourceDirectoryIntent();
    }
    
    public void showAllVideos(MenuItem item)
    {
        iController.Configuration.setHideWatchedEnabled(false);
        iController.openSourceDirectoryIntent();
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
