package com.here.zuki.imhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * Created by zuki on 3/17/17.
 */

public class SearchActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public void startActivity(Intent intent)
    {
        super.startActivity(intent);
        overridePendingTransition(R.animator.slide_to_up, R.animator.slide_to_down);
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.animator.slide_to_down, R.animator.slide_to_up);
    }
}
