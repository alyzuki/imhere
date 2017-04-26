package com.here.zuki.imhere.Utils;

/**
 * Created by zuki on 4/5/17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.here.zuki.imhere.R;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class Network {

    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 0;
    private static boolean connected = false;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean checkNetworkStatus(Context context, boolean showDialog) {
        int conn = Network.getConnectivityStatus(context);
        Resources res = context.getResources();
        connected = true;
        switch (conn)
        {
            case TYPE_WIFI:
            case TYPE_MOBILE:
                break;
            default:
                if(showDialog)
                    Toast.makeText(context, res.getText(R.string.NetworkProblem), Toast.LENGTH_LONG).show();
                connected = false;
        }
        return connected;
    }

//    public static boolean checkNetworkStatus(Context context, boolean showDialog)
//    {
//        if(!showDialog)
//            return checkNetworkStatus(context);
//        if(checkNetworkStatus(context) == false)
//        {
//            Intent intent = new Intent(context, )
//        }
//        return connected;
//    }


    public static InputStream getHttpConnection(String url, JSONObject object) throws Exception
    {
        return getHttpConnection(url, object,-1);
    }

    public static InputStream getHttpConnection(String url, JSONObject object, int timeout) throws Exception
    {
        return getHttpConnection(url, null, object, null, null, timeout, false);
    }

    public static InputStream getHttpConnection(String url, String method, List<NameValuePair> params) throws Exception
    {
        return getHttpConnection(url, method, params, -1);
    }

    public static InputStream getHttpConnection(String url, String method, List<NameValuePair> params, int timeout) throws Exception
    {
        return getHttpConnection(url, method, null, null, params, timeout, false);
    }

    public static InputStream getHttpConnection(String url, String method, List<NameValuePair> params,  List<NameValuePair> requestProperty) throws Exception
    {
        return getHttpConnection(url, method, requestProperty, params, -1);
    }

    public static InputStream getHttpConnection(String url, String method, List<NameValuePair> params,  List<NameValuePair> requestProperty, int timeout) throws Exception
    {
        return getHttpConnection(url, method, null, requestProperty, params, timeout, false);
    }

    public static InputStream getHttpConnection(String url, List<NameValuePair> request) throws Exception
    {
        return getHttpConnection(url, -1);
    }

    public static InputStream getHttpConnection(String url, String method) throws Exception
    {
        return getHttpConnection(url, method, -1);
    }

    public static InputStream getHttpConnection(String url, String method, boolean special) throws Exception
    {
        return getHttpConnection(url, method, null, null, null, -1, special);
    }

    public static InputStream getHttpConnection(String url, String method, int timeout) throws Exception
    {
        return getHttpConnection(url, method, null, null, null, timeout, false);
    }

    public static InputStream getHttpConnection(String url, int timeout) throws Exception
    {
        return getHttpConnection(url, null, null, null, null, timeout, false);
    }

    private  static InputStream getHttpConnection(String urlString, String method, JSONObject jsonObject, List<NameValuePair> requestProperty, List<NameValuePair> params, int timeout, boolean special)
            throws Exception {
        String HTTP="HTTPCON_LOG";
        OutputStreamWriter osw = null;
        OutputStream os = null;
        InputStream is = null;
        Exception throwEx = null;
        HttpURLConnection httpConnection = null;
        try
        {
            URL url = new URL(urlString);
            boolean httpWriter = false;
            //open connection
            URLConnection connection = url.openConnection();
            httpConnection = (HttpURLConnection) connection;
            //set method
            if(method == null || method.isEmpty() || method.toUpperCase().equals("GET") )
                httpConnection.setRequestMethod("GET");
            else
                httpConnection.setRequestMethod("POST");
            //set timeout
            if(timeout == -1) {
                timeout = Common.TIMEOUT_SECOND;
            }

            if(!special) {
                httpConnection.setConnectTimeout(timeout * Common.SECOND_RATE);
                httpConnection.setReadTimeout(timeout * Common.SECOND_RATE);
                httpConnection.setDoInput(true);
                httpConnection.setDoOutput(true);
            }
            //set json
            if(jsonObject != null) {
                httpConnection.setRequestProperty("Content-Type", "application/json");
                httpConnection.setRequestProperty("Accept", "application/json");
                httpWriter = true;
            }
            //set parameter
            else if(params != null)
            {
                httpConnection.setRequestProperty("Content-Type", "text/plain");
                httpWriter = true;
            }
            //set request
            if(requestProperty != null && !requestProperty.isEmpty())
            {
                for (NameValuePair nvp : requestProperty) {
                    httpConnection.setRequestProperty(nvp.getName(), nvp.getValue());
                }
            }
            httpConnection.connect();

            if(httpWriter)
            {

                os = httpConnection.getOutputStream();
                osw = new OutputStreamWriter(os);
                StringBuilder sb = new StringBuilder("");
                String writerData = null;
                if(jsonObject != null)
                {
                    sb.append(jsonObject.toString());
                }else
                {
                    for (NameValuePair nvp: params)
                    {
                        if("".equals(sb.toString()))
                            sb.append("?");
                        else
                            sb.append("&");
                        sb.append(nvp.getName());
                        sb.append("=");
                        sb.append(nvp.getValue());
                    }
                }
                if(!sb.toString().isEmpty())
                {
                    osw.write(sb.toString());
                    osw.flush();
                }
                osw.close();
                os.close();
            }

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = httpConnection.getInputStream();
            }
        }catch (SocketTimeoutException stEx)
        {
            Log.d(HTTP, "SocketTimeoutException");
            throwEx = stEx;
        }
        catch (IOException ioEx)
        {
            Log.d(HTTP, "IOException");
            if(osw !=null)
                osw.close();
            if (os != null)
                os.close();
            throwEx = ioEx;
        }
        catch (Exception ex)
        {
            Log.d(HTTP, "Exception");
            throwEx = ex;
        }
        finally {

//            if(httpConnection != null)
//            {
//                //httpConnection.disconnect();
//            }
            if(throwEx != null)
                throw  throwEx;
            return is;
        }
    }

    public static String makeHttpResponseToString(InputStream inputStream)
            throws IOException
    {
        if(inputStream == null)
            return  null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        inputStream.close();
        reader.close();
        return sb.toString();
    }

    public static JSONObject makeHttpResponseToJSONObject(InputStream inputStream)
            throws IOException, JSONException
    {
        String json = makeHttpResponseToString(inputStream);
        if(json == null)
            return null;
        return new JSONObject(json);
    }

    public static ProgressDialog newLoadingDialog(Context pContext)
    {
        ProgressDialog progressDialog = new ProgressDialog(pContext);
        progressDialog.setMessage(pContext.getResources().getText(R.string.anp_msg_add_processing));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }
}

