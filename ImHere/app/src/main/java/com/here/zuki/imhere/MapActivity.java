/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.here.zuki.imhere;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.here.zuki.imhere.Auth.Authcred;
import com.here.zuki.imhere.Utils.LoadBitmap;
import com.here.zuki.imhere.Utils.MainHandler;
import com.here.zuki.imhere.Utils.PlaceObject;
import com.here.zuki.imhere.Utils.PrefConfig;
import com.here.zuki.imhere.Utils.SessionManager;
import com.here.zuki.imhere.Utils.SharedObject;

import java.util.List;


/**
 * This shows how UI settings can be toggled.
 */
public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback , View.OnClickListener, CheckBox.OnCheckedChangeListener {

    private GoogleMap mMap;

    private UiSettings mUiSettings;

    private SharedObject sharedObject = SharedObject.getInstance();

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private int mOptsCheck = 0;
    private int mOptsLength = -1;
    private boolean bOptsCheck[];
    private CharSequence searchOpts[];
    private ImageButton btnOpt;
    private GoogleApiClient client;
    static Intent settingIntent = null;
    //static Config config;
    static PrefConfig pref;
    FloatingActionButton add = null;
    FloatingActionButton sos = null;
    SessionManager sessionManager = null;
    HorizontalScrollView profileView = null;
    private static boolean resetPic = true;

    private OurHandler handler = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        handler = new OurHandler(this, MapActivity.this);
        if(sessionManager == null)
            sessionManager = SessionManager.getInstance();

        sessionManager.checkLogin();

        Log.d("CREATE", "Init");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        pref = new PrefConfig(sessionManager.getLastLogin(), this.getApplicationContext());


        profileView = (HorizontalScrollView) findViewById(R.id.scrollview_profile);
        profileView.setVisibility(View.INVISIBLE);
        profileView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    View mainContainer = findViewById(R.id.profile_main_container);
                    if(event.getX() > mainContainer.getWidth()
                            || event.getY() > mainContainer.getHeight())
                        profileView.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        new LoadBitmap((FloatingActionButton)findViewById(R.id.fab_sos_btn), "sos.png").execute();

        loadConfig();

        //setting button click
        findViewById(R.id.FollowExpand).setOnClickListener(this);
        findViewById(R.id.SearchExpand).setOnClickListener(this);
        findViewById(R.id.AddExpand).setOnClickListener(this);
        findViewById(R.id.SOSExpand).setOnClickListener(this);
        findViewById(R.id.btn_profile_logout).setOnClickListener(this);
        findViewById(R.id.btn_profile_add_device).setOnClickListener(this);
        findViewById(R.id.btn_profile_add_place).setOnClickListener(this);
        findViewById(R.id.fab_add_btn).setOnClickListener(this);
        findViewById(R.id.fab_sos_btn).setOnClickListener(this);
        findViewById(R.id.search_string).setOnClickListener(this);
        findViewById(R.id.appMenu).setOnClickListener(this);

        //setting checkbox click
        ((CheckBox) findViewById(R.id.cb_profile_offline_using)).setOnCheckedChangeListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab_add_btn:
            case R.id.btn_profile_add_place:
                startAddPlace(v);
                break;
            case R.id.fab_sos_btn:
                break;
            case R.id.search_string:
                Intent intent = new Intent(MapActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_profile_logout:
                sessionManager.logoutUser();
                Authcred.getInstance().signOut();
                FirebaseUser user = sharedObject.getCurUser();
                user = null;
                sessionManager.checkLogin();
                profileView.setVisibility(View.INVISIBLE);
                break;
            case R.id.SearchExpand:
            case R.id.AddExpand:
            case R.id.FollowExpand:
            case R.id.SOSExpand:
                ((Expandable)v).toggle();
                break;
            case R.id.appMenu:
                profileView.setVisibility(View.VISIBLE);
                if(resetPic) {
                    resetPic = !createCircleBitmap(R.id.image_profile, null);
                }
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.cb_profile_offline_using:
                sessionManager.setOffline(isChecked);
                break;

        }
    }

    public void SearchOptClick(View v) {
        showStylesDialog();
    }


    /**
     * Returns whether the checkbox with the given id is checked.
     */
    private boolean isChecked(int id) {
        return ((CheckBox) findViewById(id)).isChecked();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
    }

    /**
     * Checks if the map is ready (which depends on whether the Google Play services APK is
     * available. This should be called prior to calling any methods on GoogleMap.
     */
    private boolean checkReady() {
        if (mMap == null) {
            //Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Shows a dialog listing the styles to choose from, and applies the selected
     * style when chosen.
     */
    private void showStylesDialog() {
        if (mOptsLength == -1) {
            mOptsLength = 4;
            bOptsCheck = new boolean[mOptsLength];
            searchOpts = new CharSequence[]{
                    getResources().getString(R.string.name_opt),
                    getResources().getString(R.string.phone_opt),
                    getResources().getString(R.string.email_opt),
                    getResources().getString(R.string.social_opt)
            };
            for (int i = 0; i < mOptsLength; i++) {
                if (bOptsCheck[i]) {
                    mOptsCheck = mOptsCheck | 1 << i;
                }
            }
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choice_option));
        builder.setMultiChoiceItems(searchOpts, bOptsCheck, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                if (isChecked) {
                    bOptsCheck[indexSelected] = true;
                    mOptsCheck = mOptsCheck | (1 << indexSelected);
                } else {
                    bOptsCheck[indexSelected] = false;
                    mOptsCheck = mOptsCheck & ~(1 << indexSelected);
                }
            }
        });
        builder.show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Map Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
        sessionManager.logoutUser();
    }

    @Override
    public  void onDestroy()
    {
        super.onDestroy();
        handler.setActivity(null);
        handler.close();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        handler.pause();
    }

    @Override
    public  void onResume(){
        super.onResume();
        handler.resume();
        Intent previousIntent = sharedObject.getCurIntent();
        if(previousIntent == null)
            return;
        String previousClass = previousIntent.getComponent().getClassName();
//        if(previousClass.equals(SettingsActivity.class.getCanonicalName())) {
//            add.setVisibility(pref.configGetBoolean(SettingsActivity.ADDSHOW, false) ? View.VISIBLE : View.INVISIBLE);
//            sos.setVisibility(pref.configGetBoolean(SettingsActivity.SOSSHOW, false) ? View.VISIBLE : View.INVISIBLE);
//        }else
        if(previousClass.equals(SearchActivity.class.getCanonicalName()))
        {
            PlaceObject place = sharedObject.getFoundPlace();
            if(place != null)
            {
                mMap.clear();
                LatLng latLng = new LatLng(place.getLat(), place.getLon());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                Marker currLocationMarker = mMap.addMarker(markerOptions);
                CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                mMap.animateCamera(cam);
            }
        }
    }



    public   void startAddPlace(View view)
    {
        Intent addaplace = new Intent(MapActivity.this, AddPlaceActivity.class);
        startActivity(addaplace);
    }

    private  boolean createCircleBitmap(int id, Bitmap bitmap)
    {
        ImageView image = (ImageView)findViewById(id);
        try {
            if (bitmap == null)
                bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        }catch (NullPointerException npe)
        {
            npe.printStackTrace();
            return false;
        }
        if(bitmap == null) {

            return false;
        }
        image.setImageBitmap(bitmap);
        RoundedBitmapDrawable roundedImageDrawable = createRoundedBitmapImageDrawableWithBorder(bitmap);
        image.setImageDrawable(roundedImageDrawable);
        return true;
    }
    private RoundedBitmapDrawable createRoundedBitmapImageDrawableWithBorder(Bitmap bitmap){
        int bitmapWidthImage = bitmap.getWidth();
        int bitmapHeightImage = bitmap.getHeight();
        int borderWidthHalfImage = 4;

        int bitmapRadiusImage = Math.min(bitmapWidthImage,bitmapHeightImage)/2;
        int bitmapSquareWidthImage = Math.min(bitmapWidthImage,bitmapHeightImage);
        int newBitmapSquareWidthImage = bitmapSquareWidthImage+borderWidthHalfImage;

        Bitmap roundedImageBitmap = Bitmap.createBitmap(newBitmapSquareWidthImage,newBitmapSquareWidthImage,Bitmap.Config.ARGB_8888);
        Canvas mcanvas = new Canvas(roundedImageBitmap);
        mcanvas.drawColor(Color.RED);
        int i = borderWidthHalfImage + bitmapSquareWidthImage - bitmapWidthImage;
        int j = borderWidthHalfImage + bitmapSquareWidthImage - bitmapHeightImage;

        mcanvas.drawBitmap(bitmap, i, j, null);

        Paint borderImagePaint = new Paint();
        borderImagePaint.setStyle(Paint.Style.STROKE);
        borderImagePaint.setStrokeWidth(borderWidthHalfImage*2);
        borderImagePaint.setColor(Color.GRAY);
        mcanvas.drawCircle(mcanvas.getWidth()/2, mcanvas.getWidth()/2, newBitmapSquareWidthImage/2, borderImagePaint);

        RoundedBitmapDrawable roundedImageBitmapDrawable = RoundedBitmapDrawableFactory.create( this.getResources(),roundedImageBitmap);
        roundedImageBitmapDrawable.setCornerRadius(bitmapRadiusImage);
        roundedImageBitmapDrawable.setAntiAlias(true);
        return roundedImageBitmapDrawable;
    }

    public static class OurHandler extends MainHandler {
        private Activity pActivity = null;
        private static OurHandler instance = null;


        public static final int WHAT_PROFILE          = 0;
        public static final int WHAT_PLACE            = 1;
        public static final int WHAT_TRACKER          = 2;
        public static final int WHAT_CAR              = 3;
        public static final int WHAT_SET_SEARCH       = 4;
        public static final int WHAT_SET_ADD          = 5;
        public static final int WHAT_SET_FOLLOW       = 6;
        public static final int WHAT_SET_SOS          = 7;
        public static final int WHAT_TMP1             = 8;
        public static final int WHAT_TMP2             = 9;
        public static final int WHAT_TMP3             = 10;
        public static final int WHAT_TMP4             = 11;

        public static final int ARG_PROFILE_NAME      = 0;
        public static final int ARG_PROFILE_PICTURE   = 1;

        public static final int ARG_FACE_PROFILE      = 0;
        public static final int ARG_GMAIL_PROFILE     = 1;



        public OurHandler(Context context) {
            super(context);
            instance = this;
        }

        protected OurHandler(Context context, Activity activity)
        {
            super(context);
            pActivity = activity;
            instance  = this;
        }

        protected void setActivity(Activity activity)
        {
            pActivity = activity;
        }

        public  Activity getActivity() { return  pActivity;}

        @Override
        public void sendMessage(Message msg) {
            handleMessage(msg);
        }

        @Override
        protected void processMessage(Message message) {
            final Activity activity = pActivity;
            if(activity == null)
                return;
            switch (message.what)
            {
                case WHAT_PROFILE:
                    switch (message.arg1)
                    {
                        case ARG_PROFILE_NAME:
                            String name = (String) message.obj;
                            ((TextView)pActivity.findViewById(R.id.tv_profile_account)).setText(name);
                            break;

                        case ARG_PROFILE_PICTURE:
                            String url = null;
                            String id = null;
                            resetPic = true;

                            if(message.obj instanceof String)
                                id = (String) message.obj;
                            else {
                                try {
                                    id = pContext.getString(R.string.identityPic);
                                    List<String> list = (List<String>) message.obj;
                                    if (!list.get(1).equals(pContext.getString(R.string.nonStr))) {
                                        id = list.get(1);
                                        String type = list.get(0);
                                        if (type.equals(pContext.getString(R.string.faceId)))
                                            url = "https://graph.facebook.com/" + id + "/picture?type=large";
                                        else if (type.equals(pContext.getString(R.string.googleId))) {

                                        }
                                    }
                                }catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                            new LoadBitmap((ImageView)pActivity.findViewById(R.id.image_profile), id, pContext.getString(R.string.identityPic)).execute(url);
                            break;
                    }
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }

        @Override
        protected boolean storeMessage(Message message) {
            return true;
        }

        @Override
        public Message obtainMessage(int whatcode, int arg1, Object obj) {
            Message msg = Message.obtain();
            msg.what = whatcode;
            msg.arg1 = arg1;
            msg.arg2 = -1;
            msg.obj = obj;
            return msg;
        }


        public Message obtainMessage(int whatcode, int arg1, int arg2, Object obj) {
            Message msg = Message.obtain();
            msg.what = whatcode;
            msg.arg1 = arg1;
            msg.arg2 = arg2;
            msg.obj = obj;
            return msg;
        }

        public static synchronized OurHandler getInstance()
        {
            return instance;
        }
    }

    private void updateUserInfo()
    {
        FirebaseUser user = sharedObject.getCurUser();
        if(user == null)
            return;
        String name = user.getDisplayName();
        pref.changeUser(name);

        //update config

        handler.pause();
        //update place
        //update car
        handler.resume();

    }


    private void loadConfig()
    {
        pref.changeUser(sessionManager.getLastLogin());
        ((CheckBox)findViewById(R.id.cb_profile_offline_using)).setChecked(pref.configGetBoolean(SETTINGS_USE_OFF, true));
        ((CheckBox)findViewById(R.id.cb_settings_notification)).setChecked(pref.configGetBoolean(SETTINGS_NOTIFY, true));
        ((EditText)findViewById(R.id.setting_distance)).setText(String.valueOf(pref.configGetFloat(SETTINGS_DISTANCE, (float) 1)));
        ((CheckBox)findViewById(R.id.setting_show_add)).setChecked(pref.configGetBoolean(SETTINGS_ADD_PLA, true));
        ((CheckBox)findViewById(R.id.setting_show_SOS)).setChecked(pref.configGetBoolean(SETTINGS_ADD_SOS, false));
    }

    public final String SETTINGS_NOTIFY     = "notification";
    public final String SETTINGS_ADD_PLA    = "showBtnAddPlace";
    public final String SETTINGS_DEV_CONT   = "ShowDeviceControl";
    public final String SETTINGS_USE_OFF    = "offlineAllow";
    public final String SETTINGS_DISTANCE   = "distances";
    public final String SETTINGS_ADD_SOS    = "showBtnSOS";
};
