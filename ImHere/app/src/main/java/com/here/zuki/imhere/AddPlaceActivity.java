package com.here.zuki.imhere;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.here.zuki.imhere.Utils.Common;
import com.here.zuki.imhere.Utils.GPSTracker;

import java.util.ArrayList;

/**
 * Created by zuki on 3/9/17.
 */


public class AddPlaceActivity extends AppCompatActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private  int advanceHeigt = -1;
    private  final String strLicense = "<html><body><p align=\"justify\">Your place will be published on <b style=\"color:red;\">ImHere</b> where people or your friends could see. Your place is shared and limited time to see. You don't use this app for any purpose this is unlawful or prohibited, or any other purpose not reasonably intended by <b style=\"color:red;\">ImHere</b>. You must bear the full responsibility for your publication.</p></body></html>";

    GPSTracker gps = null;
    private UiSettings mUiSettings;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private GoogleApiClient client;

    ArrayList<Childitem> advanceChild = null;
    private Common cmm = new Common();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

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
        viewLicense.loadData(strLicense, "text/html", "utf-8");

        Button btnDiscard = (Button)findViewById(R.id.btn_add_discast);
        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClose(v);
            }
        });

        Button getLocation = (Button)findViewById(R.id.btn_get_curlocation);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gps  != null)
                {
                    if(!gps.canGetLocation())
                    {
                        gps.showSettingsAlert();
                    }else
                    {
                        Location loc = gps.getLocation();
                        if (loc != null) {
                            //place marker at current position
                            //mGoogleMap.clear();
                            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title("Current Position");
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                            Marker currLocationMarker = mMap.addMarker(markerOptions);
                        }
                    }
                }
                else
                {
                    Log.d("ADD PLACE", "GPS init fail.");
                }
            }
        });
    }

    public void addClose(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard the changes you made?");
        builder.setPositiveButton("KEEP EDITING", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
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

        /*mUiSettings = mMap.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setZoomControlsEnabled(isChecked(R.id.zoom_buttons_toggle));
        mUiSettings.setCompassEnabled(isChecked(R.id.compass_toggle));
        mUiSettings.setMyLocationButtonEnabled(isChecked(R.id.mylocationbutton_toggle));
        mMap.setMyLocationEnabled(isChecked(R.id.mylocationlayer_toggle));
        mUiSettings.setScrollGesturesEnabled(isChecked(R.id.scroll_toggle));
        mUiSettings.setZoomGesturesEnabled(isChecked(R.id.zoom_gestures_toggle));
        mUiSettings.setTiltGesturesEnabled(isChecked(R.id.tilt_toggle));
        mUiSettings.setRotateGesturesEnabled(isChecked(R.id.rotate_toggle));*/
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
    }


};
