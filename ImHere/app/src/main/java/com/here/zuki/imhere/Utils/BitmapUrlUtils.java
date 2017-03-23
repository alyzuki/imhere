package com.here.zuki.imhere.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by zuki on 3/23/17.
 */

public class BitmapUrlUtils {

    private static BitmapUrlUtils instance = null;

    private static String Url = "http://bdssmart.net/.image/";

    private HashMap<String, Bitmap> hash;

    public BitmapUrlUtils(){
        super();
        hash = new HashMap<String, Bitmap>();
        instance = this;
    }

    //override constructor

    //getInstance
    public  static synchronized BitmapUrlUtils getInstance()
    {
        if(instance == null)
            new BitmapUrlUtils();
        return instance;
    }

    public Bitmap getBitmap(String picName)
    {
        if(picName == null)
            return null;
        if(hash.containsKey(picName))
            return (Bitmap) hash.get(picName);

        Bitmap pic = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(Url + picName);
            pic = BitmapFactory.
                    decodeStream(stream, null, bmOptions);
            stream.close();
        } catch (IOException ioEx) {
            Log.e("Error", ioEx.getMessage());
            ioEx.printStackTrace();
        }finally {
            hash.put(picName, pic);
            return pic;
        }
    }

    private InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }
}
