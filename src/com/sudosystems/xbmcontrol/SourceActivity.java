package com.sudosystems.xbmcontrol;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.xbmc.client.XbmcClient;
import com.sudosystems.xbmc.client.FilesClient.MediaType;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
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
    private ProgressDialog loadingDialog;
    
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
    
    @Override
    public void onBackPressed() 
    {
        Intent intent = new Intent(SourceActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        SourceActivity.this.startActivity(intent);
        SourceActivity.this.finish();
    }

    public void showSources()
    {
        xbmc.Files.getSources(getIntent().getExtras().getString("MEDIA_TYPE"), new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                loadingDialog = ProgressDialog.show(SourceActivity.this, "", "Loading data...", true);
            }
            
            @Override
            public void onSuccess(JSONObject response)
            {
                JSONObject result           = response.optJSONObject("result");
                TableLayout sourcesTable    = (TableLayout)findViewById(R.id.table_sources);

                if(result != null)
                {
                    JSONArray sources       = result.optJSONArray("sources");
                    
                    for(int i=0; i < sources.length(); i++)
                    {
                        String currentMediaType = getIntent().getExtras().getString("MEDIA_TYPE");
                        TableRow sourceRow      = (TableRow)getLayoutInflater().inflate(R.layout.directory_template, null);
                        ImageView rowIcon       = (ImageView) sourceRow.getChildAt(0);

                        if(currentMediaType.equals(MediaType.AUDIO))
                        {
                            rowIcon.setImageResource(R.drawable.folder_audio_64);
                        }
                        else if(currentMediaType.equals(MediaType.VIDEO))
                        {
                            rowIcon.setImageResource(R.drawable.folder_video_64);
                        }
                        else if(currentMediaType.equals(MediaType.PICTURES))
                        {
                            rowIcon.setImageResource(R.drawable.folder_pictures_64);
                        }
                        
                        sourceRow.setOnClickListener(new OnClickListener() 
                        {
                            public void onClick(View view) 
                            {
                                showDirectoryContent(view);
                            }   
                        });

                        TextView sourceTitle = (TextView) sourceRow.getChildAt(1);
                        sourceTitle.setText(sources.optJSONObject(i).optString("label", "No label specified"));
                        
                        TextView sourcePath = (TextView) sourceRow.getChildAt(2);
                        sourcePath.setText(sources.optJSONObject(i).optString("file", ""));
                        
                        sourcesTable.addView(sourceRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    }
                }
                
                Log.v("RESULT", response.toString());
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
                Log.v("RESULT ERROR", response);
            }
            
            @Override
            public void onFinish()
            {
                loadingDialog.cancel();
            }
        });
    }
    
    public void showDirectoryContent(View view)
    {
        TableRow row        = (TableRow)view;
        TextView sourcePath = (TextView) row.getChildAt(2);
        Intent intent       = new Intent(SourceActivity.this, SourceDirectoryActivity.class);
        intent.putExtra("MEDIA_TYPE", getIntent().getExtras().getString("MEDIA_TYPE"));
        intent.putExtra("ROOT_PATH", sourcePath.getText().toString());
        intent.putExtra("CURRENT_PATH", sourcePath.getText().toString());
        intent.putExtra("ACTIVITY_TITLE", getIntent().getExtras().getString("ACTIVITY_TITLE"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        SourceActivity.this.startActivity(intent); 
    }
}
