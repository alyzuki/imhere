package com.here.zuki.imhere.Adapter;

import android.content.Context;
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
import com.here.zuki.imhere.Utils.PlaceObject;

import java.util.ArrayList;

/**
 * Created by zuki on 3/22/17.
 */

public class PlaceAdapter extends ArrayAdapter<PlaceObject> implements View.OnClickListener {

    private  ArrayList<PlaceObject> dataSet;
    private Context context;


    private static class PlaceHolder
    {
        TextView        tvName;
        TextView        tvEvName;
        LinearLayout    placeInfo;
        ImageButton     icon;
        TextView        tvReName;
        TextView        tvRePhone;
        TextView        tvReSocail;
    }

    public PlaceAdapter(ArrayList<PlaceObject> data, Context context)
    {
        super(context, R.layout.event_item, data);
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }

    private int lastPos = -1;
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
            holder.placeInfo    = (LinearLayout) convertView.findViewById(R.id.placeInfomation);
            holder.icon         = (ImageButton) convertView.findViewById(R.id.placeIcon);
            holder.tvReName     = (TextView) convertView.findViewById(R.id.tvReName);
            holder.tvRePhone    = (TextView) convertView.findViewById(R.id.tvRePhone);
            holder.tvRePhone    = (TextView) convertView.findViewById(R.id.tvRePhone);
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
        if(placeObj.getAttrs() == 0)
            holder.placeInfo.setVisibility(View.INVISIBLE);
        holder.tvRePhone.setText(placeObj.getPlaceName());
        holder.tvName.setText(placeObj.getPlaceName());
//
//        viewHolder.txtName.setText(dataModel.getName());
//        viewHolder.txtType.setText(dataModel.getType());
//        viewHolder.txtVersion.setText(dataModel.getVersion_number());
//        viewHolder.info.setOnClickListener(this);
//        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
