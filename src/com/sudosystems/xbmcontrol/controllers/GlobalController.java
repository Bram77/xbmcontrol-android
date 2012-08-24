package com.sudosystems.xbmcontrol.controllers;

import com.sudosystems.xbmc.client.FilesClient.MediaType;
import com.sudosystems.xbmcontrol.HomeActivity;
import com.sudosystems.xbmcontrol.R;
import com.sudosystems.xbmcontrol.SourceActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class GlobalController
{
    private Context iContext;
    private Activity iActivity;
    private ProgressDialog iDialog;
    
    public GlobalController(Context context)
    {
        iContext    = context;
        iActivity   = (Activity) context;
    }
    
    public void notify(String message)
    {
        Toast.makeText(iContext, message, Toast.LENGTH_LONG).show();
    }

    public void showDialog(String message)
    {
        iDialog = ProgressDialog.show(iContext, "", message, true);
    }
    
    public void hideDialog()
    {
        iDialog.cancel();
    }
    
    public int getMediaIcon(boolean isDirectory, String mediaType)
    {
        if(mediaType.equals(MediaType.AUDIO))
        {
            return (isDirectory)? R.drawable.folder_audio_64 : R.drawable.file_audio_64 ;
        }
        
        if(mediaType.equals(MediaType.VIDEO))
        {
            if(isDirectory)
            {
                return R.drawable.folder_video_64;
            }
            
            if(mediaType.equals("episode"))
            {
                return R.drawable.file_video_64;
            }
            
            if(mediaType.equals("movie"))
            {
                return R.drawable.file_video_64;
            }
        }
        
        if(mediaType.equals(MediaType.PICTURES))
        {
            return (isDirectory)? R.drawable.folder_pictures_64 : R.drawable.file_image_64;
        }
        
        return (isDirectory)? R.drawable.folder_64 : R.drawable.file_64;
    }
    
    public void openHomeIntent()
    {
        Intent intent = new Intent(iContext, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        iActivity.startActivity(intent);
        iActivity.finish();
    }
    
    public void openSourceIntent(String mediaType, String title)
    {
        Intent intent = new Intent(iContext, SourceActivity.class);
        intent.putExtra("MEDIA_TYPE", mediaType);
        intent.putExtra("ACTIVITY_TITLE", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        iActivity.startActivity(intent);
        iActivity.finish();
    }
}
