package com.here.zuki.imhere.Utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by zuki on 4/2/17.
 */

public class ApplicationContextProvider extends Application{
    private static Context pContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        pContext = getApplicationContext();
    }

    public static Context getContext()
    {
        return pContext;
    }
}
