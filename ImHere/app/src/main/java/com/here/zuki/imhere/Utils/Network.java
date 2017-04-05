package com.here.zuki.imhere.Utils;

/**
 * Created by zuki on 4/5/17.
 */

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.here.zuki.imhere.R;

import cz.msebera.android.httpclient.client.cache.Resource;
import cz.msebera.android.httpclient.impl.entity.StrictContentLengthStrategy;

public class Network {

    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean getConnectivityStatusString(Context context) {
        int conn = Network.getConnectivityStatus(context);
        Resources res = context.getResources();
        switch (conn)
        {
            case TYPE_WIFI:
            case TYPE_MOBILE:
                break;
            default:
                Toast.makeText(ApplicationContextProvider.getContext(), res.getText(R.string.NetworkProblem), Toast.LENGTH_LONG).show();
                return false;
        }
        return true;
    }
}

