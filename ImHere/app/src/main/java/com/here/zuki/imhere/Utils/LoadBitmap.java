package com.here.zuki.imhere.Utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.here.zuki.imhere.LoginActivity;
import com.here.zuki.imhere.MapActivity;
import com.here.zuki.imhere.R;

/**
 * Created by zuki on 3/23/17.
 */
public class LoadBitmap   extends AsyncTask<String, Void, Bitmap>
{

    private String name;
    private Object obj;
    private String defaultPic;

    public LoadBitmap(Object obj, String imageName)
    {
        this.obj = obj;
        this.name = imageName;
        this.defaultPic = null;
    }

    public LoadBitmap(Object obj, String imageName, String defaultPic)
    {
        this.obj = obj;
        this.name = imageName;
        this.defaultPic = defaultPic;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        BitmapUrlUtils buu = BitmapUrlUtils.getInstance();
        Bitmap bitmapRet = null;
        if(params != null && params.length > 0 && params[0] != null)
        {
            bitmapRet = buu.getBitmap(this.name, params[0]);
        }
        else if(name.contains("http://") || name.contains("https://"))
        {
            bitmapRet =  buu.getBitmapFromUrl(name);
        }else
            bitmapRet = buu.getBitmap(this.name);
        if(bitmapRet == null && defaultPic != null && !defaultPic.equals(name))
            bitmapRet = buu.getBitmap(this.defaultPic);
        return bitmapRet;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(obj instanceof ImageButton)
            ((ImageButton)obj).setImageBitmap(result);
        else if(obj instanceof ImageView) {
            ImageView imgView = ((ImageView) obj);
//            if(imgView.getId() == R.id.image_profile)
//            {
//                if(result == null)
//            }
            imgView.setImageBitmap(result);
        }
        else if(obj instanceof FloatingActionButton)
            ((FloatingActionButton)obj).setImageBitmap(result);
    }

}
