package com.here.zuki.imhere;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.here.zuki.imhere.Utils.ApplicationContextProvider;
import com.here.zuki.imhere.Utils.Common;
import com.here.zuki.imhere.Utils.EventItem;
import com.here.zuki.imhere.Utils.GPSTracker;
import com.here.zuki.imhere.Utils.Network;
import com.here.zuki.imhere.Utils.PlaceObject;
import com.here.zuki.imhere.Utils.PrefConfig;
import com.here.zuki.imhere.Utils.SharedObject;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.here.zuki.imhere.Utils.Common.PREF_LANG;

/**
 * Created by zuki on 3/9/17.
 */


public class AddPlaceActivity extends AppCompatActivity implements
        OnMapReadyCallback {

    private final static String LOG         = "ADDPLACE";
    private final static String TAG_NAME    = "yrName";
    private final static String TAG_PHONE   = "yrPhone";
    private final static String TAG_MAIL    = "yrMail";


    private  final  float SECONDELAY = 3;
    private  static LatLng lastLL = new LatLng(0, 0);
    private  final Lock lock = new ReentrantLock();
    private  GoogleMap mMap;
    private  int advanceHeigt = -1;
    private  String strUrl = "http://bdssmart.net/%sLicense.html";

    GPSTracker gps = null;
    private UiSettings mUiSettings;
    private Handler UpdateLoc = new Handler();
    PrefConfig pref = null;

    private boolean isPaused = false;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private GoogleApiClient client;
    private SharedObject sharedObject = SharedObject.getInstance();
    private PlaceObject place;

    ArrayList<Childitem> advanceChild = null;
    private Common cmm = new Common();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);


        sharedObject.setCurIntent(getIntent());
        place = new PlaceObject();
        pref = PrefConfig.getInstance();


        if(!Network.getConnectivityStatusString(this))
        {
            sharedObject.getCurIntent();
            sharedObject.setCurIntent(null);
            finish();
            return;
        }
        Log.d("CREATE", "Init");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        gps = new GPSTracker(AddPlaceActivity.this);

        final Button btnAdvance = (Button)findViewById(R.id.btn_add_advance);
        btnAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout controler = (LinearLayout)findViewById(R.id.advance_controler);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)controler.getLayoutParams();
                params.height = 0;
                controler.setLayoutParams(params);
                controler.requestLayout();
                cmm.setHeightOfChild(findViewById(R.id.add_advance_container), advanceChild, 1);
            }
        });
        final LinearLayout layout = (LinearLayout) findViewById(R.id.add_advance_container);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if(advanceChild == null) {
                    advanceChild = new ArrayList<Childitem>();
                    cmm.setHeightOfChild(layout, advanceChild, -1);
                }
                ColorDrawable viewColor = (ColorDrawable)layout.getRootView().getBackground();
                findViewById(R.id.webLicense).setBackgroundColor(viewColor.getColor());
            }
        });

        WebView viewLicense = (WebView)findViewById(R.id.webLicense);
        viewLicense.getSettings().setJavaScriptEnabled(true);
        viewLicense.loadUrl(String.format(strUrl, pref.configGetString(Common.PREF_LANG, "en") ));

        Button btnDiscard = (Button)findViewById(R.id.btn_add_discast);
        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClose(v);
            }
        });

        Button btnAddPlace = (Button)findViewById(R.id.btn_add_apply);

        btnAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlace(v);
            }
        });


        EditText tvCatalogue = (EditText)findViewById(R.id.ed_add_place_catalogue);
        tvCatalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedObject.setCatalogueType(Common.CATALOGUE_EVENT);
                Intent intent = new Intent(AddPlaceActivity.this,  CatalogueActivity.class);
                startActivity(intent);
            }
        });

        EditText tvTime = (EditText)findViewById(R.id.ed_add_place_timelapse);
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedObject.setCatalogueType(Common.CATALOGUE_TIME);
                Intent intent = new Intent(AddPlaceActivity.this,  CatalogueActivity.class);
                startActivity(intent);
            }
        });

        Button getLocation = (Button)findViewById(R.id.btn_get_curlocation);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateLocation();
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                UpdateLocation();
            }
        };
        UpdateLoc.postDelayed(runnable, (int)(SECONDELAY * Common.SECOND_RATE));

        checkboxActivate(findViewById(R.id.cb_find_by_name));
        checkboxActivate(findViewById(R.id.cb_find_by_phone));
        checkboxActivate(findViewById(R.id.cb_find_by_social));

        //EditText
        //editTextSetEvent((EditText)findViewById(R.id.edit_yr_name));

        pref.configSetValue(PREF_LANG, "vi");
        TextView tvloginAccount = (TextView)findViewById(R.id.tv_login_account);
        tvloginAccount.setText(pref.getUser());

        EditText edit = (EditText)findViewById(R.id.edit_yr_name);
        edit.setText(pref.configGetString(this.TAG_NAME, ""));
        edit = (EditText)findViewById(R.id.edit_yr_phone);
        edit.setText(pref.configGetString(TAG_PHONE, ""));
        edit = (EditText)findViewById(R.id.edit_yr_email);
        edit.setText(pref.configGetString(TAG_MAIL, ""));
        pref.configGetBoolean("AddShow", false);
        //new BitmapUrlUtils().removeFile();
    }


    public  void checkboxActivate(View view)
    {
        CheckBox checkBox = (CheckBox)view;
        boolean checked = checkBox.isChecked();
        switch (view.getId())
        {
            case R.id.cb_find_by_name:
                findViewById(R.id.edit_yr_name).setEnabled(checked);
                break;
            case R.id.cb_find_by_phone:
                findViewById(R.id.edit_yr_phone).setEnabled(checked);
                break;
            case R.id.cb_find_by_mail:
                findViewById(R.id.edit_yr_email).setEnabled(checked);
                break;
        }

    }

    private  void UpdateLocation()
    {
        lock.lock();
        try {
            if (gps != null) {
                if (!gps.canGetLocation()) {
                    gps.showSettingsAlert();
                } else {
                    Location loc = gps.getLocation();
                    if (loc != null) {
                        //place marker at current position
                        //mGoogleMap.clear();
                        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                        if(!latLng.equals(lastLL)){
                            mMap.clear();
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title("Current Position");
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                            Marker currLocationMarker = mMap.addMarker(markerOptions);
                            CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                            mMap.animateCamera(cam);
                            lastLL = latLng;
                        }
                    }
                }
            } else {
                Log.d("ADD PLACE", "GPS init fail.");
            }
        }catch (Exception IEx)
        {
            IEx.printStackTrace();;
        }finally {
            lock.unlock();
        }
    }

    private void addPlace(View view)
    {
        if(!Network.getConnectivityStatusString(this))
        {
            return;
        }
        EditText placeName = (EditText)findViewById(R.id.edPlaceName);
        EditText eventName = (EditText)findViewById(R.id.ed_add_place_catalogue);
        EditText timeLapse = (EditText)findViewById(R.id.ed_add_place_timelapse);
        EditText yrName    = (EditText)findViewById(R.id.edit_yr_name);
        EditText yrPhone   = (EditText)findViewById(R.id.edit_yr_phone);
        EditText yrMail    = (EditText)findViewById(R.id.edit_yr_email);
        CheckBox cyrName   = (CheckBox)findViewById(R.id.cb_find_by_name);
        CheckBox cyrPhone  = (CheckBox)findViewById(R.id.cb_find_by_phone);
        CheckBox cyrMail   = (CheckBox)findViewById(R.id.cb_find_by_mail);
        CheckBox cyrSocial = (CheckBox)findViewById(R.id.cb_find_by_social);
        CheckBox callowUp  = (CheckBox)findViewById(R.id.cb_update_moving);
        TextView socName   = (TextView)findViewById(R.id.tvReSocial);

        pref.configSetValue(TAG_NAME, yrName.getText().toString());
        pref.configSetValue(TAG_PHONE, yrPhone.getText().toString());
        pref.configSetValue(TAG_MAIL, yrMail.getText().toString());

        if(!checkEditText(placeName, getText(R.string.anp_msg_name_place_missing).toString()))
            return;
        if(!checkEditText(eventName, getText(R.string.anp_msg_cataloge_missing).toString()))
            return;
        if(!checkEditText(timeLapse, getText(R.string.anp_msg_time_missing).toString()))
            return;
        if(cyrName.isChecked() &&
            !checkEditText(yrName, getText(R.string.anp_msg_yr_name_missing).toString()))
            return;
        if(cyrPhone.isChecked() &&
            !checkEditText(yrPhone, getText(R.string.anp_msg_yr_phone_missing).toString()))
            return;
        if(cyrMail.isChecked() &&
                !checkEditText(yrMail, getText(R.string.anp_msg_yr_email_missing).toString()))
            return;
        try {
            place.setPlaceName(placeName.getText().toString());
            if(cyrName.isChecked())
                place.setReporterName(yrName.getText().toString());
            if(cyrPhone.isChecked())
                place.setReporterPhone(yrPhone.getText().toString());
            if(cyrMail.isChecked())
                place.setReporterMail(yrMail.getText().toString());
            if(cyrSocial.isChecked())
                place.setReporterAccount(socName.getText().toString());
            //add this place to database using method post
            if(callowUp.isChecked())
            {
                //create a new service to update your place
            }
            if(!lastLL.equals(null))
            {
                place.setLat(lastLL.latitude);
                place.setLon(lastLL.longitude);
            }
            place.addPlace(this);
        }catch (Exception ex)
        {
            Log.d(LOG, "Add new place processing fail: " + ex.toString());
            ex.printStackTrace();
        }
    }

    private boolean checkEditText(EditText editText, String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaceActivity.this);
        AlertDialog dialog = builder.create();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }
        );
        String str = editText.getText().toString();
        if(str == null || str.isEmpty())
        {
            builder.setMessage(msg);
            builder.show();
            return  false;
        }
        return true;
    }

    private void addClose(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getText(R.string.anp_msg_discard).toString());
        builder.setPositiveButton(getText(R.string.anp_msg_discard_ok).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(getText(R.string.anp_msg_discard_cancel).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        builder.show();
    }

    /**
     * Returns whether the checkbox with the given id is checked.
     */

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.getUiSettings().setMapToolbarEnabled(false);
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
        if(gps  != null)
            gps.stopUsingGPS();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(!isPaused)
            return;

        isPaused = false;
        Intent intent = sharedObject.getCurIntent();
        if(intent == null) return;

        String previousClass = intent.getComponent().getClassName();
        if(previousClass.equals(CatalogueActivity.class.getCanonicalName())) {
            EventItem item = sharedObject.getCatalogueItem();
            if (item != null) {
                Toast.makeText(AddPlaceActivity.this, item.getEventNane(), Toast.LENGTH_LONG).show();
                if(sharedObject.getCatalogueType() == Common.CATALOGUE_TIME)
                {
                    place.setTimeLapse(item.getType());
                    ((EditText)findViewById(R.id.ed_add_place_timelapse)).setText(item.getEventNane());
                }else
                {
                    ((EditText)findViewById(R.id.ed_add_place_catalogue)).setText(item.getEventNane());
                    place.setEventName(item.getEventNane());
                    place.setTimeLapse(item.getType());
                }
            }
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        isPaused = true;

    }

    private void updateUserInformation(View object)
    {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(ApplicationContextProvider.setLocale());
    }

};
