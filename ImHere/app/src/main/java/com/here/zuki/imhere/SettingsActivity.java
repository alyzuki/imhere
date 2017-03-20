package com.here.zuki.imhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.here.zuki.imhere.Utils.PrefConfig;

public class SettingsActivity extends AppCompatActivity {

    public  static final String LOGNAME        = "SETTINGS";
    public  static final String NOTIFY         = "closeNotify";
    public  static final String DISTANCE       = "closeDistance";
    public  static final String SEARCHNAME     = "sbyName";
    public  static final String SEARCHPHONE    = "sbyPhone";
    public  static final String SEARCHSOCIAL   = "sbySocialID";
    public  static final String ADDSHOW        = "AddShow";
    public  static final String SOSSHOW        = "SOSShow";
    public  static final String SOSBROADCAST   = "SOSBroadcast";
    //private  static final String NOTIFY = "closeNotify";
    //private  static final String NOTIFY = "closeNotify";

    PrefConfig pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        pref = PrefConfig.getInstance();

        final EditText distance = (EditText)findViewById(R.id.setting_distance);
        distance.setText((CharSequence)String.valueOf(pref.configGetFloat(DISTANCE, (float) 0.0)));
        distance.setEnabled(pref.configGetBoolean(NOTIFY, false));
        distance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (s.length() <= 0)
                    {
                        distance.setError("Distance must not be empty");
                        return;
                    }
                    float dis = Float.parseFloat(s.toString());
                    if(dis  >= (float)10)
                        distance.setError("Distance is so far");
                    else
                        pref.configSetValue(DISTANCE, dis);
                }catch (NumberFormatException nfe)
                {
                    Log.d(LOGNAME, nfe.toString());
                    distance.setError("Distance must be number");
                }
            }
        });

        CheckBox notify = (CheckBox)findViewById(R.id.settings_notification_btn);
        notify.setChecked(pref.configGetBoolean(NOTIFY, false));
        notify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.configSetValue(NOTIFY, isChecked);
                distance.setEnabled(isChecked);
            }
        });

        CheckBox name = (CheckBox)findViewById(R.id.opts_name);
        name.setChecked(pref.configGetBoolean(SEARCHNAME, false));
        name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.configSetValue(SEARCHNAME, isChecked);
            }
        });


        CheckBox phone = (CheckBox)findViewById(R.id.opts_num_phone);
        phone.setChecked(pref.configGetBoolean(SEARCHPHONE, false));
        phone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.configSetValue(SEARCHPHONE, isChecked);
            }
        });

        CheckBox socialId = (CheckBox)findViewById(R.id.opts_social_id);
        socialId.setChecked(pref.configGetBoolean(SEARCHSOCIAL, false));
        socialId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.configSetValue(SEARCHSOCIAL, isChecked);
            }
        });



        CheckBox showSOS = (CheckBox)findViewById(R.id.setting_show_SOS);
        showSOS.setChecked(pref.configGetBoolean(SOSSHOW, false));
        showSOS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.configSetValue(SOSSHOW, isChecked);
            }
        });

        CheckBox showAdd = (CheckBox)findViewById(R.id.setting_show_add);
        showAdd.setChecked(pref.configGetBoolean(ADDSHOW, false));
        showAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.configSetValue(ADDSHOW, isChecked);
            }
        });

        Log.d("SETTING", "user" + pref.getUser());

    }


    void expandable_toggle(View view)
    {
        ((Expandable)view).toggle();
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.animator.slide_from_left, R.animator.slide_to_right);
    }

    public void goBack(View view)
    {
        Log.d("SETTINGS", "go back ...");
        finish();
        //super.onBackPressed();
    }


    public void goSearchOptions(View view)
    {

       // Intent intent = new Intent(this, SearchOptions.class);
        //startActivity(intent);
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
