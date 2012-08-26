package com.sudosystems.xbmcontrol;

import com.sudosystems.xbmcontrol.controllers.ConfigurationController;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

public class ConfigurationActivity extends Activity 
{
    
    private ConfigurationController cConfiguration;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        
        cConfiguration = new ConfigurationController(this);
        cConfiguration.loadConnectionData();
    }
    
    @Override
    public void onBackPressed() 
    {
        cConfiguration.openHomeIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
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

    public void storeConfiguration(View view)
    {
        if(!cConfiguration.storeConnectionData())
        {
            cConfiguration.notify("Configuration could not be saved");
        }
        
        openHomeIntent(null);
    }
    
    public void openHomeIntent(View view)
    {
        cConfiguration.openHomeIntent();
    }
}
