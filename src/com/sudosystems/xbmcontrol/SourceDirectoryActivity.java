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
    private SourceDirectoryController cSourceDirectory;
    private Bundle activityParams;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_directory);
 
        activityParams          = getIntent().getExtras();
        cSourceDirectory        = new SourceDirectoryController(this, activityParams);
        cSourceDirectory.displayDirectoryContent();
        
        String activityTitle = getIntent().getExtras().getString("MEDIA_TYPE")+ " / " +getIntent().getExtras().getString("ACTIVITY_TITLE");
        setTitle(getResources().getString(R.string.title_empty, activityTitle, activityTitle.length()));
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View sourceRow, ContextMenuInfo menuInfo) 
    {
      super.onCreateContextMenu(menu, sourceRow, menuInfo);
      
      cSourceDirectory.setContextMenuRow((TableRow) sourceRow);
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
        return (isVolumeKey)? cSourceDirectory.applyVolume(event.getKeyCode()) : super.dispatchKeyEvent(event) ;
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
        cSourceDirectory.showDirectoryUpIntent();
    }
    
    public void playDirectory(MenuItem item)
    {
        cSourceDirectory.playDirectory(item, null, 0);
    }

    public void enqueDirectory(MenuItem menuItem)
    {
        
    }
}
