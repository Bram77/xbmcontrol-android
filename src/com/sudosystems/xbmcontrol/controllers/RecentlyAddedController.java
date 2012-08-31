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
    private ImageUtils imageUtils = new ImageUtils();

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
    
    private void handleResult(final String mediaType, final LinearLayout target, JSONObject result)
    {
        JSONArray media = result.optJSONArray(mediaType);
        
        if(media != null && media.length() > 0)
        {
            final int maxItems = 3;
            int limit           = (media.length() > maxItems)? maxItems : media.length();
            
            for(int i=0; i<limit; i++)
            {
                JSONObject item                 = media.optJSONObject(i);
                final LinearLayout iTemplate   = (LinearLayout) iActivity.getLayoutInflater().inflate(R.layout.recently_add_movie_template, null);
                TextView itemTitle              = (TextView) iTemplate.getChildAt(0);
                TextView itemExtraInfo          = (TextView) iTemplate.getChildAt(2);
                String thumbUrl                 = item.optString("thumbnail", "");
                String cleanUrl                 = getThumbUrl(thumbUrl);
                String title                    = "";
                String extraInfo                = "";

                if(mediaType.equals("movies"))
                {
                    title       = item.optString("label", "");
                    extraInfo   = item.optString("rating", "");
                    extraInfo   = "rating "+extraInfo.substring(0,3); 
                }
                else if(mediaType.equals("episodes"))
                {
                    title       = item.optString("showtitle", "");
                    extraInfo   = item.optString("label", "");
                }
                else if(mediaType.equals("albums"))
                {
                    JSONArray artists   = item.optJSONArray("artist");
                    title               = (artists.length() > 0)? artists.optString(0) : "";
                    extraInfo           = item.optString("label", "");
                }
                
                itemTitle.setText(title);
                itemExtraInfo.setText(extraInfo);
 
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
                                final ImageView movieThumb = (ImageView) iTemplate.getChildAt(1);
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
