package com.here.zuki.imhere.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.here.zuki.imhere.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.cache.Resource;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

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
    private boolean     allowUpdate;


    public static final String TAG_PLACE            = "Name";
    public static final String TAG_EVNAME           = "EvName";
    public static final String TAG_LAT              = "Lat";
    public static final String TAG_LON              = "Lon";
    public static final String TAG_TIME             = "Time";
    public static final String TAG_TIMELAPSE        = "TimeLapse";
    public static final String TAG_REPORTERNAME     = "ReName";
    public static final String TAG_REPORTERPHONE    = "RePhone";
    public static final String TAG_REPORTERSOCIAL   = "ReSocial";
    public static final String TAG_REPORTERMAIL     = "ReMail";
    public static final String TAG_ID               = "ID";
    public static final String TAG_EVID             = "EvID";
    public static final String TAG_USERID           = "UserID";
    public static final String TAG_PLACES           = "Places";
    public static final String TAG_UPDATE           = "AllowUpdate";


    public static final String load_place_url       = "getPlace.php";
    public static final String add_place_url        = "addPlace.php";


    enum PLACE_OPERATOR
    {
        PLACE_ADD,
        PLACE_UPDATE,
        PLACE_LOAD,
        PLACE_DELETE
    };


    public PlaceObject(){
        this.placeName = null;
        this.eventName = null;
        this.lat = 0;
        this.lon = 0;
        this.timeLapse = 0;
        this.userId = 0;
        this.attrsFind = 0;
        this.EvID = 0;
    }

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

    public void setTimeLapse(int timeLapse)
    {
        this.timeLapse = timeLapse;
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

    public void setReporterName(String reporterName) { this.ReporterName = reporterName; }

    public void setReporterPhone(String reporterPhone) { this.ReporterPhone = reporterPhone; }

    public void setReporterMail(String reporterMail) { this.ReporterMail = reporterMail; }

    public void setReporterAccount(String reporterSocial) { this.ReporterSocial = reporterSocial; }

    public void setAllowUpdate(boolean allow) { this.allowUpdate = allow; }

    public boolean getAllowUpdate() { return  this.allowUpdate; }

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

    public void addPlace(Context context)
    {
        new AddPlace(context, this, Common.datatbaseUrl + add_place_url).execute();
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
                    //json = jsonParser.makeHttpRequest(load_place_url, "GET", this.params);
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
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
    }

    class AddPlace extends AsyncTask<Boolean, String, String>
    {
        private ProgressDialog progressDialog;
        private PlaceObject placeObject;
        private Context pContext;
        private String url;

        public AddPlace(Context context, PlaceObject place, String url)
        {
            this.pContext = context;
            this.placeObject = place;
            this.url  = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(this.pContext);
            progressDialog.setMessage(pContext.getResources().getText(R.string.anp_msg_add_processing));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Boolean... params) {
            try
            {
                String json = "";
                JSONObject jsonObject = new JSONObject();
                InputStream     is;
                OutputStream    os;

                // 1. build jsonObject
                jsonObject.put(TAG_PLACE,            placeObject.getPlaceName());
                jsonObject.put(TAG_EVID,             placeObject.getEvID());
                jsonObject.put(TAG_USERID,           1);
                jsonObject.put(TAG_LAT,              placeObject.getLat());
                jsonObject.put(TAG_LON,              placeObject.getLon());
                jsonObject.put(TAG_TIMELAPSE,        placeObject.getTimeLapse());
                jsonObject.put(TAG_REPORTERNAME,     placeObject.getReporterName());
                jsonObject.put(TAG_REPORTERPHONE,    placeObject.getReporterPhone());
                jsonObject.put(TAG_REPORTERSOCIAL,   placeObject.getReporterSocial());
                jsonObject.put(TAG_REPORTERMAIL,     placeObject.getReporterMail());
                jsonObject.put(TAG_UPDATE,           placeObject.getAllowUpdate());

                // 2. convert JSONObject to JSON to String
                json = jsonObject.toString();

                // 3.create connection

                URL url = new URL(this.url);
                URLConnection connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("POST");
                httpConnection.setConnectTimeout(Common.TIMEOUT_SECOND * Common.SECOND_RATE);
                httpConnection.setReadTimeout(Common.TIMEOUT_SECOND * Common.SECOND_RATE);
                httpConnection.setRequestProperty("Content-Type", "application/json");
                httpConnection.setRequestProperty("Accept", "application/json");
                httpConnection.connect();


                os = httpConnection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                os.write(json.getBytes());
                os.flush();
                os.close();



                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    is = httpConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    return  sb.toString();
                }
                return null;
            }catch (Exception ex)
            {
                ex.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String file_url) {
            progressDialog.dismiss();
        }
    }
}
