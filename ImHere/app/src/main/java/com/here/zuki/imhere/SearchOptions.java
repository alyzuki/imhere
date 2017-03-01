package com.here.zuki.imhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;

/**
 * Created by zuki on 3/1/17.
 */

public class SearchOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_settings);
    }

    public  void cb_notification_click()
    {
        Common.SearchOptElement searchOpts = new Common.SearchOptElement();
        searchOpts.setNotification(((CheckBox)findViewById(R.id.cb_notification)).isActivated());
        Log.d("SEARCH_SETTINGS", Boolean.toString(searchOpts.isNotification()));
    }

    public  void goBack(View v)
    {
        finish();
    }

}
