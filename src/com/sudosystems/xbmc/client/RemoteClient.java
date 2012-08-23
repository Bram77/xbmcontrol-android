package com.sudosystems.xbmc.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.xbmc.eventclient.ButtonCodes;
import org.xbmc.eventclient.EventClient;

import android.os.AsyncTask;
import android.util.Log;

public class RemoteClient extends AsyncTask<Object, Object, Object>
{
    private EventClient eventClient;
    private static int UDP_PORT         = 9777;
    private static String HOST_ADDRESS  = "donda.nl";
    
    public RemoteClient()
    {
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
        eventClient.sendButton("R1", ButtonCodes.REMOTE_ENTER, false, true, true, (short)0, (byte)0);
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

    @Override
    protected Object doInBackground(Object... params)
    {
        try
        {
            eventClient = new EventClient(InetAddress.getByName(HOST_ADDRESS), UDP_PORT, "XBMControl");
        }
        catch(UnknownHostException e)
        {
            Log.v("RemoteClient", e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
