package com.here.zuki.imhere.Utils;

import java.util.ArrayList;

/**
 * Created by zuki on 4/4/17.
 */

public class Login {

    private static Login instance = null;
    private String  userName;
    private String  passWord;
    private String  sType;
    private int     status;
    private String  cookies;
    private String  token;
    private ArrayList<PlaceObject> placeList;

    public static final int LOGIN_NONE = 0;
    public static final int LOGIN_FAIL = 1;
    public static final int LOGIN_SUCCESS = 2;
    //public static enum LOGIN_STATUS { LOGIN_NONE, LOGIN_FAIL, LOGIN_SUCCESS};

    public Login() {
        super();
        status = LOGIN_NONE;
    }

    public Login(String userName) {
        super();
        this.userName = userName;
        status = LOGIN_NONE;
    }

    public static synchronized Login getInstance(){
        if(instance == null)
        {
            instance = new Login("Hieu");
        }
        return instance;
    }

    public boolean login(String sType)
    {
        if(sType.equals("facebook"))
            return facebook_login();
        else if(sType.equals("zalo"))
            return  zalo_login();
        else
        {

        }
        return  false;
    }

    private boolean facebook_login()
    {
        return false;
    }

    private boolean zalo_login()
    {
        return false;
    }

    public String getUserName()
    {
        return this.userName;
    }


}
