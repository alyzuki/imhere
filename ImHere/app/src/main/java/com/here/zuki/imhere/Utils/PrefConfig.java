package com.here.zuki.imhere.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zuki on 3/9/17.
 */

public class PrefConfig {

    private  static PrefConfig instance;
    private Context context;
    private static SharedPreferences pref = null;
    private SharedPreferences.Editor editor = null;
    private Login login;

    public PrefConfig()
    {
        super();
        this.instance = this;
        this.context = ApplicationContextProvider.getContext();
        constructor();
    }

    public PrefConfig(String username, Context context)
    {
        super();
        this.context = context;
        constructor();
    }


    private void constructor()
    {
        login = Login.getInstance();
        pref = this.context.getSharedPreferences(login.getUserName(), context.MODE_PRIVATE);
        editor = pref.edit();
        instance = this;
    }

    public  void configSetValue(String key, int value)
    {
       // editor.clear();
        editor.putInt(key,value);
        editor.commit();
    }

    public  void configSetValue(String key, float value)
    {
        //editor.clear();
        editor.putFloat(key,value);
        editor.commit();
    }

    public  void configSetValue(String key, Long value)
    {
        //editor.clear();
        editor.putLong(key,value);
        editor.commit();
    }

    public  void configSetValue(String key, Boolean value)
    {
        //editor.clear();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public  void configSetValue(String key, String value)
    {
        //editor.clear();
        editor.putString(key,value);
        editor.commit();
    }

    public int configGetInt(String key, int defVal)
    {
        return  pref.getInt(key, defVal);
    }

    public float configGetFloat(String key, Float defVal)
    {
        return  pref.getFloat(key, defVal);
    }

    public boolean configGetBoolean(String key, Boolean defVal)
    {
        return  pref.getBoolean(key, defVal);
    }

    public long configGetLong(String key, long defVal)
    {
        return  pref.getLong(key, defVal);
    }

    public String configGetString(String key, String defVal)
    {
        return  pref.getString(key, defVal);
    }

    public String getUser()
    {
        return this.login.getUserName();
    }

    public static synchronized PrefConfig getInstance(){
        if(instance == null)
        {
            instance = new PrefConfig("", ApplicationContextProvider.getContext());
        }
        return instance;
    }

    public void changeUser()
    {
        pref = ApplicationContextProvider.getContext().getSharedPreferences(login.getUserName(), Context.MODE_PRIVATE);
        editor = pref.edit();
    }
}
