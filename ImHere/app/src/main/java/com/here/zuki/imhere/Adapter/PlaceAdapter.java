package com.here.zuki.imhere.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnimatorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.here.zuki.imhere.R;
import com.here.zuki.imhere.Utils.BitmapUrlUtils;
import com.here.zuki.imhere.Utils.Common;
import com.here.zuki.imhere.Utils.PlaceObject;

import java.util.ArrayList;

/**
 * Created by zuki on 3/22/17.
 */

public class PlaceAdapter extends ArrayAdapter<PlaceObject> implements View.OnClickListener {

    private  ArrayList<PlaceObject> dataSet;
    private Context context;
    private int type;
    private BitmapUrlUtils buu;

    private static class PlaceHolder
    {
        TextView        tvName;
        TextView        tvEvName;
        ImageButton     icon;
        TextView        tvReName;
        TextView        tvRePhone;
        TextView        tvReSocial;
        TextView        tvReMail;
    }

    public PlaceAdapter(ArrayList<PlaceObject> data, Context context)
    {
        super(context, R.layout.event_item, data);
        this.dataSet = data;
        this.context = context;
        buu = BitmapUrlUtils.getInstance();
    }


    @Override
    public void onClick(View v) {

    }

    private int lastPos = -1;
    @SuppressWarnings("ResourceType")
    @Override
    public  View getView(int pos, View convertView, ViewGroup parent)
    {
        PlaceObject placeObj = getItem(pos);
        // Check if an existing view is being reused, otherwise inflate the view
        PlaceHolder holder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            holder = new PlaceHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.place_item, parent, false);
            holder.tvName       = (TextView) convertView.findViewById(R.id.tvPlaceName);
            holder.tvEvName     = (TextView) convertView.findViewById(R.id.tvEvName);
            holder.icon         = (ImageButton) convertView.findViewById(R.id.placeIcon);
            holder.tvReName     = (TextView) convertView.findViewById(R.id.tvReName);
            holder.tvRePhone    = (TextView) convertView.findViewById(R.id.tvRePhone);
            holder.tvRePhone    = (TextView) convertView.findViewById(R.id.tvRePhone);
            holder.tvReMail     = (TextView) convertView.findViewById(R.id.tvReMail);
            result=convertView;
            convertView.setTag(holder);
        } else {
            holder = (PlaceHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(this.context,
                (pos > lastPos) ?   R.animator.slide_from_bottom : R.animator.slide_from_top);
        result.startAnimation(animation);
        lastPos = pos;

        holder.tvName.setText(placeObj.getPlaceName());
        holder.tvEvName.setText(placeObj.getEventName());

//        buu.setFileName("logo.png");
//        buu.execute();
//        holder.icon.setImageBitmap(buu.getBitmap());

        LinearLayout viewContainer = (LinearLayout)result.findViewById(R.id.placeInfomation);
        if(viewContainer != null)
        {
            while (true)
            {
                int attrs = placeObj.getAttrs();
                if (attrs == 0 || (attrs | Common.ATTRS_NAME) == 0) {
                    viewContainer.removeAllViews();
                    break;
                }else
                    holder.tvReName.setText(placeObj.getReporterName());
                if ((attrs | Common.ATTRS_PHONE) == 0)
                    viewContainer.removeView(result.findViewById(R.id.PlacePhoneInfo));
                else
                    holder.tvRePhone.setText(placeObj.getReporterPhone());
                if ((attrs | Common.ATTRS_SOCIAL) == 0)
                    viewContainer.removeView(result.findViewById(R.id.PlaceSocialInfo));
                else
                    holder.tvReMail.setText(placeObj.getReporterMail());
                if ((attrs | Common.ATTRS_MAIL) == 0)
                    viewContainer.removeView(result.findViewById(R.id.PlaceMailInfo));
                else
                    holder.tvReSocial.setText(placeObj.getReporterSocial());
                break;
            }
        }
        return convertView;
    }
}
