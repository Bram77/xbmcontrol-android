package com.sudosystems.xbmcontrol;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import com.sudosystems.xbmc.client.FilesClient;
import com.sudosystems.xbmc.client.FilesClient.MediaType;

public class HomeActivity extends Activity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
