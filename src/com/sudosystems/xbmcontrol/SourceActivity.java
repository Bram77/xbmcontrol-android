package com.sudosystems.xbmcontrol;

import com.sudosystems.xbmcontrol.controllers.SourceController;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class SourceActivity extends Activity 
{
    private SourceController cSource;
    private Bundle activityParams;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        activityParams  = getIntent().getExtras();
        cSource         = new SourceController(this, activityParams);
        cSource.displaySources();
        
        setTitle(getResources().getString(R.string.title_global, activityParams.getString("ACTIVITY_TITLE"), activityParams.getString("ACTIVITY_TITLE").length()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_source, menu);
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
    
    @Override
    public void onBackPressed() 
    {
        Intent intent = new Intent(SourceActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        SourceActivity.this.startActivity(intent);
        SourceActivity.this.finish();
    }
    
    public void displayDirectoryContent(View view)
    {
        TableRow row        = (TableRow)view;
        TextView sourcePath = (TextView) row.getChildAt(2);
        Intent intent       = new Intent(this, SourceDirectoryActivity.class);
        intent.putExtra("MEDIA_TYPE", activityParams.getString("MEDIA_TYPE"));
        intent.putExtra("ROOT_PATH", sourcePath.getText().toString());
        intent.putExtra("CURRENT_PATH", sourcePath.getText().toString());
        intent.putExtra("ACTIVITY_TITLE", activityParams.getString("ACTIVITY_TITLE"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent);
        this.finish();
    }
}
