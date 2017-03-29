package com.here.zuki.imhere.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zuki on 3/22/17.
 */

public class PlaceObject {

    private String      placeName;
    private String      eventName;
    private double      lat;
    private double      lon;
    private long        curTime;
    private int         timeLapse;
    private int         userId;
    private int         attrsFind;
    private int         ID;
    private int         EvID;
    private String      ReporterName;
    private String      ReporterPhone;
    private String      ReporterSocial;
    private String      ReporterMail;


    public static final String TAG_PLACE            = "Name";
    public static final String TAG_EVNAME           = "EvName";
    public static final String TAG_LAT              = "Lat";
    public static final String TAG_LON              = "Lon";
    public static final String TAG_TIME             = "Time";
    public static final String TAG_TIMELAPSE        = "TimeLapse";
    public static final String TAG_REPORTERNAME     = "ReName";
    public static final String TAG_REPORTERPHONE    = "RePhone";
    public static final String TAG_REPORTERSOCIAL   = "ReSocial";
    public static final String TAG_ID               = "ID";
    public static final String TAG_EVID             = "EvID";
    public static final String TAG_PLACES           = "Places";


    public static final String load_place_url       = "http://bdssmart.net/databaseconnector/getPlace.php";


    enum PLACE_OPERATOR
    {
        PLACE_ADD,
        PLACE_UPDATE,
        PLACE_LOAD,
        PLACE_DELETE
    };


    public PlaceObject(){}

    public PlaceObject(String placeName, String eventName, double lat, double lon, int timeLapse, int userId, int attrsFind)
    {
        super();
        this.placeName = placeName;
        this.eventName = eventName;
        this.lat = lat;
        this.lon = lon;
        this.timeLapse = timeLapse;
        this.userId = userId;
        this.attrsFind = attrsFind;
    }


    public void setPlaceName(String name)    { this.placeName = name; }

    public  String getPlaceName()   { return this.placeName; }

    public void setEventName(String name)   { this.eventName = name;}

    public String getEventName()    { return this.eventName; }

    public void setLat(double lat)  {this.lat = lat;}

    public  double getLat() {return this.lat;}

    public void  setLon(double lon)     {   this.lon = lon; }

    public double getLon()  { return  this.lon; }

    public int getTimeLapse()
    {

        if(this.timeLapse == -1)
            return this.timeLapse;
        long now = System.currentTimeMillis();
        long newTimeLapse = now - (this.curTime + this.timeLapse * Common.SECOND_RATE);
        if( newTimeLapse <= 0)
            return 0;
        return (int)(newTimeLapse / Common.SECOND_RATE);
    }

    public int getAttrs()   { return  this.attrsFind; }

    public  void setAttrs(int attrs)
    {
        this.attrsFind = this.attrsFind & attrs;
    }

    public void setEvID(int id) { this.EvID = id; };

    public int getEvID()    { return this.EvID; }

    public void  setUserId(int id)  { this.userId = id; }

    public String getReporterName()     { return this.ReporterName; }

    public String getReporterSocial()   { return this.ReporterSocial; }

    public String getReporterPhone()    { return  this.ReporterPhone; }

    public String getReporterMail()     { return this.ReporterMail;}

    private PlaceObject getPlaceFromJSONObj(JSONObject jsObj)
    {
        if(jsObj == null)
            return  null;
        PlaceObject place = new PlaceObject();
        try
        {
            place.ID                = jsObj.getInt(TAG_ID);
            if(place.ID <= 0) throw  new UnknownServiceException();
            place.timeLapse         = jsObj.getInt(TAG_TIMELAPSE);
            place.curTime           = jsObj.getLong(TAG_TIME);
            if(getTimeLapse() == 0)
            {
                throw new UnknownServiceException();
            }
            place.lat               = jsObj.getDouble(TAG_LAT);
            place.lon               = jsObj.getDouble(TAG_LON);
            place.placeName         = jsObj.getString(TAG_PLACE);
            if(place.placeName == null) throw  new UnknownServiceException();
            place.eventName         = jsObj.getString(TAG_EVNAME);
            if(place.eventName == null) throw  new UnknownServiceException();
            place.ReporterName      = jsObj.getString(TAG_REPORTERNAME);
            place.ReporterPhone     = jsObj.getString(TAG_REPORTERPHONE);
            place.ReporterSocial    = jsObj.getString(TAG_REPORTERSOCIAL);
            place.EvID              = jsObj.getInt(TAG_EVID);

        }catch (JSONException jsEx)
        {
            Log.e("PLACE", "GetObject fail:\n" + jsEx.toString());
            place = null;
        }catch (UnknownServiceException usEx)
        {
            Log.e("PLACE", "GetObject fail throws:\n");
            place = null;
        }
        finally {
            return place;
        }
    }


    public void updatePlace(double lat, double lon)
    {
        setLat(lat);
        setLon(lon);
        updatePlace();
    }

    public void updatePlace()
    {

    }


    public final ArrayList<HashMap<String,PlaceObject>> getPlaces(Context contex, List<NameValuePair> params)
    {
        ArrayList<HashMap<String,PlaceObject>> placeList = new ArrayList<HashMap<String,PlaceObject>>();
        new LoadPlaces(contex, PLACE_OPERATOR.PLACE_LOAD, params, placeList).execute();
        return  placeList;
    }


    public final ArrayList<PlaceObject> getPlaceTest(Context context, List<NameValuePair> params)
    {
        ArrayList<PlaceObject> placeList = new ArrayList< PlaceObject>();

        for (int i = 0; i < 100; i++)
        {
            PlaceObject obj = new PlaceObject(
                    "placeName " + String.valueOf(i),
                    "EventName " + String.valueOf(i),
                    10.8232932 + i / 1000 , 106.7236943 + i / 1000,360, 1, 0);
            placeList.add(obj);
        }

        return placeList;
    }


    class LoadPlaces extends AsyncTask<String, String, String>
    {

        private ProgressDialog pDialog;
        private Context parentContext;
        private ArrayList<HashMap<String,PlaceObject>> placeList;
        private PLACE_OPERATOR op;
        List<NameValuePair> params;
        JSONParser jsonParser;
        public  LoadPlaces(Context context, PLACE_OPERATOR op, List<NameValuePair> params, ArrayList<HashMap<String,PlaceObject>> list)
        {
            this.parentContext = context;
            this.placeList = list;
            this.op = op;
            this.params = params;
             jsonParser = new JSONParser();
        }

        //before load

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(this.parentContext);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        //loading
        @Override
        protected String doInBackground(String... vargs) {


            // getting JSON string from URL
            JSONObject json = null;
            switch (this.op)
            {
                case PLACE_ADD:
                    break;
                case PLACE_LOAD:
                    json = jsonParser.makeHttpRequest(load_place_url, "GET", this.params);
                    break;
                case PLACE_DELETE:
                    break;
                case PLACE_UPDATE:
                    break;
            }

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(Common.TAG_SUCCESS);

                if (success == 1) {
                    //Operation is success
                    switch (this.op)
                    {
                        case PLACE_ADD:
                            break;
                        case PLACE_LOAD:
                        {
                            JSONArray places = json.getJSONArray(TAG_PLACE);
                            for (int i = 0; i < places.length(); i++)
                            {
                                JSONObject obj = places.getJSONObject(i);
                                PlaceObject place = getPlaceFromJSONObj(obj);


                            }

                        }break;
                        case PLACE_DELETE:
                            break;
                        case PLACE_UPDATE:
                            break;
                    }

//                    // products found
//                    // Getting Array of Products
//                    products = json.getJSONArray(TAG_PRODUCTS);
//
//                    // looping through All Products
//                    for (int i = 0; i < products.length(); i++) {
//                        JSONObject c = products.getJSONObject(i);
//
//                        // Storing each json item in variable
//                        String id = c.getString(TAG_PID);
//                        String name = c.getString(TAG_NAME);
//
//                        // creating new HashMap
//                        HashMap<String, String> map = new HashMap<String, String>();
//
//                        // adding each child node to HashMap key => value
//                        map.put(TAG_PID, id);
//                        map.put(TAG_NAME, name);
//
//                        // adding HashList to ArrayList
//                        productsList.add(map);
//                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
//                    Intent i = new Intent(getApplicationContext(),
//                            NewProductActivity.class);
//                    // Closing all previous activities
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
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
