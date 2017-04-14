package com.here.zuki.imhere.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.here.zuki.imhere.Childitem;
import com.here.zuki.imhere.R;

import java.util.ArrayList;

/**
 * Created by zuki on 3/10/17.
 */

public class Common extends AppCompatActivity{


    public final static int SECOND_RATE = 1000;
    public final static int TIMEOUT_SECOND = 10;
    public final static String TAG_SUCCESS = "success";
    public final static String PREF_LANG = "lang";

    public final static int ATTRS_NAME = 1;
    public final static int ATTRS_PHONE = 2;
    public final static int ATTRS_MAIL = 4;
    public final static int ATTRS_SOCIAL = 8;

    public final static int EVTYPE_SEARCH = 1;
    public final static int EVTYPE_CATALOGUE_STANDARD = 2;
    public final static int EVTYPE_CATALOGUE_SEARCH = 4;

    public final static int EVENT_TYPE_ALL = -1;
    public final static int EVENT_TYPE_OTHER = 0;
    public final static int EVENT_TYPE_COUNT = 4;

    public final static int EVENT_ADAPTER_ADD = 0;
    public final static int EVENT_ADAPTER_GET = 1;

    public final static int CATALOGUE_EVENT = 0;
    public final static int CATALOGUE_TIME = 1;

    public static final String datatbaseUrl = "http://bdssmart.net/databaseconnector/";




    public void setHeightOfChild(View parent, ArrayList<Childitem> childitems, int height)
    {
        if(childitems == null)
            return;
        if(parent instanceof ViewGroup)
        {
            int count = ((ViewGroup) parent).getChildCount();
            for(int i = 0; i < count; i++)
            {
                int heightSet = 0;
                View child = ((ViewGroup) parent).getChildAt(i);
                if(child  instanceof ViewGroup) {
                    setHeightOfChild(child, childitems, height);
                }

                if(child.getId() == -1)
                    continue;

                switch (height)
                {
                    case -1:
                        Childitem item = new Childitem(child.getId(), child.getWidth(), child.getHeight());
                        childitems.add(item);
                        break;
                    case  0:
                        break;
                    default:
                        for (Childitem items : childitems )
                        {
                            if(child.getId() == items.getID())
                                heightSet = items.getHeight();
                        }
                        break;
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)child.getLayoutParams();
                params.height = heightSet;
                child.setLayoutParams(params);
                child.requestLayout();
                Log.d("COMMON", child.toString() + "\n" + String.valueOf(heightSet));
            }
        }
        parent.requestLayout();
    }

   public boolean networkIsConnected(Activity activity){
       ConnectivityManager connMgr = (ConnectivityManager) getSystemService(activity.CONNECTIVITY_SERVICE);
       NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
       if (networkInfo != null && networkInfo.isConnected())
           return true;
       else
       {
           Toast.makeText(ApplicationContextProvider.getContext(), getText(R.string.NetworkProblem), Toast.LENGTH_LONG).show();
           return false;
       }

   }

    public static void appSleep(int seconds)
    {
        try
        {
            Thread.sleep(seconds * SECOND_RATE);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
