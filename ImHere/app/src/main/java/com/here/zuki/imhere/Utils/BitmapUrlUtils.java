package com.here.zuki.imhere.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by zuki on 3/23/17.
 */

public class BitmapUrlUtils {

    private static BitmapUrlUtils instance = null;
    private static String LOG   = ":::::BUU";

    private static String Url = "http://bdssmart.net/.image/";

    private HashMap<String, Bitmap> hash;

    private static ContextWrapper cw;
    private static File ownDir;

    public BitmapUrlUtils(){
        super();
        hash = new HashMap<String, Bitmap>();
        instance = this;
        cw = new ContextWrapper(ApplicationContextProvider.getContext());
        ownDir = cw.getDir("profile", Context.MODE_PRIVATE);
        if(!ownDir.exists())
            ownDir.mkdir();
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

        File fullPath = new File(ownDir, picName);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
//        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        if(fullPath.exists())
        {
            try {
                FileInputStream fis = new FileInputStream(fullPath);
                pic = BitmapFactory.decodeStream(fis);
                fis.close();
                hash.put(picName, pic);
            }catch (IOException ioEx)
            {
                Log.d(LOG, "Error get file " + ioEx.toString());
                pic = null;
            }
            return pic;
        }

        InputStream stream = null;
        try {
            stream = getHttpConnection(Url + picName);
            pic = BitmapFactory.
                    decodeStream(stream, null, bmOptions);
            stream.close();
        }catch (SocketTimeoutException stoEx)
        {
            Log.e("Error TIMEOUT", stoEx.getMessage());
            stoEx.printStackTrace();
            pic = null;
        }
        catch (IOException ioEx) {
            Log.e("Error", ioEx.getMessage());
            ioEx.printStackTrace();
            pic = null;
        }catch (Exception Ex)
        {
            Log.e("Error", Ex.getMessage());
            Ex.printStackTrace();
            pic = null;
        }
        finally {
            if(pic != null)
            {
                hash.put(picName, pic);
                try {
                    //fullPath.mkdir();
                    FileOutputStream fos = new FileOutputStream(fullPath);
                    pic.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.flush();
                    fos.close();
                }catch (FileNotFoundException fnfEx)
                {
                    Log.d(LOG, "File not found " + fnfEx.toString());
                }catch (IOException ioEx)
                {
                    Log.d(LOG, "Error access file " + ioEx.toString());
                }
            }

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
            httpConnection.setConnectTimeout(Common.TIMEOUT_SECOND * Common.SECOND_RATE);
            httpConnection.setReadTimeout(Common.TIMEOUT_SECOND * Common.SECOND_RATE);
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        }
        catch (SocketTimeoutException stoEx)
        {
            stoEx.printStackTrace();
            throw  stoEx;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return stream;
    }

    public void removeFile()
    {
        File file = new File("/data/data/com.here.zuki.imhere/app_profile/placeholder.png");
        file.delete();
        file = new File("/data/data/com.here.zuki.imhere/app_profile/wedding.png");
        file.delete();
        file = new File("/data/data/com.here.zuki.imhere/app_profile/party.png");
        file.delete();
        file = new File("/data/data/com.here.zuki.imhere/app_profile/beer.png");
        file.delete();
    }
}
