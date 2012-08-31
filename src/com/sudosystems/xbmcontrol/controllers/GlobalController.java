package com.sudosystems.xbmcontrol.controllers;

import com.sudosystems.xbmc.client.RemoteClient;
import com.sudosystems.xbmc.client.XbmcClient;
import com.sudosystems.xbmcontrol.HomeActivity;
import com.sudosystems.xbmcontrol.R;
import com.sudosystems.xbmcontrol.RemoteActivity;
import com.sudosystems.xbmcontrol.ConfigurationActivity;
import com.sudosystems.xbmcontrol.SourceActivity;
import com.sudosystems.xbmcontrol.SourceDirectoryActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GlobalController
{
    public ConfigurationController Configuration;
    public XbmcClient iXbmc;
    public Context iContext;
    public Activity iActivity;
    public Bundle iActivityParams;
    private ProgressDialog iDialog;
    public RemoteClient Remote;
    public TableRow iLoadingRow;
    protected Animation iFadeInAnimation;
    protected Animation iFadeOutAnimation;
    public Vibrator iVibrator;
    
    public GlobalController(Context context)
    {
        Remote                      = new RemoteClient();
        iContext                    = context;
        iActivity                   = (Activity) context;
        iActivityParams             = iActivity.getIntent().getExtras();
        Configuration               = new ConfigurationController(context);
        iXbmc                       = new XbmcClient(context, Configuration.getConnectionData());
        iFadeInAnimation            = AnimationUtils.loadAnimation(iContext, R.anim.fade_in);
        iFadeOutAnimation           = AnimationUtils.loadAnimation(iContext, R.anim.fade_out);
        iVibrator                   = (Vibrator) iActivity.getSystemService(Context.VIBRATOR_SERVICE); 
        iLoadingRow                 = (TableRow) iActivity.getLayoutInflater().inflate(R.layout.loading_template, null);
    }
    
    public void highlightNavigationButton()
    {
        Button navigationButton = null;
        Drawable background = iActivity.getResources().getDrawable(R.drawable.button_shape_positive);
        
        if(iActivityParams.getString("MEDIA_TYPE").equals(StaticData.MEDIA_TYPE_AUDIO))
        {
            navigationButton = (Button) iActivity.findViewById(R.id.navigation_music);
        }
        else if(iActivityParams.getString("MEDIA_TYPE").equals(StaticData.MEDIA_TYPE_VIDEO))
        {
            navigationButton = (Button) iActivity.findViewById(R.id.navigation_video);
        }
        else if(iActivityParams.getString("MEDIA_TYPE").equals(StaticData.MEDIA_TYPE_PICTURES))
        {
            navigationButton = (Button) iActivity.findViewById(R.id.navigation_pictures);
        }
        else
        {
            return;
        }
        
        navigationButton.setBackgroundDrawable(background);
        navigationButton.setTextAppearance(iContext, R.style.ButtonTextPositve);
    }
    
    public void showLoadingRow(TableLayout table)
    {
        table.addView(iLoadingRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        iLoadingRow.startAnimation(iFadeInAnimation);
    }
    
    public void hideLoadingRow(TableLayout table)
    {
        iLoadingRow.startAnimation(iFadeOutAnimation);
        table.removeView(iLoadingRow);
    }
    
    public void addNavigationToLayout()
    {
        TableRow mainLayout   = (TableRow) iActivity.findViewById(R.id.navigation_container);
        View navigation       = iActivity.getLayoutInflater().inflate(R.layout.navigation_template, null);
        mainLayout.addView(navigation);
    }
    
    public void notify(String message)
    {
        Toast.makeText(iContext, message, Toast.LENGTH_LONG).show();
    }

    public void showDialog(String message)
    {
        iDialog = ProgressDialog.show(iContext, "", message, true);
        iActivity.overridePendingTransition(0, 0);
    }
    
    public void hideDialog()
    {
        iDialog.cancel();
        iActivity.overridePendingTransition(0, 0);
    }
    
    public boolean applyVolume(int keyCode)
    {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            Remote.volumeUp();
        }
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            Remote.volumeDown();
        }
        
        return true;
    }
    
    public int getMediaIcon(boolean isDirectory, String mediaType, String type)
    {
        if(mediaType.equals(StaticData.MEDIA_TYPE_AUDIO))
        {
            return (isDirectory)? R.drawable.folder_audio_64 : R.drawable.file_audio_64 ;
        }
        
        if(mediaType.equals(StaticData.MEDIA_TYPE_VIDEO))
        {
            if(isDirectory && !type.equals("movie"))
            {
                return R.drawable.folder_video_64;
            }
            
            if(type.equals("episode"))
            {
                return R.drawable.file_video_64;
            }
            
            if(type.equals("movie"))
            {
                return R.drawable.file_video_64;
            }
        }
        
        if(mediaType.equals(StaticData.MEDIA_TYPE_PICTURES))
        {
            return (isDirectory)? R.drawable.folder_pictures_64 : R.drawable.file_image_64;
        }
        
        return (isDirectory)? R.drawable.folder_64 : R.drawable.file_64;
    }
    
    public int getMediaIcon(boolean isDirectory, String mediaType)
    {
        return getMediaIcon(isDirectory, mediaType, "");
    }
    
    public void openHomeIntent()
    {
        Intent intent = new Intent(iContext, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        iActivity.startActivity(intent);
        iActivity.overridePendingTransition(0, 0);
        iActivity.finish();
    }
    
    public void openSourceIntent(String mediaType)
    {
        String title    = mediaType.substring(0,1).toUpperCase() + mediaType.substring(1);
        Intent intent   = new Intent(iContext, SourceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("MEDIA_TYPE", mediaType);
        intent.putExtra("ACTIVITY_TITLE", title);
        iActivity.startActivity(intent);
        iActivity.overridePendingTransition(0, 0);
        iActivity.finish();
    }
    
    public void openAudioIntent()
    {
        openSourceIntent(StaticData.MEDIA_TYPE_AUDIO);
    }
    
    public void openVideoIntent()
    {
        openSourceIntent(StaticData.MEDIA_TYPE_VIDEO);
    }
    
    public void openPicturesIntent()
    {
        openSourceIntent(StaticData.MEDIA_TYPE_PICTURES);
    }
    
    public void openRemoteIntent()
    {
        Intent intent = new Intent(iContext, RemoteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        iActivity.startActivity(intent);
        iActivity.overridePendingTransition(0, 0);
        iActivity.finish();
    }
    
    public void openConfigurationIntent()
    {
        Intent intent = new Intent(iContext, ConfigurationActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        iActivity.startActivity(intent);
        iActivity.finish();
    }
    
    private void openSourceDirectoryIntent(String mediaType, String rootPath, String targetPath, View view)
    {
        String lTargetPath = "";
        
        if(view != null)
        {
            TableRow row            = (TableRow) view;
            TextView rowText        = (TextView) row.getChildAt(2);
            lTargetPath             = rowText.getText().toString();
        }
        else if(targetPath != null)
        {
            lTargetPath = targetPath;
        }
        
        //TODO: Find more elegant way to check if trying to go below root
        if(lTargetPath.length() < rootPath.length())
        {
            openSourceIntent(mediaType);
            return;
        }
        
        String[] aTargetPath    = lTargetPath.split("/");
        String lTitle           = mediaType.substring(0,1).toUpperCase() + mediaType.substring(1)+ " / " +aTargetPath[(aTargetPath.length-1)];
        Intent intent           = new Intent(iContext, SourceDirectoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("MEDIA_TYPE", mediaType);
        intent.putExtra("ROOT_PATH", rootPath);
        intent.putExtra("CURRENT_PATH", lTargetPath);
        intent.putExtra("ACTIVITY_TITLE", lTitle);
        iActivity.startActivity(intent);
        iActivity.overridePendingTransition(0, 0);
        iActivity.finish();
    }

    public void openSourceDirectoryIntent(String mediaType, String rootPath, String targetPath)
    {
        openSourceDirectoryIntent(mediaType, rootPath, targetPath, null);
    }
    
    public void openSourceDirectoryIntent(String mediaType, String rootPath, View view)
    {
        openSourceDirectoryIntent(mediaType, rootPath, null, view);
    }
    
    public void openSourceDirectoryIntent()
    {
        openSourceDirectoryIntent(iActivityParams.getString("MEDIA_TYPE"), iActivityParams.getString("ROOT_PATH"), iActivityParams.getString("CURRENT_PATH"));
    }
}
