package com.here.zuki.imhere.Utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


/**
 * Created by zuki on 3/22/17.
 */

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public JSONParser(){}

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String urlString, String method,
                                      List<NameValuePair> params)
        throws JSONParserException{
        String fullUrl= urlString;
        try {
            InputStream is = null;
            //append parameters
            if(params != null && ! params.isEmpty()) {
                StringBuilder builder = new StringBuilder(urlString);
                for (NameValuePair nvp : params) {
                    String name = nvp.getName();
                    String value = nvp.getValue();
                    if (urlString.equals(builder.toString())) {
                        builder.append('?');
                    } else
                        builder.append('&');
                    builder.append(name + "=" + value);
                }
                fullUrl = builder.toString();
            }
            URL url = new URL(fullUrl);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod(method);
            httpConnection.setConnectTimeout(Common.TIMEOUT_SECOND * Common.SECOND_RATE);
            httpConnection.setReadTimeout(Common.TIMEOUT_SECOND * Common.SECOND_RATE);
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = httpConnection.getInputStream();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            // try parse the string to a JSON object
            jObj = new JSONObject(json);

        }
        //httpConnection.getOutputStream()
        catch (SocketTimeoutException stoEx)
        {
            throw  new  JSONParserException( "JSONPARSER TIMEOUT:\nUrl:" +fullUrl+ "\n" + stoEx.toString());
        }
        catch (IOException ioEx)
        {
            throw  new  JSONParserException( "JSONPARSER IO:\nUrl:" +fullUrl+ "\n"  + ioEx.toString());
        }
        catch (JSONException jsEx) {
            throw  new  JSONParserException( "JSONPARSER JSON:\nUrl:" +fullUrl+ "\n"  + jsEx.toString());
        }
        catch (Exception Ex)
        {
            throw  new  JSONParserException( "JSONPARSER:\nUrl:" +fullUrl+ "\n"  + Ex.toString());
        }

        // return JSON String
        return jObj;

    }

    public final class JSONParserException extends Exception
    {
        public JSONParserException(String message) {
            super(message);
        }

        public JSONParserException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }

}
