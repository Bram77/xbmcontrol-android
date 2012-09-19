package com.sudosystems.xbmc.client;

import java.io.IOException;
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
    private EventClient ioEventClient;
    private static int UDP_PORT = 9777;
    
    public RemoteClient(GlobalController controller)
    {
    	iController = controller;
        this.execute();
    }

    public void left()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_LEFT, false, true, true, (short)0, (byte)0);
    }
    
    public void right()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_RIGHT, false, true, true, (short)0, (byte)0);
    }
    
    public void up()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_UP, false, true, true, (short)0, (byte)0);
    }
    
    public void down()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_DOWN, false, true, true, (short)0, (byte)0);
    }
    
    public void select()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_SELECT, false, true, true, (short)0, (byte)0);
    }
    
    public void home()
    {
        ioEventClient.sendButton("KB", ButtonCodes.KEYBOARD_ESCAPE, false, true, true, (short)0, (byte)0);
    }
    
    public void info()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_INFO, false, true, true, (short)0, (byte)0);
    }

    public void back()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_BACK, false, true, true, (short)0, (byte)0);
    }

    public void contextMenu()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_TITLE, false, true, true, (short)0, (byte)0);
    }
    
    public void shutDown()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_POWER, false, true, true, (short)0, (byte)0);
    }
    
    public void menu()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_MENU, false, true, true, (short)0, (byte)0);
    }
    
    public void volumeUp()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_VOLUME_PLUS, false, true, true, (short)0, (byte)0);
    }
    
    public void volumeDown()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_VOLUME_MINUS, false, true, true, (short)0, (byte)0);
    }
    
    //Playback controls
    public void playbackPrevious()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_SKIP_MINUS, false, true, true, (short)0, (byte)0);
    }
    
    public void playbackPause()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_PAUSE, false, true, true, (short)0, (byte)0);
    }
    
    public void playbackStart()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_PLAY, false, true, true, (short)0, (byte)0);
    }
    
    public void playbackStop()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_STOP, false, true, true, (short)0, (byte)0);
    }
    
    public void playbackNext()
    {
        ioEventClient.sendButton("R1", ButtonCodes.REMOTE_SKIP_PLUS, false, true, true, (short)0, (byte)0);
    }
    
    public boolean ping()
    {
    	try 
    	{
			ioEventClient.ping();
			return true;
		} 
    	catch(IOException e) 
    	{
			Log.v("RemoteClient::ping", "Connection to XBMC lost");
			return false;
		}
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
            ioEventClient = new EventClient(InetAddress.getByName(iController.Configuration.getConnectionValue("host_address")), UDP_PORT, "XBMControl");
        }
        catch(UnknownHostException e)
        {
            Log.v("RemoteClient", e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
