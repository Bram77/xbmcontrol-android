package com.sudosystems.xbmcontrol.controllers;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.xbmcontrol.R;

public class SourceController extends GlobalController
{
    private SourceController self;
    private TableLayout sourcesTable;
    
    public SourceController(Context context)
    {
        super(context);
        self            = this;
        sourcesTable    = (TableLayout) iActivity.findViewById(R.id.table_sources);
    }
    
    public void displaySources()
    {
        iXbmc.Files.getSources(iActivityParams.getString("MEDIA_TYPE"), new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                self.showLoadingRow(sourcesTable);
                //self.showDialog("Loading data...");
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
                self.hideLoadingRow(sourcesTable);
                //self.hideDialog();
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

        JSONArray sources = result.optJSONArray("sources");

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
            ImageView rowIcon       = (ImageView) sourceRow.findViewById(R.id.content_icon);
            TextView sourceTitle    = (TextView) sourceRow.findViewById(R.id.content_label);
            TextView sourcePath     = (TextView) sourceRow.findViewById(R.id.content_path);
            
            rowIcon.setImageResource(getMediaIcon(true, currentMediaType));
            sourceTitle.setText(sources.optJSONObject(i).optString("label", "No label specified"));
            sourcePath.setText(sources.optJSONObject(i).optString("file", ""));
            
            sourcesTable.addView(sourceRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }
}
