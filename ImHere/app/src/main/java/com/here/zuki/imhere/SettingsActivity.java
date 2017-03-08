package com.here.zuki.imhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
//        final Expandable searchExpand = (Expandable)findViewById(R.id.SearchExpand);
//        searchExpand.setOnClickListener(
//            new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    searchExpand.toggle();
//                }
//            }
//        );
//
//        final Expandable addExpand = (Expandable)findViewById(R.id.AddExpand);
//        addExpand.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        addExpand.toggle();
//                    }
//                }
//        );
    }


    void expandable_toggle(View view)
    {
        ((Expandable)view).toggle();
    }

    public void goBack(View view)
    {
        Log.d("SETTINGS", "go back ...");
        finish();
        //super.onBackPressed();
    }

    public void goSearchOptions(View view)
    {

        Intent intent = new Intent(this, SearchOptions.class);
        startActivity(intent);
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
