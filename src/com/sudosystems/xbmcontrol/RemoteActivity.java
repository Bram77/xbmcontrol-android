package com.sudosystems.xbmcontrol;

import com.sudosystems.xbmc.client.RemoteClient;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

public class RemoteActivity extends Activity
{
    private RemoteClient remote;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        remote = new RemoteClient();
        
        String title = getResources().getString(R.string.title_activity_remote);
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
    public void onPause() 
    {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void remoteUp(View view)
    {
        remote.up();
    }
    
    public void remoteDown(View view)
    {
        remote.down();
    }
    
    public void remoteLeft(View view)
    {
        remote.left();
    }
    
    public void remoteRight(View view)
    {
        remote.right();
    }
    
    public void remoteBack(View view)
    {
        remote.back();
    }
    
    public void remoteHome(View view)
    {
        remote.home();
    }
    
    public void remoteContextMenu(View view)
    {
        remote.contextMenu();
    }
    
    public void remoteInfo(View view)
    {
        remote.info();
    }
    
    public void remoteSelect(View view)
    {
        remote.select();
    }
}
