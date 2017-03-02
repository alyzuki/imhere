package com.here.zuki.imhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * Created by zuki on 3/2/17.
 */

public class SOSOptions extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_settings);
    }



}
