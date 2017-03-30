package com.here.zuki.imhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.here.zuki.imhere.Adapter.EventAdapter;
import com.here.zuki.imhere.Adapter.PlaceAdapter;
import com.here.zuki.imhere.Utils.Common;
import com.here.zuki.imhere.Utils.EventItem;
import com.here.zuki.imhere.Utils.PlaceObject;
import com.here.zuki.imhere.Utils.SharedObject;

import java.util.ArrayList;

/**
 * Created by zuki on 3/17/17.
 */

public class SearchActivity extends AppCompatActivity {

    private SharedObject sharedObject = SharedObject.getInstance();
    private EventItem specItem = new EventItem("Tất cả", Common.EVENT_TYPE_ALL);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final ListView listView = (ListView) findViewById(R.id.list_search_result);

        ArrayList<PlaceObject> list = new PlaceObject().getPlaceTest(this, null);

        PlaceAdapter adapter = new PlaceAdapter(list, this);
        listView.setAdapter(adapter);
        View header = (View) getLayoutInflater().inflate(R.layout.search_header, null);
        listView.addHeaderView(header);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PlaceObject object = (PlaceObject) listView.getItemAtPosition(position);
                Log.d("SEARCH", object.toString());
                sharedObject.setFoundPlace(object);
            }
        });


        //final TextView searchObj = (TextView)findViewById(R.id.searchObjName);
        setEventList();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        sharedObject.setCurIntent(getIntent());
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        sharedObject.setCurIntent(intent);
        overridePendingTransition(R.animator.slide_to_up, R.animator.slide_to_down);
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.animator.slide_to_down, R.animator.slide_to_up);
    }

    private void setEventList() {
        final ArrayList<EventItem> listEvent = new EventItem().getListTest();
        final EventAdapter eventAdapter = new EventAdapter(listEvent, this);

        listEvent.add(0, specItem);
        eventAdapter.notifyDataSetChanged();

        final Spinner spinner = (Spinner) findViewById(R.id.spinner_event_list);
        spinner.setAdapter(eventAdapter);

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("====>Event List", "DOWN");
                    new EventItem().changeList(listEvent);
                    listEvent.add(0, specItem);
                    eventAdapter.notifyDataSetChanged();

                }
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EventItem item = (EventItem) spinner.getItemAtPosition(position);
                sharedObject.setFoundSpecialEvent(item);
                //searchObj.setText(item.getEventNane());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sharedObject.setFoundSpecialEvent(specItem);
            }
        });
    }

    @Override
    public  void onNewIntent(Intent intent) {
        sharedObject.setCurIntent(intent);
        super.onNewIntent(intent);
    }
}
