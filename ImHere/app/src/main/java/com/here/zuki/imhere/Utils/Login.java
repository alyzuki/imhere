package com.here.zuki.imhere.Utils;

import android.util.Log;

/**
 * Created by zuki on 4/4/17.
 */

public class Login {

    private static Login instance = null;
    private String userName;

    public Login() {super();}

    public Login(String userName) {super(); this.userName = userName;}

    public static synchronized Login getInstance(){
        if(instance == null)
        {
            instance = new Login("Hieu");
        }
        return instance;
    }

    public void login()
    {

    }

    public String getUserName()
    {
        return this.userName;
    }


}
