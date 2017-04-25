package com.here.zuki.imhere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.here.zuki.imhere.Utils.ApplicationContextProvider;
import com.here.zuki.imhere.Utils.Common;
import com.here.zuki.imhere.Utils.EventItem;
import com.here.zuki.imhere.Utils.LoadBitmap;
import com.here.zuki.imhere.Utils.SharedObject;

import java.util.ArrayList;

/**
 * Created by zuki on 3/30/17.
 */

public class CatalogueActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedObject sharedObject = SharedObject.getInstance();
    private  ArrayList<EventItem> list = null;
    private  AdapterCatalogue adapter;
    private  int requestCode;

    private static final String TAG = ":::CATALOG:::";
    private EventItem selectedItem = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        sharedObject.setCurIntent(getIntent());

        EditText selected = (EditText)findViewById(R.id.edit_catalogue_selected);
        selected.setClickable(false);
        selected.setFocusable(false);

        final ListView lview = (ListView)findViewById(R.id.lv_catalogueItem);
        this.list = new ArrayList<EventItem>();
        adapter = new AdapterCatalogue(list, this);
        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (EventItem) lview.getItemAtPosition(position);
                ((EditText)findViewById(R.id.edit_catalogue_selected)).setText(selectedItem.getEventNane());
            }
        });

        findViewById(R.id.btn_catalogue_apply).setOnClickListener(this);
        findViewById(R.id.btn_catalogue_cancel).setOnClickListener(this);
        findViewById(R.id.btn_catalogue_new).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_catalogue_new)).setImageBitmap(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_input_add));

        requestCode = getIntent().getIntExtra(AddPlaceActivity.REQUEST, -1);
        if(requestCode == Common.CATALOGUE_EVENT)
            createEventCatalogue();
        else if(requestCode == Common.CATALOGUE_TIME )
            createTimeCatalogue();
        else {
            Log.d(TAG, "Request code invalid");
            finish();
        }
        lview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setResultValues()
    {
        Intent parent = new Intent();
        String name = selectedItem != null ? selectedItem.getEventNane() : null;
        parent.putExtra(AddPlaceActivity.RES_NAME, name);
        int value = selectedItem != null ? selectedItem.getType() : -1;
        parent.putExtra(AddPlaceActivity.RES_VAL, value);
        if(selectedItem == null)
            setResult(Activity.RESULT_CANCELED, parent);
        else
            setResult(Activity.RESULT_OK, parent);
        finish();
    }

    private void createEventCatalogue()
    {
        findViewById(R.id.btn_catalogue_new).setVisibility(View.VISIBLE);
        ((EditText)findViewById(R.id.edit_catalogue_selected)).setHint(R.string.catalog_choose_event_hint);
        ((TextView)findViewById(R.id.tv_catalogue_name)).setText(R.string.catalog_choose_event);
        new EventItem().getCatalogueList(this.list, this, adapter);
    }

    private void createTimeCatalogue()
    {
        ((EditText)findViewById(R.id.edit_catalogue_selected)).setHint(R.string.catalog_choose_time_hint);
        ((TextView)findViewById(R.id.tv_catalogue_name)).setText(R.string.catalog_choose_time);
        findViewById(R.id.btn_catalogue_new).setVisibility(View.INVISIBLE);

        this.list.add(new EventItem("5 " + getText(R.string.minutes),        5));
        this.list.add(new EventItem("10 " + getText(R.string.minutes),       10));
        this.list.add(new EventItem("15 " + getText(R.string.minutes),       15));
        this.list.add(new EventItem("30 " + getText(R.string.minutes),       20));
        this.list.add(new EventItem("45 " + getText(R.string.minutes),       45));
        this.list.add(new EventItem("1 " + getText(R.string.hour),           60));
        this.list.add(new EventItem("1 " + getText(R.string.hour) + " 30 " + getText(R.string.minutes), 90));
        this.list.add(new EventItem("2 " + getText(R.string.hours),          120));
        this.list.add(new EventItem("3 " + getText(R.string.hours),          180));
        this.list.add(new EventItem("4 " + getText(R.string.hours),          240));
    }



    public final class CatalogueItem
    {
        private int Value;
        private String Name;
        private String Icon;
        public CatalogueItem(){}

        public CatalogueItem(String Name, int value)
        {
            this.Name = Name;
            this.Icon = null;
            this.Value = value;
        }

        public CatalogueItem(String Name, int value, String iconSrc)
        {
            this.Name = Name;
            this.Icon = iconSrc;
            this.Value = value;
        }

        public int getValue() {return this.Value; }
        public String getName() { return this.Name; }
        public String getIcon() { return this.Icon; }

    }

    public class AdapterCatalogue extends ArrayAdapter<EventItem> implements View.OnClickListener
    {

        ArrayList<EventItem> dataSet;
        Context context;

        private class Items
        {
            ImageButton         btnIcon;
            TextView            tvName;
        }

        public AdapterCatalogue(ArrayList<EventItem> data, Context context)
        {
            super(context, R.layout.event_item,  data);
            this.context = context;
            this.dataSet = data;
        }

        @Override
        public void onClick(View v) {

        }
        @SuppressWarnings("ResourceType")
        @Override
        public  View getView(int pos, View convertView, ViewGroup parent) {

            EventItem item = getItem(pos);
            // Check if an existing view is being reused, otherwise inflate the view
            Items holder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {
                holder = new Items();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.event_item, parent, false); //set new layout

                holder.tvName       = (TextView) convertView.findViewById(R.id.tvEventItemName);
                holder.btnIcon      = (ImageButton) convertView.findViewById(R.id.eventIcon);
                result=convertView;
                convertView.setTag(holder);
            } else {
                holder = (Items) convertView.getTag();
                result=convertView;
            }
            if(sharedObject.getCatalogueType() ==  Common.CATALOGUE_TIME)
            {
                LinearLayout ll = (LinearLayout)convertView.findViewById(R.id.ll_icon_container);
                ll.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                ll.requestLayout();
            }else {
                holder.btnIcon.setFocusable(false);
                holder.btnIcon.setClickable(false);
                new LoadBitmap(holder.btnIcon, item.getIconSrc()).execute();
                //set icon src
            }

            holder.tvName.setText(item.getEventNane());

            return convertView;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(ApplicationContextProvider.setLocale());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_catalogue_cancel:
                selectedItem = null;
            case R.id.btn_catalogue_apply:
                setResultValues();
                break;
            case R.id.btn_catalogue_new:
                EditText name = (EditText)findViewById(R.id.edit_catalogue_selected);
                if(name.isFocusable())
                {
                    //check name was existed
                    name.setFocusable(false);
                    ((ImageButton)v).setImageBitmap(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_input_add));
                }
                else
                {
                    name.setFocusable(true);
                    ((ImageButton)v).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.apply));
                }
                break;
        }
    }
}
