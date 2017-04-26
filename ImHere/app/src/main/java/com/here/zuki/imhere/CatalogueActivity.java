package com.here.zuki.imhere;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.here.zuki.imhere.Utils.ApplicationContextProvider;
import com.here.zuki.imhere.Utils.Common;
import com.here.zuki.imhere.Utils.EventItem;
import com.here.zuki.imhere.Utils.LoadBitmap;
import com.here.zuki.imhere.Utils.Network;
import com.here.zuki.imhere.Utils.SharedObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
                EditText edit = ((EditText)findViewById(R.id.edit_catalogue_selected));
                edit.setText(selectedItem.getEventNane());
                ((ImageButton)findViewById(R.id.btn_catalogue_new)).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add));
                edit.setEnabled(false);
                edit.setClickable(false);
                edit.setFocusable(false);
                edit.setFocusableInTouchMode(false);
            }
        });

        findViewById(R.id.btn_catalogue_apply).setOnClickListener(this);
        findViewById(R.id.btn_catalogue_cancel).setOnClickListener(this);
        findViewById(R.id.btn_catalogue_new).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_catalogue_new)).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add));

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
                boolean isFocusable = name.isFocusable();
                if(isFocusable)
                {
                    String eventName = name.getText().toString();
                    if(eventName.isEmpty())
                    {
                        Toast.makeText(CatalogueActivity.this, getString(R.string.eventItemNull),Toast.LENGTH_LONG).show();
                        return;
                    }
                    new NewEvent(CatalogueActivity.this).execute(eventName);
                }
                else
                {
                    ((ImageButton)v).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.apply));
                }
                name.setEnabled(!isFocusable);
                name.setClickable(!isFocusable);
                name.setFocusable(!isFocusable);
                name.setFocusableInTouchMode(!isFocusable);
                break;
        }
    }

    private class NewEvent extends AsyncTask<String, Void, Boolean>
    {
        private static final String url ="";
        private ProgressDialog progressDialog;
        private Context pContext;
        private JSONObject jsonObject;


        public NewEvent(Context context)
        {
            super();
            this.pContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!Network.checkNetworkStatus(pContext, false))
                this.cancel(false);
            progressDialog = Network.newLoadingDialog(pContext);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try
            {
                if(params != null && params.length >= 1 && params[0] != null)
                {
                    InputStream is;
                    List<NameValuePair> request = new ArrayList<NameValuePair>();
                    request.add(new BasicNameValuePair("eventname", params[0]));
                    jsonObject = Network.makeHttpResponseToJSONObject(Network.getHttpConnection(this.url, "POST", request));

                }
            }
            catch (JSONException jsEx)
            {
                jsonObject = null;
                jsEx.printStackTrace();
            }
            catch (Exception ex)
            {
                jsonObject = null;
                ex.printStackTrace();
            }finally {
                return false;
            }
        }

        protected void onPostExecutes(boolean result)
        {
            try
            {
                int success = jsonObject.getInt(Common.TAG_SUCCESS);

                if (success == 1) {
                    Toast.makeText(pContext, pContext.getText(R.string.catalog_addSucc),Toast.LENGTH_LONG).show();
                    JSONArray events = jsonObject.getJSONArray("Events");
                    JSONObject object = events.getJSONObject(0);
                    selectedItem = EventItem.getItemFromJSONObj(object);
                    setResultValues();
                    return;
                }else
                {
                    String msg = jsonObject.getString("Message");
                    if(msg.equals("EXISTED"))
                        msg = getText(R.string.catalog_existed).toString();
                    new AlertDialog.Builder(this.pContext)
                            .setMessage(msg)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                    return;
                }
            }catch (JSONException jsEx)
            {
                jsEx.printStackTrace();
            }catch ( Exception Ex)
            {
                Ex.printStackTrace();
            }finally {
                new AlertDialog.Builder(this.pContext)
                        .setMessage(pContext.getString(R.string.catalog_neterr))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                return;
            }
        }
    }
}
