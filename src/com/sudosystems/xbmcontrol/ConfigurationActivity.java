package com.sudosystems.xbmcontrol;

import com.sudosystems.xbmcontrol.controllers.ConfigurationController;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

public class ConfigurationActivity extends Activity 
{
    private ConfigurationController iController;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        
        iController = new ConfigurationController(this);
        iController.loadConnectionData();
    }
    
    @Override
    public void onBackPressed() 
    {
        openHomeIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_configuration, menu);
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
        openHomeIntent();
    }
    
    public void openHomeIntent(View view)
    {
        openHomeIntent();
    }
    
    public void openHomeIntent()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent);
        this.finish();
    }
}
