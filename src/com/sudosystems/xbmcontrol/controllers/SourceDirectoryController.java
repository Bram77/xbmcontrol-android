package com.sudosystems.xbmcontrol.controllers;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.xbmc.client.FilesClient.MediaType;
import com.sudosystems.xbmc.client.XbmcClient;
import com.sudosystems.xbmcontrol.R;
import com.sudosystems.xbmcontrol.SourceDirectoryActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SourceDirectoryController extends GlobalController
{
    private XbmcClient xbmc;
    private Activity iActivity;
    private Bundle iActivityParams;
    private Context iContext;
    private SourceDirectoryController self;
    private TableRow iContextMenuRow    = null;
    private TableRow ioDirectoryUpRow   = null;
    
    public SourceDirectoryController(Context context, Bundle activityParams)
    {
        super(context);
        self            = this;
        iContext        = context;
        iActivity       = (Activity) context;
        iActivityParams = activityParams;
        xbmc            = new XbmcClient(context);
    }
    
    public void setContextMenuRow(TableRow sourceRow)
    {
        iContextMenuRow = sourceRow;
    }
    
    public void showDirectoryUpIntent()
    {
        if(ioDirectoryUpRow != null)
        {
            ioDirectoryUpRow.performClick();
        }
    }
    
    public void displayDirectoryContent()
    {
        xbmc.Files.getDirectory(iActivityParams.getString("MEDIA_TYPE"), iActivityParams.getString("CURRENT_PATH"), new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                self.showDialog("Loading data...");
            }
            
            @Override
            public void onSuccess(JSONObject response)
            {
                handleDisplayDirectoryResponse(response);
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
                self.notify("ERROR: Source not accessable");
                self.openHomeIntent();
            }
            
            @Override
            public void onFinish()
            {
                self.hideDialog();
            }
        });
    }
    
    private void handleDisplayDirectoryResponse(JSONObject response)
    {
        JSONObject loError = response.optJSONObject("error");

        if(loError != null)
        {
            self.notify("ERROR: Source is not accessable");
            openSourceDirectoryIntent(Util.getOneDirectoryUp(iActivityParams.getString("CURRENT_PATH")));
            return;
        }
        
        JSONObject result = response.optJSONObject("result");

        if(result == null)
        {
            return;
        }

        final JSONArray files = result.optJSONArray("files");
        
        if(files == null || files.length() == 0)
        {
            self.notify("ERROR: No " +iActivityParams.getString("MEDIA_TYPE")+ " files found");
            openSourceDirectoryIntent(Util.getOneDirectoryUp(iActivityParams.getString("CURRENT_PATH")));
            return;
        }
        
        TableLayout sourcesTable = (TableLayout) iActivity.findViewById(R.id.table_source_directory);
        addDirectoryUpRow(sourcesTable);

        for(int i=0; i < files.length(); i++)
        {
            final int index            = i;
            final boolean isDirectory  = (files.optJSONObject(index).optString("filetype", "").equals("directory"));
            TableRow sourceRow          = (isDirectory)? (TableRow) iActivity.getLayoutInflater().inflate(R.layout.directory_template, null) : (TableRow) iActivity.getLayoutInflater().inflate(R.layout.file_template, null);
            ImageView rowIcon           = (ImageView) sourceRow.getChildAt(0);
            rowIcon.setImageResource(getMediaIcon(isDirectory, iActivityParams.getString("MEDIA_TYPE")));

            sourceRow.setOnClickListener(new OnClickListener() 
            {
                public void onClick(View view) 
                {
                    if(isDirectory)
                    {
                        openSourceDirectoryIntent(view);
                    }
                    else
                    {
                        if(iActivityParams.getString("MEDIA_TYPE").equals(MediaType.AUDIO))
                        {
                            playDirectory(files.optJSONObject(index), index);
                        }
                        else
                        {
                            playFile(files.optJSONObject(index));
                        }
                    }
                }   
            });
            
            sourceRow.setOnCreateContextMenuListener(iActivity);
            
            TextView sourceTitle = (TextView) sourceRow.getChildAt(1);
            sourceTitle.setText(files.optJSONObject(index).optString("label", "> No label specified <"));
            
            TextView sourcePath = (TextView) sourceRow.getChildAt(2);
            sourcePath.setText(files.optJSONObject(index).optString("file", ""));
            
            sourcesTable.addView(sourceRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }
    
    private void addDirectoryUpRow(TableLayout sourcesTable)
    {
        ioDirectoryUpRow = (TableRow) iActivity.getLayoutInflater().inflate(R.layout.directory_template, null);
        ioDirectoryUpRow.setOnClickListener(new OnClickListener() 
        {
            public void onClick(View view) 
            {
                openSourceDirectoryIntent(view);
            }   
        });
        
        ImageView rowIcon = (ImageView) ioDirectoryUpRow.getChildAt(0);
        rowIcon.setImageResource(R.drawable.folder_open_64);
        
        TextView sourceTitle = (TextView) ioDirectoryUpRow.getChildAt(1);
        sourceTitle.setText("..");
        
        TextView sourcePath = (TextView) ioDirectoryUpRow.getChildAt(2);
        sourcePath.setText(Util.getOneDirectoryUp(iActivityParams.getString("CURRENT_PATH")));
        
        sourcesTable.addView(ioDirectoryUpRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
    
    public void openSourceDirectoryIntent(String mediaType, String rootPath, String targetPath, String title)
    {
        Intent intent = new Intent(iContext, SourceDirectoryActivity.class);
        intent.putExtra("MEDIA_TYPE", mediaType);
        intent.putExtra("ROOT_PATH", rootPath);
        intent.putExtra("CURRENT_PATH", targetPath);
        intent.putExtra("ACTIVITY_TITLE", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        iActivity.startActivity(intent);
        iActivity.finish();
    }

    public void openSourceDirectoryIntent(View view, String psSourcePath)
    {
        String targetPath = "";
        
        if(view != null)
        {
            TableRow row            = (TableRow)view;
            TextView loSourcePath   = (TextView) row.getChildAt(2);
            targetPath              = loSourcePath.getText().toString();
        }
        else if(psSourcePath != null)
        {
            targetPath = psSourcePath;
        }
        
        //TODO: Find more elegant way to check if trying to go below root
        if(targetPath.length() < iActivityParams.getString("ROOT_PATH").length())
        {
            openSourceIntent(iActivityParams.getString("MEDIA_TYPE"), iActivityParams.getString("ACTIVITY_TITLE"));
            return;
        }
        
        openSourceDirectoryIntent(iActivityParams.getString("MEDIA_TYPE"), iActivityParams.getString("ROOT_PATH"), targetPath, iActivityParams.getString("ACTIVITY_TITLE"));
    }

    public void openSourceDirectoryIntent(String psSourcePath)
    {
        openSourceDirectoryIntent(null, psSourcePath);
    }
    
    public void openSourceDirectoryIntent(View view)
    {
        openSourceDirectoryIntent(view, null);
    }
    
    public void playFile(final JSONObject fileData)
    {
        xbmc.Player.playFile(fileData.optString("file"), new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                handlePlayFileResponse(response, fileData.optString("label"));
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
                self.notify("ERROR: Playback of '" +fileData.optString("label")+ "' could not be started");
            }
        });
    }
    
    public void playDirectory(MenuItem item, final JSONObject fileData, final int itemIndex)
    {
        String path;
        final String title;
        
        if(fileData == null)
        {
            final TextView sourceTitle = (TextView) iContextMenuRow.getChildAt(1);
            title                       = sourceTitle.getText().toString();
            TextView sourcePath         = (TextView) iContextMenuRow.getChildAt(2);
            path                        = sourcePath.getText().toString();
        }
        else
        {
            title   = fileData.optString("label", "");
            path    = iActivityParams.getString("CURRENT_PATH");
        }

        xbmc.Playlist.addDirectory(iActivityParams.getString("MEDIA_TYPE"), path, true, new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                self.showDialog("Starting playback...");
            }
            
            @Override
            public void onSuccess(JSONObject response)
            {
                handleAddDirectoryResponse(response, title, itemIndex);
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
                self.notify("ERROR: '" +title+ "' could not be added to playlist");
                self.openHomeIntent();
            }

            @Override
            public void onFinish()
            {
                self.hideDialog();
            }
        });
    }

    public void playDirectory(final JSONObject fileData, int itemIndex)
    {
        playDirectory(null, fileData, itemIndex);
    }
    
    private void handleAddDirectoryResponse(JSONObject response, final String title, final int itemIndex)
    {
        String addDirectoryResult = response.optString("result");

        if(addDirectoryResult != null && addDirectoryResult.equals("OK"))
        {
            xbmc.Player.playPlaylist(iActivityParams.getString("MEDIA_TYPE"), itemIndex, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(JSONObject response)
                {
                    handlePlayPlaylistResponse(response, title, itemIndex);
                }
                
                @Override
                public void onFailure(Throwable e, String response) 
                {
                    self.notify("ERROR: Playback of '" +title+ "' could not be started");
                }
            });
        }
        else
        {
            self.notify("ERROR: '" +title+ "' could not be added to playlist");
        }
    }
    
    private void handlePlayPlaylistResponse(JSONObject response, final String title, int itemIndex)
    {
        final String playPlaylistResult = response.optString("result");
        
        if(playPlaylistResult != null && playPlaylistResult.equals("OK"))
        {
            self.notify("Playback of '" +title+ "' started");
        }
        else
        {
            self.notify("ERROR: Playback of '" +title+ "' could not be started");
        }
    }
    
    private void handlePlayFileResponse(JSONObject response, final String title)
    {
        final String playFileResult = response.optString("result");
        
        if(playFileResult != null && playFileResult.equals("OK"))
        {
            self.notify("Playback of '" +title+ "' started");
        }
        else
        {
            self.notify("ERROR: Playback of '" +title+ "' could not be started");
        }
    }
}
