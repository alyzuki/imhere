package com.here.zuki.imhere.Utils;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import java.util.Locale;

import com.here.zuki.imhere.R;
import com.here.zuki.imhere.Utils.Common;

import static com.here.zuki.imhere.Utils.Common.PREF_LANG;

/**
 * Created by zuki on 4/2/17.
 */

public class ApplicationContextProvider extends Application{
    private static Context pContext;
    private static PrefConfig pref;

    @Override
    public void onCreate()
    {
        super.onCreate();
        pContext = getApplicationContext();
        pref = PrefConfig.getInstance();
    }

    public static Context getContext()
    {
        return pContext;
    }

    public static Context setLocale()
    {
        String strLocale = pref.configGetString(PREF_LANG, "");
        if(strLocale == null || strLocale.isEmpty() || strLocale.length() == 0)
            return pContext;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(pContext, strLocale);
        }

        return updateResourcesLegacy(pContext, strLocale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    public static void appClose()
    {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
    }

  }


