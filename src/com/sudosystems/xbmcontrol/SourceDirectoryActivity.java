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
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class SourceDirectoryActivity extends Activity 
{
    private XbmcClient xbmc;
    private TableRow ioDirectoryUpRow       = null;
    private ProgressDialog loadingDialog;
    private TableRow contextMenuSourceRow   = null;

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
    public void onCreateContextMenu(ContextMenu menu, View sourceRow, ContextMenuInfo menuInfo) 
    {
      super.onCreateContextMenu(menu, sourceRow, menuInfo);
      
      contextMenuSourceRow =  (TableRow) sourceRow;
      TextView sourceTitle  = (TextView) ((ViewGroup) sourceRow).getChildAt(1);
      menu.setHeaderTitle("Directory '" +sourceTitle.getText()+ "'");
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
            else
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
                    final JSONArray files = result.optJSONArray("files");
                    
                    if(files == null || files.length() == 0)
                    {
                        Toast.makeText(SourceDirectoryActivity.this, "ERROR: No " +getIntent().getExtras().getString("MEDIA_TYPE")+ " files found", Toast.LENGTH_SHORT).show();
                        openSourceDirectory(xbmc.Helper.getOneDirectoryUp(getIntent().getExtras().getString("CURRENT_PATH")));
                        
                        return;
                    }
                    
                    addDirectoryUpRow(sourcesTable);

                    for(int i=0; i < files.length(); i++)
                    {
                        final int index            = i;
                        final boolean isDirectory  = (files.optJSONObject(index).optString("filetype", "").equals("directory"));
                        final String fileType       = files.optJSONObject(index).optString("type", "");
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
                                else
                                {
                                    playFile(files.optJSONObject(index), index);
                                }
                            }   
                        });
                        
                        sourceRow.setOnCreateContextMenuListener(SourceDirectoryActivity.this);
                        
                        TextView sourceTitle = (TextView) sourceRow.getChildAt(1);
                        sourceTitle.setText(files.optJSONObject(index).optString("label", "No label specified"));
                        
                        TextView sourcePath = (TextView) sourceRow.getChildAt(2);
                        sourcePath.setText(files.optJSONObject(index).optString("file", ""));
                        
                        sourcesTable.addView(sourceRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    }
                }
                
                //Log.v("RESULT", response.toString());
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
    
    private void playFile(final JSONObject fileData, int itemIndex)
    {
        playDirectory(fileData, itemIndex);
    }

    public void playDirectory(MenuItem item, final JSONObject fileData, final int itemIndex)
    {
        String path;
        final String title;
        
        if(fileData == null)
        {
            final TextView sourceTitle = (TextView) contextMenuSourceRow.getChildAt(1);
            title                       = sourceTitle.getText().toString();
            TextView sourcePath         = (TextView) contextMenuSourceRow.getChildAt(2);
            path                        = sourcePath.getText().toString();
        }
        else
        {
            title   = fileData.optString("label", "");
            path    = getIntent().getExtras().getString("CURRENT_PATH");
        }

        xbmc.Playlist.addDirectory(
                getIntent().getExtras().getString("MEDIA_TYPE"), 
                path, 
                true, 
                new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                loadingDialog = ProgressDialog.show(SourceDirectoryActivity.this, "", "Starting playback...", true);
            }
            
            @Override
            public void onSuccess(JSONObject response)
            {
                String addDirectoryResult = response.optString("result");

                if(addDirectoryResult != null && addDirectoryResult.equals("OK"))
                {
                    xbmc.Player.playPlaylist(getIntent().getExtras().getString("MEDIA_TYPE"), itemIndex, new JsonHttpResponseHandler()
                    {
                        @Override
                        public void onSuccess(JSONObject playPlaylistResponse)
                        {
                            final String playPlaylistResult= playPlaylistResponse.optString("result");
                            
                            if(playPlaylistResult != null && playPlaylistResult.equals("OK"))
                            {
                                Toast.makeText(SourceDirectoryActivity.this, "Playback of '" +title+ "' started", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(SourceDirectoryActivity.this, "ERROR: Playback of '" +title+ "' could not be started", Toast.LENGTH_LONG).show();
                                Log.v("PLAYBACK ERROR", playPlaylistResponse.toString());
                            }
                        }
                        
                        @Override
                        public void onFailure(Throwable e, String response) 
                        {
                            Toast.makeText(SourceDirectoryActivity.this, "ERROR: Playback of '" +title+ "' could not be started", Toast.LENGTH_LONG).show();
                            Log.v("PLAYBACK ERROR", response);
                        }
                    });
                }
                else
                {
                    Toast.makeText(SourceDirectoryActivity.this, "ERROR: '" +title+ "' could not be added to playlist", Toast.LENGTH_LONG).show();
                    Log.v("ADD PLAYLIST ERROR", response.toString());
                }
                
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
                Toast.makeText(SourceDirectoryActivity.this, "ERROR: '" +title+ "' could not be added to playlist", Toast.LENGTH_LONG).show();
                Log.v("ADD PLAYLIST ERROR", response.toString());
            }

            @Override
            public void onFinish()
            {
                loadingDialog.cancel();
            }
        });
    }
    
    public void playDirectory(MenuItem item)
    {
        playDirectory(item, null, 0);
    }
    
    public void playDirectory(final JSONObject fileData, int itemIndex)
    {
        playDirectory(null, fileData, itemIndex);
    }
    
    public void enqueDirectory(MenuItem menuItem)
    {
        
    }
    
    public void openSource()
    {
        Intent intent = new Intent(this, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", getIntent().getExtras().getString("MEDIA_TYPE"));
        intent.putExtra("ACTIVITY_TITLE", getIntent().getExtras().getString("ACTIVITY_TITLE"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent);
        this.finish();
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
        
        Intent intent = new Intent(this, SourceDirectoryActivity.class);
        intent.putExtra("MEDIA_TYPE", getIntent().getExtras().getString("MEDIA_TYPE"));
        intent.putExtra("IS_ROOT", false);
        intent.putExtra("ROOT_PATH", getIntent().getExtras().getString("ROOT_PATH"));
        intent.putExtra("CURRENT_PATH", lsSourcePath);
        intent.putExtra("ACTIVITY_TITLE", getIntent().getExtras().getString("ACTIVITY_TITLE"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(intent);
        this.finish();
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
