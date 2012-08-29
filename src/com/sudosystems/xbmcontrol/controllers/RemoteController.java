package com.sudosystems.xbmcontrol.controllers;

import android.content.Context;

public class RemoteController extends GlobalController
{
    private ConfigurationController iConfiguration;

    public RemoteController(Context context)
    {
        super(context);
        iConfiguration = new ConfigurationController(context);
    }
}
