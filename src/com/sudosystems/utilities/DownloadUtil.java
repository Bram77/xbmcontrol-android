package com.sudosystems.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadUtil extends AsyncTask<Void, Void, Object>
{
    private DownloadCompleteListener iCallback;
    private String iSourceUrl;
    private String iFileType;
    
    public DownloadUtil(String fileType, String sourceUrl, DownloadCompleteListener callback)
    {
        iFileType   = fileType;
        iSourceUrl  = sourceUrl;
        iCallback   = callback;
    }

    @Override
    public void onPreExecute() 
    {
        Log.v("DownloadUtil", "Download of '" +iSourceUrl+ "' starting");
    }
    
    @Override
    public Object doInBackground(Void... params) 
    {
        if(!StringUtils.isValidWebUrl(iSourceUrl))
        {
            Log.e("DownloadUtil", "Invalid source url provided");
            return null;
        }
        
        URLConnection connection;
        InputStream response = null;
        
        try
        {
            connection  = (new URL(iSourceUrl)).openConnection();
            connection.setUseCaches(true);
            response    = connection.getInputStream();
        }
        catch(MalformedURLException e)
        {
            Log.e("DownloadUtil", "The file could not be downloaded from: " +iSourceUrl);
            e.printStackTrace();
        }
        catch(IOException e)
        {
            Log.e("DownloadUtil", "Error connecting to remote server");
            e.printStackTrace();
            return null;
        }
        
        if(iFileType.equals("image") && response != null)
        {
            return BitmapFactory.decodeStream(response);
        }
        
        return response;
    }

    @Override
    public void onPostExecute(Object result) 
    {
        iCallback.onTaskComplete(result);
    }
}