package com.here.zuki.imhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * Created by zuki on 3/1/17.
 */

public class SearchOptions extends AppCompatActivity {

    private  Common CMClass  = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_settings);
        try
        {
            if(CMClass == null)
                CMClass = Common.getInstance();
        }finally {
        }
        CheckBox checkBox = (CheckBox) findViewById(R.id.cb_notification);
        checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            Common.SearchOptElement searchOpts = CMClass.getSearchOptsClass();
                            Log.d("SEARCH_SETTINGS", Boolean.toString(searchOpts.isNotification()));
                            searchOpts.setNotification(isChecked);
                            Log.d("SEARCH_SETTINGS", Boolean.toString(searchOpts.isNotification()));
                        }
                        catch (Exception ex){
                            Log.d("SEARCH_SETTINGS", "fail checkbox");
                        }

                    }
                }
        );

        checkBox = (CheckBox) findViewById(R.id.opts_num_phone);
        checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            Common.SearchOptElement searchOpts = CMClass.getSearchOptsClass();
                            Log.d("PHONE_SETTINGS", Boolean.toString(searchOpts.isFollowPhone()));
                            searchOpts.setFollowPhone(isChecked);
                            Log.d("PHONE_SETTINGS", Boolean.toString(searchOpts.isFollowPhone()));
                        }
                        catch (Exception ex){
                            Log.d("PHONE_SETTINGS", "fail checkbox");
                        }
                    }
                }
        );
        checkBox = (CheckBox) findViewById(R.id.opts_name);
        checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            Common.SearchOptElement searchOpts = CMClass.getSearchOptsClass();
                            Log.d("NAME_SETTINGS", Boolean.toString(searchOpts.isFollowName()));
                            searchOpts.setFollowName(isChecked);
                            Log.d("NAME_SETTINGS", Boolean.toString(searchOpts.isFollowName()));
                        }
                        catch (Exception ex){
                            Log.d("NAME_SETTINGS", "fail checkbox");
                        }
                    }
                }
        );

        checkBox = (CheckBox) findViewById(R.id.opts_social_id);
        checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            Common.SearchOptElement searchOpts = CMClass.getSearchOptsClass();
                            Log.d("SOCIAL_SETTINGS", Boolean.toString(searchOpts.isFollowSocialId()));
                            searchOpts.setFollowSocialId(isChecked);
                            Log.d("SOCIAL_SETTINGS", Boolean.toString(searchOpts.isFollowSocialId()));
                        }
                        catch (Exception ex){
                            Log.d("SOCIAL_SETTINGS", "fail checkbox");
                        }
                    }
                }
        );
    }

    public  void goBack(View v)
    {
        Common.SearchOptElement searchOpts = CMClass.getSearchOptsClass();
        if(searchOpts.isNotification())
        {
            EditText disText = (EditText)findViewById(R.id.disText);
            String distance = disText.getText().toString();
            if(TextUtils.isEmpty(distance))
            {
                disText.setError("Distance is Empty");
            }
            else
            {
                try
                {
                    Float dis = Float.parseFloat(distance);
                    if(dis > 5.0)
                        disText.setError("Distance is so far");
                    else if(dis < 0)
                        disText.setError("Distance is not nagative number");
                    else
                    {
                        searchOpts.setDistance(dis);
                        disText.setError(null);
                    }
                }catch (NumberFormatException NuEx)
                {
                    disText.setError("Distance must be a float number");
                }
            }
            if(disText.getError() != null)
            {
                return;
            }
        }
        finish();
    }

}
