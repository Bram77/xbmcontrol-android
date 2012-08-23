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
    private TableRow ioDirectoryUpRow = null;
    private ProgressDialog loadingDialog;

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
    
    @Override
    public void onBackPressed() 
    {
        if(ioDirectoryUpRow != null)
        {
            ioDirectoryUpRow.performClick();
        }
    }
    
    private void addDirectoryUpRow(TableLayout sourcesTable)
    {
        ioDirectoryUpRow = (TableRow) getLayoutInflater().inflate(R.layout.directory_template, null);
        ioDirectoryUpRow.setOnClickListener(new OnClickListener() 
        {
            public void onClick(View view) 
            {
                showDirectoryContent(view);
            }   
        });
        
        ImageView rowIcon = (ImageView) ioDirectoryUpRow.getChildAt(0);
        rowIcon.setImageResource(R.drawable.folder_open_64);
        
        TextView sourceTitle = (TextView) ioDirectoryUpRow.getChildAt(1);
        sourceTitle.setText("..");
        
        TextView sourcePath = (TextView) ioDirectoryUpRow.getChildAt(2);
        sourcePath.setText(xbmc.Helper.getOneDirectoryUp(getIntent().getExtras().getString("CURRENT_PATH")));
        
        sourcesTable.addView(ioDirectoryUpRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
    
    private void setMediaIcon(boolean isDirectory, String mediaType, ImageView rowIcon)
    {
        String currentMediaType = getIntent().getExtras().getString("MEDIA_TYPE");
        
        if(currentMediaType.equals(MediaType.AUDIO))
        {
            if(isDirectory)
            {
                rowIcon.setImageResource(R.drawable.folder_audio_64);
            }
            else if(mediaType.equals("song"))
            {
                rowIcon.setImageResource(R.drawable.file_audio_64);
            }
        }
        else if(currentMediaType.equals(MediaType.VIDEO))
        {
            if(isDirectory)
            {
                rowIcon.setImageResource(R.drawable.folder_video_64);
            }
            else if(mediaType.equals("episode"))
            {
                rowIcon.setImageResource(R.drawable.file_video_64);
            }
            else if(mediaType.equals("movie"))
            {
                rowIcon.setImageResource(R.drawable.file_video_64);
            }
        }
        else if(currentMediaType.equals(MediaType.PICTURES))
        {
            if(isDirectory)
            {
                rowIcon.setImageResource(R.drawable.folder_pictures_64);
            }
            else if(currentMediaType.equals("picture"))
            {
                rowIcon.setImageResource(R.drawable.file_image_64);
            }
        }
    }
    
    public void showDirectoryContent()
    {
        xbmc.Files.getDirectory(getIntent().getExtras().getString("MEDIA_TYPE"), getIntent().getExtras().getString("CURRENT_PATH"), new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                loadingDialog = ProgressDialog.show(SourceDirectoryActivity.this, "", "Loading data...", true);
            }
            
            @Override
            public void onSuccess(JSONObject response)
            {
                JSONObject loError       = response.optJSONObject("error");
                JSONObject result        = response.optJSONObject("result");
                TableLayout sourcesTable = (TableLayout)findViewById(R.id.table_source_directory);
                
                if(loError != null)
                {
                    Toast.makeText(SourceDirectoryActivity.this, "ERROR: Source not accessable", Toast.LENGTH_SHORT).show();
                    openSourceDirectory(xbmc.Helper.getOneDirectoryUp(getIntent().getExtras().getString("CURRENT_PATH")));
                    
                    return;
                }

                if(result != null)
                {
                    JSONArray files = result.optJSONArray("files");
                    
                    if(files == null || files.length() == 0)
                    {
                        Toast.makeText(SourceDirectoryActivity.this, "ERROR: No " +getIntent().getExtras().getString("MEDIA_TYPE")+ " files found", Toast.LENGTH_SHORT).show();
                        openSourceDirectory(xbmc.Helper.getOneDirectoryUp(getIntent().getExtras().getString("CURRENT_PATH")));
                        
                        return;
                    }
                    
                    addDirectoryUpRow(sourcesTable);

                    for(int i=0; i < files.length(); i++)
                    {
                        final boolean isDirectory = (files.optJSONObject(i).optString("filetype", "").equals("directory"));
                        final String fileType       = files.optJSONObject(i).optString("type", "");
                        TableRow sourceRow          = (isDirectory)? (TableRow) getLayoutInflater().inflate(R.layout.directory_template, null) : (TableRow) getLayoutInflater().inflate(R.layout.file_template, null);
                        ImageView rowIcon           = (ImageView) sourceRow.getChildAt(0);
                        
                        setMediaIcon(isDirectory, fileType, rowIcon);
                        
                        sourceRow.setOnClickListener(new OnClickListener() 
                        {
                            public void onClick(View view) 
                            {
                                if(isDirectory)
                                {
                                    showDirectoryContent(view);
                                }
                                else if(fileType == "song")
                                {
                                    
                                }
                            }   
                        });
                        
                        TextView sourceTitle = (TextView) sourceRow.getChildAt(1);
                        sourceTitle.setText(files.optJSONObject(i).optString("label", "No label specified"));
                        
                        TextView sourcePath = (TextView) sourceRow.getChildAt(2);
                        sourcePath.setText(files.optJSONObject(i).optString("file", ""));
                        
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
    
    public void openSource()
    {
        Intent intent = new Intent(SourceDirectoryActivity.this, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", getIntent().getExtras().getString("MEDIA_TYPE"));
        intent.putExtra("ACTIVITY_TITLE", getIntent().getExtras().getString("ACTIVITY_TITLE"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        SourceDirectoryActivity.this.startActivity(intent);
        SourceDirectoryActivity.this.finish();
    }
    
    public void showDirectoryContent(View view, String psSourcePath)
    {
        String lsSourcePath = "";
        
        if(view != null)
        {
            TableRow row            = (TableRow)view;
            TextView loSourcePath   = (TextView) row.getChildAt(2);
            lsSourcePath            = loSourcePath.getText().toString();
        }
        else if(psSourcePath != null)
        {
            lsSourcePath = psSourcePath;
        }
        else
        {
            return;
        }

        Log.v("ROOT", getIntent().getExtras().getString("ROOT_PATH"));
        Log.v("TARGET", lsSourcePath);
        
        if(lsSourcePath.length() < getIntent().getExtras().getString("ROOT_PATH").length())
        {
            openSource();
            return;
        }
        
        Intent intent = new Intent(SourceDirectoryActivity.this, SourceDirectoryActivity.class);
        intent.putExtra("MEDIA_TYPE", getIntent().getExtras().getString("MEDIA_TYPE"));
        intent.putExtra("IS_ROOT", false);
        intent.putExtra("ROOT_PATH", getIntent().getExtras().getString("ROOT_PATH"));
        intent.putExtra("CURRENT_PATH", lsSourcePath);
        intent.putExtra("ACTIVITY_TITLE", getIntent().getExtras().getString("ACTIVITY_TITLE"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        SourceDirectoryActivity.this.startActivity(intent); 
    }
    
    public void showDirectoryContent(View view)
    {
        showDirectoryContent(view, null);
    }
    
    public void openSourceDirectory(String psSourcePath)
    {
        showDirectoryContent(null, psSourcePath);
    }
}
