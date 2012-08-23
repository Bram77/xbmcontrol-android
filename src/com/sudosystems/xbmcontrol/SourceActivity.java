package com.sudosystems.xbmcontrol;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.xbmc.client.XbmcClient;
import com.sudosystems.xbmc.client.FilesClient.MediaType;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class SourceActivity extends Activity 
{
    private XbmcClient xbmc;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        xbmc = new XbmcClient(this);
        
        setTitle(getResources().getString(R.string.title_global, getIntent().getExtras().getString("ACTIVITY_TITLE"), getIntent().getExtras().getString("ACTIVITY_TITLE").length()));
        showSources();
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

    public void showSources()
    {
        xbmc.Files.getSources(getIntent().getExtras().getString("MEDIA_TYPE"), new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                JSONObject result = response.optJSONObject("result");
                TableLayout sourcesTable = (TableLayout)findViewById(R.id.table_sources);

                if(result != null)
                {
                    JSONArray sources       = result.optJSONArray("sources");
                    TableRow[] sourceRows   = new TableRow[sources.length()];
                    
                    for(int i=0; i < sources.length(); i++)
                    {
                        sourceRows[i] = new TableRow(SourceActivity.this);
                        sourceRows[i].setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        sourceRows[i].setClickable(true);
                        sourceRows[i].setOnClickListener(new OnClickListener() 
                        {
                            public void onClick(View view) 
                            {
                                openSourceDirectory(view);
                            }   
                        });
                        
                        ImageView iconImage     = new ImageView(SourceActivity.this);
                        String currentMediaType = getIntent().getExtras().getString("MEDIA_TYPE");

                        if(currentMediaType.equals(MediaType.AUDIO))
                        {
                            iconImage.setImageResource(R.drawable.folder_audio_64);
                        }
                        else if(currentMediaType.equals(MediaType.VIDEO))
                        {
                            iconImage.setImageResource(R.drawable.folder_video_64);
                        }
                        else if(currentMediaType.equals(MediaType.PICTURES))
                        {
                            iconImage.setImageResource(R.drawable.folder_pictures_64);
                        }
                        else
                        {
                            iconImage.setImageResource(R.drawable.folder_64);
                        }
                        
                        iconImage.setPadding(20, 10, 10, 10);
                        sourceRows[i].addView(iconImage);

                        TextView sourceTitle    = new TextView(SourceActivity.this);
                        sourceTitle.setTextSize(18);
                        sourceTitle.setTypeface(null, Typeface.BOLD);
                        sourceTitle.setPadding(10, 10, 10, 10);
                        sourceTitle.setText(sources.optJSONObject(i).optString("label", "No label specified"));
                        sourceRows[i].addView(sourceTitle);
                        
                        TextView sourcePath     = new TextView(SourceActivity.this);
                        sourcePath.setText(sources.optJSONObject(i).optString("file", ""));
                        sourcePath.setVisibility(View.GONE);
                        sourceRows[i].addView(sourcePath);
                        
                        sourcesTable.addView(sourceRows[i], new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    }
                }
                
                Log.v("RESULT", response.toString());
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
                Log.v("RESULT ERROR", response);
            }
        });
    }
    
    public void openSourceDirectory(View view)
    {
        TableRow row        = (TableRow)view;
        TextView sourcePath = (TextView) row.getChildAt(2);
        Intent intent       = new Intent(SourceActivity.this, SourceDirectoryActivity.class);
        intent.putExtra("MEDIA_TYPE", MediaType.AUDIO);
        intent.putExtra("IS_ROOT", true);
        intent.putExtra("PARENT_DIRECTORY", sourcePath.getText().toString());
        intent.putExtra("ROOT_PATH", sourcePath.getText().toString());
        intent.putExtra("ACTIVITY_TITLE", getIntent().getExtras().getString("ACTIVITY_TITLE"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        SourceActivity.this.startActivity(intent); 
    }
}
