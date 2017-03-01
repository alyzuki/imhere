package com.here.zuki.imhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
    }


    public void goBack(View view)
    {
        Log.d("SETTINGS", "go back ...");
        finish();
        //super.onBackPressed();
    }

    public void goSearchOptions(View view)
    {
        onBackPressed();
    }

    public void goAddOptions(View view)
    {
        onBackPressed();
    }

    public void goSOSOptions(View view)
    {
        onBackPressed();
    }

    public void goTrackOptions(View view)
    {
        onBackPressed();
    }


}
