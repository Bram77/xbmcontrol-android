package com.sudosystems.xbmcontrol.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sudosystems.utilities.StringUtils;
import com.sudosystems.xbmcontrol.R;
import com.sudosystems.utilities.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecentlyAddedController extends GlobalController
{
    ImageUtils imageUtils = new ImageUtils();
    
    public RecentlyAddedController(Context context)
    {
        super(context);
    }
    
    public void showMovies(final LinearLayout target)
    {
        iXbmc.VideoLibrary.GetRecentlyAddedMovies(new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
            }
            
            @Override
            public void onSuccess(JSONObject response)
            {
                if(response != null)
                {
                    JSONObject result = response.optJSONObject("result");
                    
                    if(result != null)
                    {
                        handleResult("movies", target, result);
                        return;
                    }
                    
                    Log.d("RecentlyAddedController::showMovies", "Xbmc returned an empty result");
                }
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
            }
            
            @Override
            public void onFinish()
            {
            }
        });
    }
    
    public void showEpisodes(final LinearLayout target)
    {
        iXbmc.VideoLibrary.GetRecentlyAddedEpisodes(new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
            }
            
            @Override
            public void onSuccess(JSONObject response)
            {
                if(response != null)
                {
                    JSONObject result = response.optJSONObject("result");
                    
                    if(result != null)
                    {
                        handleResult("episodes", target, result);
                        return;
                    }
                    
                    Log.d("RecentlyAddedController::showEpisodes", "Xbmc returned an empty result");
                }
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
            }
            
            @Override
            public void onFinish()
            {
            }
        });
    }
    
    public void showAlbums(final LinearLayout target)
    {
        iXbmc.AudioLibrary.GetRecentlyAddedAlbums(new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
            }
            
            @Override
            public void onSuccess(JSONObject response)
            {
                if(response != null)
                {
                    JSONObject result = response.optJSONObject("result");
                    
                    if(result != null)
                    {
                        handleResult("albums", target, result);
                        return;
                    }
                    
                    Log.d("RecentlyAddedController::showEpisodes", "Xbmc returned an empty result");
                }
            }
            
            @Override
            public void onFailure(Throwable e, String response) 
            {
            }
            
            @Override
            public void onFinish()
            {
            }
        });
    }
    
    private void handleResult(String mediaType, LinearLayout target, JSONObject result)
    {
        JSONArray movies    = result.optJSONArray(mediaType);
        int itemLimit       = 1;
        int itemCount       = (result.length() < itemLimit)? result.length() : itemLimit;
        
        if(movies != null && movies.length() > 0)
        {
            for(int i=0; i<itemCount; i++)
            {
                JSONObject movie            = movies.optJSONObject(i);
                LinearLayout iTemplate      = (LinearLayout) iActivity.getLayoutInflater().inflate(R.layout.recently_add_movie_template, null);
                TextView movieTitle         = (TextView) iTemplate.getChildAt(1);
                final ImageView movieThumb = (ImageView) iTemplate.getChildAt(0);
                String thumbUrl             = movie.optString("thumbnail", "");
                
                movieTitle.setText(movie.optString("label", "No title provided"));
                
                String cleanUrl = getThumbUrl(thumbUrl);

                if(cleanUrl != null && StringUtils.isValidWebUrl(cleanUrl))
                {
                    new ImageDownload(cleanUrl, new DownloadCompleteListener()
                    {
                        public void onTaskComplete(Object image)
                        {
                            Bitmap imageFile = (image != null)? (Bitmap) image : null;

                            if(imageFile != null && imageFile instanceof Bitmap)
                            {
                                Bitmap scaledImage = imageUtils.getScaledImage(imageFile, 200, 200);
                                movieThumb.setImageBitmap(scaledImage);
                            }
                            else
                            {
                                Log.v("RecentlyAddedController::handleMoviesResult", "Image file could not be downloaded");
                            }
                        }
                    });
                }
                
                target.addView(iTemplate);
            }
        }
    }

    private String getThumbUrl(String rawUrl)
    {
        String decodedUrl   = null;
        String splitString  = "image://";
        
        if(rawUrl.equals("") || rawUrl.length() <= splitString.length())
        {
            return null;
        }
        
        try
        {
            decodedUrl = URLDecoder.decode(rawUrl.substring(splitString.length(), rawUrl.length()), "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            Log.e("RecentlyAddedController::getThumbUrl", "Thumb url: '" +rawUrl+ "' could not be decoded");
            e.printStackTrace();
        }
        
        return decodedUrl;
    }
}
