package com.here.zuki.imhere;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.Voice;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.here.zuki.imhere.Utils.ApplicationContextProvider;
import com.here.zuki.imhere.Utils.Common;
import com.here.zuki.imhere.Utils.Network;

import static com.here.zuki.imhere.R.*;

/**
 * Created by zuki on 4/13/17.
 */

public class SplashScreen extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(layout.splash_screen);

        /****** Create Thread that will sleep for 5 seconds *************/
        new Thread() {
            public void run() {

                try {
                    RunAnimation((TextView) findViewById(id.textSplashWellcome));
                    sleep(3 * Common.SECOND_RATE);
                    // After 5 seconds redirect to another intent
                    new SplashScreenExcute().execute();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    private void RunAnimation(TextView textView)
    {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        textView.startAnimation(anim);
    }

    private class  SplashScreenExcute extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void results)
        {
            try{
                Intent intent;
                if(Network.checkNetworkStatus(SplashScreen.this, false)) {
                    intent = new Intent(ApplicationContextProvider.getContext(), MapActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    //Remove activity
                    finish();
                }else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(SplashScreen.this)
                            .setTitle(getText(string.Network))
                            .setMessage(getString(string.NetworkProblem))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    ApplicationContextProvider.appClose();
                                }
                            }).show();
                }
            } catch (Exception e)

            {
                e.printStackTrace();
            }
        }
    }
}
