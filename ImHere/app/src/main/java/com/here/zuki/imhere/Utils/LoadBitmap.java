package com.here.zuki.imhere.Utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by zuki on 3/23/17.
 */
public class LoadBitmap   extends AsyncTask<String, Void, Bitmap>
{

    private String name;
    private Bitmap bitmap;
    private Object obj;

    public LoadBitmap(ImageView imageView, String imageName)
    {
        this.obj = imageView;
        this.name = imageName;
    }

    public LoadBitmap(ImageButton btn, String imageName)
    {
        this.obj = btn;
        this.name = imageName;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        BitmapUrlUtils buu = BitmapUrlUtils.getInstance();
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
