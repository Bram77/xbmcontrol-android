package com.sudosystems.xbmc.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.xbmc.eventclient.ButtonCodes;
import org.xbmc.eventclient.EventClient;

import com.sudosystems.xbmcontrol.controllers.GlobalController;

import android.os.AsyncTask;
import android.util.Log;

public class RemoteClient extends AsyncTask<Object, Object, Object>
{
	private GlobalController iController;
    private EventClient eventClient;
    private static int UDP_PORT         = 9777;
    private static String HOST_ADDRESS  = "donda.nl";
    
    public RemoteClient(GlobalController controller)
    {
    	iController = controller;
        this.execute();
    }

    public void left()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_LEFT, false, true, true, (short)0, (byte)0);
    }
    
    public void right()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_RIGHT, false, true, true, (short)0, (byte)0);
    }
    
    public void up()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_UP, false, true, true, (short)0, (byte)0);
    }
    
    public void down()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_DOWN, false, true, true, (short)0, (byte)0);
    }
    
    public void select()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_SELECT, false, true, true, (short)0, (byte)0);
    }
    
    public void home()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_DISPLAY, false, true, true, (short)0, (byte)0);
    }
    
    public void info()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_INFO, false, true, true, (short)0, (byte)0);
    }

    public void back()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_BACK, false, true, true, (short)0, (byte)0);
    }

    public void contextMenu()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_TITLE, false, true, true, (short)0, (byte)0);
    }
    
    public void menu()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_MENU, false, true, true, (short)0, (byte)0);
    }
    
    public void volumeUp()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_VOLUME_PLUS, false, true, true, (short)0, (byte)0);
    }
    
    public void volumeDown()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_VOLUME_MINUS, false, true, true, (short)0, (byte)0);
    }
    
    //Playback controls
    public void playbackPrevious()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_SKIP_MINUS, false, true, true, (short)0, (byte)0);
    }
    
    public void playbackPause()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_PAUSE, false, true, true, (short)0, (byte)0);
    }
    
    public void playbackStart()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_PLAY, false, true, true, (short)0, (byte)0);
    }
    
    public void playbackStop()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_STOP, false, true, true, (short)0, (byte)0);
    }
    
    public void playbackNext()
    {
        eventClient.sendButton("R1", ButtonCodes.REMOTE_SKIP_PLUS, false, true, true, (short)0, (byte)0);
    }

    @Override
    protected Object doInBackground(Object... params)
    {
    	if(!iController.isConfigured())
    	{
    		return null;
    	}
    	
        try
        {
            eventClient = new EventClient(InetAddress.getByName(iController.Configuration.getConnectionValue("host_address")), UDP_PORT, "XBMControl");
        }
        catch(UnknownHostException e)
        {
            Log.v("RemoteClient", e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
