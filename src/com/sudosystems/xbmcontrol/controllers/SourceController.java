package com.sudosystems.xbmcontrol.controllers;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.xbmc.client.XbmcClient;
import com.sudosystems.xbmcontrol.R;

public class SourceController extends GlobalController
{
    private XbmcClient xbmc;
    private Activity iActivity;
    private Bundle iActivityParams;
    private SourceController self;
    
    public SourceController(Context context, Bundle activityParams)
    {
        super(context);
        self            = this;
        iActivity       = (Activity) context;
        iActivityParams = activityParams;
        xbmc            = new XbmcClient(context);
    }
    
    public void displaySources()
    {
        xbmc.Files.getSources(iActivityParams.getString("MEDIA_TYPE"), new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                self.showDialog("Loading data...");
            }
            
            @Override
            public void onSuccess(JSONObject response)
            {
                handleResponse(response);
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
    
    private void handleResponse(JSONObject response)
    {
        JSONObject result = response.optJSONObject("result");

        if(result == null)
        {
            return;
        }
        
        TableLayout sourcesTable    = (TableLayout) iActivity.findViewById(R.id.table_sources);
        JSONArray sources           = result.optJSONArray("sources");

        if(sources == null || sources.length() < 1)
        {
            notify("ERROR: No sources available");
            openHomeIntent();
            
            return;
        }
        
        for(int i=0; i < sources.length(); i++)
        {
            String currentMediaType = iActivityParams.getString("MEDIA_TYPE");
            TableRow sourceRow      = (TableRow) iActivity.getLayoutInflater().inflate(R.layout.directory_template, null);
            
            ImageView rowIcon = (ImageView) sourceRow.getChildAt(0);
            rowIcon.setImageResource(getMediaIcon(true, currentMediaType));

            TextView sourceTitle = (TextView) sourceRow.getChildAt(1);
            sourceTitle.setText(sources.optJSONObject(i).optString("label", "No label specified"));
            
            TextView sourcePath = (TextView) sourceRow.getChildAt(2);
            sourcePath.setText(sources.optJSONObject(i).optString("file", ""));
            
            sourcesTable.addView(sourceRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }
}
