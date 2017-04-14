package com.here.zuki.imhere.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.here.zuki.imhere.LoginActivity;

/**
 * Created by zuki on 4/11/17.
 */

public class SessionManager {


    private static  SessionManager instance = null;
    private Context pContext;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final String TAG_IS_LOGIN        = "IsLogin";
    private static final String TAG_ACCOUNT         = "Account";
    private static final String TAG_ACCOUNT_TYPE    = "Type";
    private static final String TAG_OFFLINE         = "IsOfflineAllow";
    private static final String TAG_AUTOLOG         = "IsAutoLogin";

    public SessionManager(Context context)
    {
        this.pContext = context;
        init();
    }

    public SessionManager()
    {
        this.pContext = ApplicationContextProvider.getContext();
        init();
    }

    private void init()
    {
        pref = pContext.getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean(TAG_IS_LOGIN, false);
        editor.commit();
    }


    public void checkLogin()
    {
        if(isLogin() == false)
        {
            Intent loginInten  = new Intent(pContext, LoginActivity.class);
            //close all the activities
            loginInten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //start new Activity
            loginInten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pContext.startActivity(loginInten);
        }
    }


    public boolean isOffAllow()
    {
        return pref.getBoolean(TAG_OFFLINE, false);
    }

    public void setOffAllow(boolean allow)
    {
        editor.putBoolean(TAG_OFFLINE, allow);
        editor.commit();
    }

    public boolean isLogin()
    {
        return pref.getBoolean(TAG_IS_LOGIN, false);
    }

    public static synchronized SessionManager getInstance()
    {
        if(instance == null)
        {
            instance = new SessionManager();
        }
        return  instance;
    }

    public void logoutUser()
    {
        editor.putBoolean(TAG_IS_LOGIN, false);
        editor.commit();
    }


    public String getLastLogin()
    {
        return pref.getString(TAG_ACCOUNT, "");
    }

    public boolean getAutoLogin()
    {
        return pref.getBoolean(TAG_AUTOLOG, false);
    }

    public void  setAutolog(boolean auto)
    {
        editor.putBoolean(TAG_AUTOLOG, auto);
        editor.commit();
    }

    public boolean setOffline(boolean allow)
    {
        editor.putBoolean(TAG_OFFLINE, allow);
        return editor.commit();
    }

    public boolean getOffline()
    {
        return pref.getBoolean(TAG_OFFLINE, false);
    }

    public String getLastLoginType()
    {
        return pref.getString(TAG_ACCOUNT_TYPE, "");
    }

    public boolean createLoginSession(String name, String type)
    {
        if (name == null || name.isEmpty() || type == null || type.isEmpty())
            return false;
        editor.putBoolean(TAG_IS_LOGIN, true);
        editor.putString(TAG_ACCOUNT, name);
        editor.putString(TAG_ACCOUNT_TYPE, type);
        return  editor.commit();
    }

    public boolean createLoginSession(String name, String type, boolean Offline)
    {
        if(createLoginSession(name, type))
        {
            editor.putBoolean(TAG_OFFLINE, Offline);
            return editor.commit();
        }
        return false;
    }
}
