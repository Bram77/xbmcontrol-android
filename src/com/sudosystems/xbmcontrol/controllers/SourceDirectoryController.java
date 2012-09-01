package com.sudosystems.xbmcontrol.controllers;


import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.utilities.StringUtils;
import com.sudosystems.xbmcontrol.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SourceDirectoryController extends GlobalController
{
    private SourceDirectoryController self;
    private TableRow iContextMenuRow    = null;
    private TableRow ioDirectoryUpRow   = null;
    private TableLayout sourceDirectoryTable;
    
    public SourceDirectoryController(Context context)
    {
        super(context);
        self                    = this;
        sourceDirectoryTable    = (TableLayout) iActivity.findViewById(R.id.table_source_directory);
    }
    
    public void setContextMenuRow(TableRow sourceRow)
    {
        iContextMenuRow = sourceRow;
    }
    
    public void prepareMenu(Menu menu)
    {
        if(iActivityParams.getString("MEDIA_TYPE").equals(StaticData.MEDIA_TYPE_VIDEO))
        {
            if(Configuration.isHideWatchedEnabled())
            {
                menu.getItem(2).setVisible(false);
                menu.getItem(1).setVisible(true);
            }
            else
            {
                menu.getItem(2).setVisible(true);
                menu.getItem(1).setVisible(false);
            }
        }
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
        iXbmc.Files.getDirectory(iActivityParams.getString("MEDIA_TYPE"), iActivityParams.getString("CURRENT_PATH"), new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                self.showLoadingRow(sourceDirectoryTable);
                //self.showDialog("Loading data...");
            }
            
            @Override
            public void onSuccess(JSONObject response)
            {
                //Log.v("RESULT", response.toString());
                
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
                self.hideLoadingRow(sourceDirectoryTable);
                //self.hideDialog();
            }
        });
    }
    
    private void handleDisplayDirectoryResponse(JSONObject response)
    {
        JSONObject loError = response.optJSONObject("error");

        if(loError != null)
        {
            self.notify("ERROR: Source not accessable");
            openSourceDirectoryIntent(iActivityParams.getString("MEDIA_TYPE"), iActivityParams.getString("ROOT_PATH"), Util.getOneDirectoryUp(iActivityParams.getString("CURRENT_PATH")));
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
            self.notify("ERROR: No " +iActivityParams.getString("MEDIA_TYPE")+ " found");
            openSourceDirectoryIntent(iActivityParams.getString("MEDIA_TYPE"), iActivityParams.getString("ROOT_PATH"), Util.getOneDirectoryUp(iActivityParams.getString("CURRENT_PATH")));
            return;
        }

        addDirectoryUpRow(sourceDirectoryTable);

        for(int i=0; i < files.length(); i++)
        {
            addRow(i, files.optJSONObject(i), sourceDirectoryTable);
        }
    }
    
    private void addRow(int index, final JSONObject file, TableLayout sourceDirectoryTable)
    {
        int episode                  = file.optInt("episode", -1);
        String labelPrefix           = "";
        String sourceName            = "";
        final int fIndex            = index;
        final boolean isDirectory   = (file.optString("filetype", "").equals("directory"));
        final boolean isMovie       = (file.optString("type", "").equals("movie"));
        final boolean isWatched     = (file.optInt("playcount", 0) > 0);
        final boolean isVideo       = (iActivityParams.getString("MEDIA_TYPE").equals(StaticData.MEDIA_TYPE_VIDEO));
        final boolean isAudio       = (iActivityParams.getString("MEDIA_TYPE").equals(StaticData.MEDIA_TYPE_AUDIO));
        
        if(isWatched && isVideo && Configuration.isHideWatchedEnabled())
        {
            return;
        }

        TableRow sourceRow          = (isDirectory)? (TableRow) iActivity.getLayoutInflater().inflate(R.layout.directory_template, null) : (TableRow) iActivity.getLayoutInflater().inflate(R.layout.file_template, null);
        ImageView rowIcon           = (ImageView) sourceRow.findViewById(R.id.content_icon);
        TextView rowLabel           = (TextView) sourceRow.findViewById(R.id.content_label);
        TextView sourcePath         = (TextView) sourceRow.findViewById(R.id.content_path);
        ImageView rowWatchedIcon    = (ImageView) sourceRow.findViewById(R.id.content_watched_icon);
        
        rowIcon.setImageResource(getMediaIcon(isDirectory, iActivityParams.getString("MEDIA_TYPE"), file.optString("type", "")));
        sourceRow.setOnCreateContextMenuListener(iActivity);

        //Apply watched status
        if(isWatched && isVideo)
        {
            rowWatchedIcon.setVisibility(View.VISIBLE);
            rowLabel.setTextColor(Color.LTGRAY);
        }
        
        if(isAudio)
        {
            sourceName = (isDirectory)? StringUtils.getDirectoryNameFormUrl(file.optString("file", "")) : StringUtils.getFileNameFormUrl(file.optString("file", ""), true);
        }
        else
        {
            if(isDirectory)
            {
                sourceName = file.optString("label", "");
            }
            else if(episode > -1) //prepend episode if tvshow
            {
                labelPrefix = Integer.toString(episode)+ ". ";
                sourceName  = labelPrefix+file.optString("label", "");
            }
        }

        rowLabel.setText(sourceName);
        sourcePath.setText(file.optString("file", ""));

        sourceRow.setOnClickListener(new OnClickListener() 
        {
            public void onClick(View view) 
            {
                if(isDirectory && !isMovie)
                {
                    openSourceDirectoryIntent(iActivityParams.getString("MEDIA_TYPE"), iActivityParams.getString("ROOT_PATH"), view);
                }
                else
                {
                    if(isAudio)
                    {
                        playDirectory(file, fIndex);
                        return;
                    }

                    playFile(file);
                }
            }
        });

        sourceDirectoryTable.addView(sourceRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
    
    private void addDirectoryUpRow(TableLayout sourcesTable)
    {
        ioDirectoryUpRow        = (TableRow) iActivity.getLayoutInflater().inflate(R.layout.directory_template, null);
        ImageView rowIcon       = (ImageView) ioDirectoryUpRow.findViewById(R.id.content_icon);
        TextView rowTitle       = (TextView) ioDirectoryUpRow.findViewById(R.id.content_label);
        TextView rowPath        = (TextView) ioDirectoryUpRow.findViewById(R.id.content_path);
        
        rowIcon.setImageResource(R.drawable.folder_open_64);
        rowTitle.setText("..");
        rowPath.setText(Util.getOneDirectoryUp(iActivityParams.getString("CURRENT_PATH")));
        
        ioDirectoryUpRow.setOnClickListener(new OnClickListener() 
        {
            public void onClick(View view) 
            {
                openSourceDirectoryIntent(iActivityParams.getString("MEDIA_TYPE"), iActivityParams.getString("ROOT_PATH"), view);
            }   
        });

        sourcesTable.addView(ioDirectoryUpRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
 
    public void playFile(final JSONObject fileData)
    {
        iXbmc.Player.playFile(fileData.optString("file"), new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                handlePlayFileResponse(response, fileData.optString("label"));
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
                showPlaybackFailedMessage(fileData.optString("label"));
            }
        });
    }
    
    public void playDirectory(MenuItem item, final JSONObject fileData, final int itemIndex)
    {
        String path;
        final String title;
        
        if(fileData == null)
        {
            final TextView sourceTitle = (TextView) iContextMenuRow.findViewById(R.id.content_label);
            title                       = sourceTitle.getText().toString();
            TextView sourcePath         = (TextView) iContextMenuRow.findViewById(R.id.content_path);
            path                        = sourcePath.getText().toString();
        }
        else
        {
            title   = fileData.optString("label", "");
            path    = iActivityParams.getString("CURRENT_PATH");
        }

        iXbmc.Playlist.addDirectory(iActivityParams.getString("MEDIA_TYPE"), path, true, new JsonHttpResponseHandler()
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
            iXbmc.Player.playPlaylist(iActivityParams.getString("MEDIA_TYPE"), itemIndex, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(JSONObject response)
                {
                    handlePlayPlaylistResponse(response, title, itemIndex);
                }
                
                @Override
                public void onFailure(Throwable e, String response) 
                {
                    showPlaybackFailedMessage(title);
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
            showPlaybackStartedMessage(title);
            return;
        }

        showPlaybackFailedMessage(title);
    }
    
    private void handlePlayFileResponse(JSONObject response, final String title)
    {
        final String playFileResult = response.optString("result");
        
        if(playFileResult != null && playFileResult.equals("OK"))
        {
            showPlaybackStartedMessage(title);
            return;
        }

        showPlaybackFailedMessage(title);
    }
    
    private void showPlaybackStartedMessage(String fileTitle)
    {
        String message = iActivityParams.getString("MEDIA_TYPE").equals(StaticData.MEDIA_TYPE_PICTURES)? "The image '" +fileTitle+ "' is being displayed" : "Playback of '" +fileTitle+ "' started"; 
        self.notify(message);
    }
    
    private void showPlaybackFailedMessage(String fileTitle)
    {
        String message = iActivityParams.getString("MEDIA_TYPE").equals(StaticData.MEDIA_TYPE_PICTURES)? "ERROR: The image '" +fileTitle+ "' could not be displayed" : "ERROR: Playback of '" +fileTitle+ "' could not be started"; 
        self.notify(message);
    }
}
