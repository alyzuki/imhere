package com.here.zuki.imhere.Utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.here.zuki.imhere.LoginActivity;
import com.here.zuki.imhere.MapActivity;

/**
 * Created by zuki on 3/23/17.
 */
public class LoadBitmap   extends AsyncTask<String, Void, Bitmap>
{

    private String name;
    private Object obj;

    public LoadBitmap(Object obj, String imageName)
    {
        this.obj = obj;
        this.name = imageName;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        BitmapUrlUtils buu = BitmapUrlUtils.getInstance();
        if(name.contains("http://") || name.contains("https://"))
        {
            return buu.getBitmapFromUrl(name);
        }
        return buu.getBitmap(this.name);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(obj instanceof ImageButton)
            ((ImageButton)obj).setImageBitmap(result);
        else if(obj instanceof ImageView)
            ((ImageView)obj).setImageBitmap(result);
        else if(obj instanceof FloatingActionButton)
            ((FloatingActionButton)obj).setImageBitmap(result);
    }

}
