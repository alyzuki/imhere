package com.here.zuki.imhere;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.here.zuki.imhere.Adapter.PlaceAdapter;
import com.here.zuki.imhere.Utils.Common;
import com.here.zuki.imhere.Utils.EventItem;
import com.here.zuki.imhere.Utils.PlaceObject;
import com.here.zuki.imhere.Utils.SharedObject;

import java.util.ArrayList;

/**
 * Created by zuki on 3/30/17.
 */

public class CatalogueActivity extends AppCompatActivity {

    private SharedObject sharedObject = SharedObject.getInstance();
    private  ArrayList<EventItem> list = null;
    private  AdapterCatalogue adapter;
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
        if(sharedObject.getCatalogueType() == Common.CATALOGUE_EVENT)
        {
            createEventCatalogue();
        }
        else if(sharedObject.getCatalogueType() == Common.CATALOGUE_TIME)
        {
            createTimeCatalogue();
        }
        lview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventItem item = (EventItem) lview.getItemAtPosition(position);
                sharedObject.setCatalogueItem(item);
                ((EditText)findViewById(R.id.edit_catalogue_selected)).setText(item.getEventNane());
            }
        });

        Button btnCancel = (Button)findViewById(R.id.btn_catalogue_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedObject.setCatalogueItem(null);
                finish();
            }
        });

        Button btnApply = (Button)findViewById(R.id.btn_catalogue_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createEventCatalogue()
    {
        ((EditText)findViewById(R.id.edit_catalogue_selected)).setHint("Event");
        ((TextView)findViewById(R.id.tv_catalogue_name)).setText("Choose Catalogue");
        new EventItem().getCatalogueList(this.list, this);
    }

    private void createTimeCatalogue()
    {
        ((EditText)findViewById(R.id.edit_catalogue_selected)).setHint("Time");
        ((TextView)findViewById(R.id.tv_catalogue_name)).setText("Choose Time");

        this.list.add(new EventItem("5 minutes",        5));
        this.list.add(new EventItem("10 minutes",       10));
        this.list.add(new EventItem("15 minutes",       15));
        this.list.add(new EventItem("30 minutes",       20));
        this.list.add(new EventItem("45 minutes",       45));
        this.list.add(new EventItem("1 hour",           60));
        this.list.add(new EventItem("1 hour 30 minute", 90));
        this.list.add(new EventItem("2 hours",          120));
        this.list.add(new EventItem("3 hours",          180));
        this.list.add(new EventItem("4 hours",          240));
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

    private class AdapterCatalogue extends ArrayAdapter<EventItem> implements View.OnClickListener
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
                //set icon src
            }

            holder.tvName.setText(item.getEventNane());
            return convertView;
        }
    }
}
