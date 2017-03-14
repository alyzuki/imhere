package com.here.zuki.imhere;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
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
import com.here.zuki.imhere.Utils.Common;

import java.util.ArrayList;

/**
 * Created by zuki on 3/9/17.
 */


public class AddPlaceActivity extends AppCompatActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private  int advanceHeigt = -1;
    private  final String strLicense = "<html><body><p align=\"justify\">Your place will be published on <b style=\"color:red;\">ImHere</b> where people or your friends could see. Your place is shared and limited time to see. You don't use this app for any purpose this is unlawful or prohibited, or any other purpose not reasonably intended by <b style=\"color:red;\">ImHere</b>. You must bear the full responsibility for your publication.</p></body></html>";

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

//    public void setZoomButtonsEnabled(View v) {
//        if (!checkReady()) {
//            return;
//        }
//        // Enables/disables the zoom controls (+/- buttons in the bottom-right of the map for LTR
//        // locale or bottom-left for RTL locale).
//        mUiSettings.setZoomControlsEnabled(((CheckBox) v).isChecked());
//    }
//
//    public void setCompassEnabled(View v) {
//        if (!checkReady()) {
//            return;
//        }
//        // Enables/disables the compass (icon in the top-left for LTR locale or top-right for RTL
//        // locale that indicates the orientation of the map).
//        mUiSettings.setCompassEnabled(((CheckBox) v).isChecked());
//    }
//
//    public void setMyLocationButtonEnabled(View v) {
//        if (!checkReady()) {
//            return;
//        }
//        // Enables/disables the my location button (this DOES NOT enable/disable the my location
//        // dot/chevron on the map). The my location button will never appear if the my location
//        // layer is not enabled.
//        // First verify that the location permission has been granted.
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mUiSettings.setMyLocationButtonEnabled(mMyLocationButtonCheckbox.isChecked());
//        } else {
//            // Uncheck the box and request missing location permission.
//            mMyLocationButtonCheckbox.setChecked(false);
//            requestLocationPermission(MY_LOCATION_PERMISSION_REQUEST_CODE);
//        }
//    }

//    public void setMyLocationLayerEnabled(View v) {
//        if (!checkReady()) {
//            return;
//        }
//        // Enables/disables the my location layer (i.e., the dot/chevron on the map). If enabled, it
//        // will also cause the my location button to show (if it is enabled); if disabled, the my
//        // location button will never show.
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(mMyLocationLayerCheckbox.isChecked());
//        } else {
//            // Uncheck the box and request missing location permission.
//            mMyLocationLayerCheckbox.setChecked(false);
//            PermissionUtils.requestPermission(this, LOCATION_LAYER_PERMISSION_REQUEST_CODE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, false);
//        }
//    }

//    public void setScrollGesturesEnabled(View v) {
//        if (!checkReady()) {
//            return;
//        }
//        // Enables/disables scroll gestures (i.e. panning the map).
//        mUiSettings.setScrollGesturesEnabled(((CheckBox) v).isChecked());
//    }

//    public void setZoomGesturesEnabled(View v) {
//        if (!checkReady()) {
//            return;
//        }
//        // Enables/disables zoom gestures (i.e., double tap, pinch & stretch).
//        mUiSettings.setZoomGesturesEnabled(((CheckBox) v).isChecked());
//    }

//    public void setTiltGesturesEnabled(View v) {
//        if (!checkReady()) {
//            return;
//        }
//        // Enables/disables tilt gestures.
//        mUiSettings.setTiltGesturesEnabled(((CheckBox) v).isChecked());
//    }

//    public void setRotateGesturesEnabled(View v) {
//        if (!checkReady()) {
//            return;
//        }
//        // Enables/disables rotate gestures.
//        mUiSettings.setRotateGesturesEnabled(((CheckBox) v).isChecked());
//    }

        /**
         * Requests the fine location permission. If a rationale with an additional explanation should
         * be shown to the user, displays a dialog that triggers the request.
         */
//    public void requestLocationPermission(int requestCode) {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)) {
//            // Display a dialog with rationale.
//            PermissionUtils.RationaleDialog
//                    .newInstance(requestCode, false).show(
//                    getSupportFragmentManager(), "dialog");
//        } else {
//            // Location permission has not been granted yet, request it.
//            PermissionUtils.requestPermission(this, requestCode,
//                    Manifest.permission.ACCESS_FINE_LOCATION, false);
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//            @NonNull int[] grantResults) {
//        if (requestCode == MY_LOCATION_PERMISSION_REQUEST_CODE) {
//            // Enable the My Location button if the permission has been granted.
//            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                mUiSettings.setMyLocationButtonEnabled(true);
//                mMyLocationButtonCheckbox.setChecked(true);
//            } else {
//                mLocationPermissionDenied = true;
//            }
//
//        } else if (requestCode == LOCATION_LAYER_PERMISSION_REQUEST_CODE) {
//            // Enable the My Location layer if the permission has been granted.
//            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                mMap.setMyLocationEnabled(true);
//                mMyLocationLayerCheckbox.setChecked(true);
//            } else {
//                mLocationPermissionDenied = true;
//            }
//        }
//    }

        //   @Override
        // protected void onResumeFragments() {
//        super.onResumeFragments();
//        if (mLocationPermissionDenied) {
//            PermissionUtils.PermissionDeniedDialog
//                    .newInstance(false).show(getSupportFragmentManager(), "dialog");
//            mLocationPermissionDenied = false;
//        }
        //}

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
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


};
