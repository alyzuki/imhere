package com.here.zuki.imhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.here.zuki.imhere.Utils.ApplicationContextProvider;

/**
 * Created by zuki on 4/12/17.
 */

public class ErrorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_layout);
        ((TextView) findViewById(R.id.errorMsgStr)).setText(getIntent().getStringExtra("MSG_ERROR"));
        findViewById(R.id.btnErrorOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ApplicationContextProvider.appClose();
            }
        });
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        lp.dimAmount = 0;
        lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout ll = (RelativeLayout) inflater.inflate(
                R.layout.error_layout, null);
        setContentView(ll, lp);
    }
}
