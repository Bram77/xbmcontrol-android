package com.sudosystems.xbmcontrol;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.xbmc.client.XbmcClient;
import com.sudosystems.xbmc.client.FilesClient.MediaType;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class SourceDirectoryActivity extends Activity 
{
    private XbmcClient xbmc;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_directory);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        xbmc = new XbmcClient(this);
        
        setTitle(getResources().getString(R.string.title_global, getIntent().getExtras().getString("ACTIVITY_TITLE"), getIntent().getExtras().getString("ACTIVITY_TITLE").length()));
        showDirectoryContent();
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
    public void onPause() 
    {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    
    public void showDirectoryContent()
    {
        xbmc.Files.getDirectory(getIntent().getExtras().getString("ROOT_PATH"), new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                JSONObject result        = response.optJSONObject("result");
                TableLayout sourcesTable = (TableLayout)findViewById(R.id.table_source_directory);

                if(result != null)
                {
                    JSONArray files = result.optJSONArray("files");
                    
                    if(files == null || files.length() == 0)
                    {
                        TableRow noResultsRow   = new TableRow(SourceDirectoryActivity.this);
                        TextView noResultsTitle = new TextView(SourceDirectoryActivity.this);
                        noResultsTitle.setText("No results");
                        noResultsRow.addView(noResultsTitle);
                        sourcesTable.addView(noResultsRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                        
                        return;
                    }
                    
                    if(!getIntent().getExtras().getBoolean("IS_ROOT"))
                    {
                        TableRow dirUpRow       = new TableRow(SourceDirectoryActivity.this);
                        dirUpRow.setClickable(true);
                        dirUpRow.setOnClickListener(new OnClickListener() 
                        {
                            public void onClick(View view) 
                            {
                                openSourceDirectory(view);
                            }   
                        });
                        
                        ImageView dirUpImage    = new ImageView(SourceDirectoryActivity.this);
                        dirUpImage.setImageResource(R.drawable.folder_open_64);
                        dirUpImage.setPadding(20, 10, 10, 10);
                        dirUpRow.addView(dirUpImage);

                        TextView dirUpTitle     = new TextView(SourceDirectoryActivity.this);
                        dirUpTitle.setTextSize(18);
                        dirUpTitle.setTypeface(null, Typeface.BOLD);
                        dirUpTitle.setPadding(10, 10, 10, 10);
                        dirUpTitle.setText("..");
                        dirUpRow.addView(dirUpTitle);
                        
                        TextView parentPath = new TextView(SourceDirectoryActivity.this);
                        parentPath.setText(getIntent().getExtras().getString("PARENT_DIRECTORY"));
                        parentPath.setVisibility(View.GONE);
                        dirUpRow.addView(parentPath);

                        sourcesTable.addView(dirUpRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    }
                    
                    TableRow[] sourceRows   = new TableRow[files.length()];
                    
                    for(int i=0; i < files.length(); i++)
                    {
                        sourceRows[i] = new TableRow(SourceDirectoryActivity.this);
                        sourceRows[i].setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        sourceRows[i].setClickable(true);
                        sourceRows[i].setOnClickListener(new OnClickListener() 
                        {
                            public void onClick(View view) 
                            {
                                openSourceDirectory(view);
                            }   
                        });
                        
                        boolean isDirectory     = (files.optJSONObject(i).optString("filetype", "").equals("directory"));
                        ImageView iconImage     = new ImageView(SourceDirectoryActivity.this);
                        String currentMediaType = getIntent().getExtras().getString("MEDIA_TYPE");
                        
                        if(currentMediaType.equals(MediaType.AUDIO))
                        {
                            if(isDirectory)
                            {
                                iconImage.setImageResource(R.drawable.folder_audio_64);
                            }
                            else if(files.optJSONObject(i).optString("type", "").equals("song"))
                            {
                                iconImage.setImageResource(R.drawable.file_audio_64);
                            }
                            else
                            {
                                iconImage.setImageResource(R.drawable.file_64);
                            }
                        }
                        else if(currentMediaType.equals(MediaType.VIDEO))
                        {
                            if(isDirectory)
                            {
                                iconImage.setImageResource(R.drawable.folder_video_64);
                            }
                            else if(files.optJSONObject(i).optString("type", "").equals("movie"))
                            {
                                iconImage.setImageResource(R.drawable.file_video_64);
                            }
                            else
                            {
                                iconImage.setImageResource(R.drawable.file_64);
                            }
                        }
                        else if(currentMediaType.equals(MediaType.PICTURES))
                        {
                            if(isDirectory)
                            {
                                iconImage.setImageResource(R.drawable.folder_pictures_64);
                            }
                            else if(files.optJSONObject(i).optString("type", "").equals("movie"))
                            {
                                iconImage.setImageResource(R.drawable.file_image_64);
                            }
                            else
                            {
                                iconImage.setImageResource(R.drawable.file_64);
                            }
                        }
                        else
                        {
                            if(isDirectory)
                            {
                                iconImage.setImageResource(R.drawable.folder_64);
                            }
                            else
                            {
                                iconImage.setImageResource(R.drawable.file_64);
                            }
                        }

                        iconImage.setPadding(20, 10, 10, 10);
                        sourceRows[i].addView(iconImage);
                        
                        TextView sourceTitle    = new TextView(SourceDirectoryActivity.this);
                        sourceTitle.setTextSize(18);
                        sourceTitle.setTypeface(null, Typeface.BOLD);
                        sourceTitle.setPadding(10, 10, 10, 10);
                        sourceTitle.setText(files.optJSONObject(i).optString("label", "No label specified"));
                        sourceRows[i].addView(sourceTitle);
                        
                        TextView sourcePath     = new TextView(SourceDirectoryActivity.this);
                        sourcePath.setText(files.optJSONObject(i).optString("file", ""));
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
        Intent intent       = new Intent(SourceDirectoryActivity.this, SourceDirectoryActivity.class);
        intent.putExtra("MEDIA_TYPE", MediaType.AUDIO);
        intent.putExtra("IS_ROOT", false);
        intent.putExtra("PARENT_DIRECTORY", getIntent().getExtras().getString("ROOT_PATH"));
        intent.putExtra("ROOT_PATH", sourcePath.getText().toString());
        intent.putExtra("ACTIVITY_TITLE", getIntent().getExtras().getString("ACTIVITY_TITLE"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        SourceDirectoryActivity.this.startActivity(intent); 
    }
}
