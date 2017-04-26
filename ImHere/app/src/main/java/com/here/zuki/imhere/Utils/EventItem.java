package com.here.zuki.imhere.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.here.zuki.imhere.Adapter.EventAdapter;
import com.here.zuki.imhere.CatalogueActivity;
import com.here.zuki.imhere.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by zuki on 3/26/17.
 */


public class EventItem {
    private int type;
    private int id;
    private String eventNane;
    private String iconSrc;

    public static final String load_catalogue_event = "http://bdssmart.net/databaseconnector/getEventCatalogue.php";
    public static final String test_catalogue_event = "http://bdssmart.net/databaseconnector/display.php";
    private static String TAG_EVENTS    =   "Events";
    private static String TAG_ID        =   "EventID";
    private static String TAG_TYPE      =   "EventType";
    private static String TAG_NAME      =   "EventName";
    private static String TAG_ICON      =   "EventIcon";

    public  EventItem(){}

    public EventItem(String eventNane, int  type)
    {
        this.eventNane = eventNane;
        this.type = type;
    }

    public EventItem(String eventNane, int  type, String iconSrc)
    {
        this.eventNane = eventNane;
        this.type = type;
        this.iconSrc = iconSrc;
    }

    public String getEventNane()
    {
        return  this.eventNane;
    }

    public void setEventNane(String name)
    {
        this.eventNane = name;
    }

    public int getType() { return  this.type;}

    public String getIconSrc() { return this.iconSrc; }

    public void setIconSrc(String iconSrc) { this.iconSrc = iconSrc; }

    public void setId(int id) { this.id = id; }

    public int getId() { return this.id; }

    public final ArrayList<EventItem> getListTest()
    {
        ArrayList<EventItem> list = new ArrayList<EventItem>();
        for(int i = 0; i < 10; i++)
        {
            EventItem item = new EventItem("Event " + String.valueOf(i), i % Common.EVENT_TYPE_COUNT);
            list.add(item);
        }
        return  list;
    }


    public final void changeList(ArrayList<EventItem> list)
    {
        list.clear();
        for(int i = 0; i < 10; i++)
        {
            EventItem item = new EventItem("Event change " + String.valueOf(i), i % Common.EVENT_TYPE_COUNT);
            list.add(item);
        }
    }



    public final void getCatalogueList(ArrayList<EventItem> list, Context context, CatalogueActivity.AdapterCatalogue adapter)
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("All", "false"));
        LoadCatalogue load = new LoadCatalogue(context, params, list, adapter);
        load.execute();
    }

    public static EventItem getItemFromJSONObj(JSONObject jsonObject)
    {
        EventItem item = null;
        if(jsonObject == null)
            return  null;
        item = new EventItem();
        try
        {
            item.id                 = jsonObject.getInt(TAG_ID);
            if(item.id <= 0) throw  new UnknownServiceException();

            item.type               = jsonObject.getInt(TAG_TYPE);
            item.iconSrc            = jsonObject.getString(TAG_ICON);
            item.eventNane          = jsonObject.getString(TAG_NAME);
        }catch (JSONException jsEx)
        {
            Log.e("EVENT", "GetEvents fail:\n" + jsEx.toString());
            item = null;
        }catch (UnknownServiceException usEx)
        {
            Log.e("EVENT", "GetEvents fail throws:\n");
            item = null;
        }
        finally {
            return item;
        }
    }



    class LoadCatalogue extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        private Context parentContext;
        private ArrayList<EventItem> eventList;
        private CatalogueActivity.AdapterCatalogue adapter;
        List<NameValuePair> params;


        public LoadCatalogue(Context context, List<NameValuePair> params, ArrayList<EventItem> list, CatalogueActivity.AdapterCatalogue adapter) {
            this.parentContext = context;
            this.eventList = list;
            this.params = params;
            this.adapter = adapter;
        }

        //before load

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            eventList.clear();
            pDialog = Network.newLoadingDialog(this.parentContext);
        }


        //loading
        @Override
        protected String doInBackground(String... vargs) {



            JSONObject json = null;
            // Check your log cat for JSON reponse
//            Log.d("==>All Event: ", json.toString());

            try {
                // getting JSON string from URL
                json = Network.makeHttpResponseToJSONObject(Network.getHttpConnection(load_catalogue_event, "GET", this.params));

                // Checking for SUCCESS TAG
                int success = json.getInt(Common.TAG_SUCCESS);

                if (success == 1) {
                    //Operation is success
                    JSONArray events = json.getJSONArray(TAG_EVENTS);
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject event = events.getJSONObject(i);
                        EventItem item = getItemFromJSONObj(event);
                        eventList.add(item);
                    }

                } else {
                    // no products found
                    // Launch Add New product Activity
//                    Intent i = new Intent(getApplicationContext(),
//                            NewProductActivity.class);
//                    // Closing all previous activities
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
                }
            }
            catch (IOException ioEx)
            {
                ioEx.printStackTrace();
            }
            catch (JSONException jsonEx) {
                jsonEx.printStackTrace();
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            adapter.notifyDataSetChanged();
            // updating UI from Background Thread
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    /**
//                     * Updating parsed JSON data into ListView
//                     * */
//                    ListAdapter adapter = new SimpleAdapter(
//                            AllProductsActivity.this, productsList,
//                            R.layout.list_item, new String[] { TAG_PID,
//                            TAG_NAME},
//                            new int[] { R.id.pid, R.id.name });
//                    // updating listview
//                    setListAdapter(adapter);
//                }
//            });
        }
    }
}
