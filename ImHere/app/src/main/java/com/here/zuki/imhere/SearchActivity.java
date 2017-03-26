package com.here.zuki.imhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.here.zuki.imhere.Adapter.PlaceAdapter;
import com.here.zuki.imhere.Utils.PlaceObject;

import java.util.ArrayList;

/**
 * Created by zuki on 3/17/17.
 */

public class SearchActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final ListView listView = (ListView)findViewById(R.id.list_search_result);

        ArrayList<PlaceObject> list = new PlaceObject().getPlaceTest(this, null);

        PlaceAdapter adapter = new PlaceAdapter(list, this);
        listView.setAdapter(adapter);
        View header = (View)getLayoutInflater().inflate(R.layout.search_header,  null);
        listView.addHeaderView(header);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaceObject object = (PlaceObject) listView.getItemAtPosition(position);
                Log.d("SEARCH", object.toString());
            }
        });
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
        //overridePendingTransition(R.animator.slide_to_down, R.animator.slide_to_up);
    }
}
