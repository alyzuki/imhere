package com.here.zuki.imhere.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.here.zuki.imhere.R;
import com.here.zuki.imhere.Utils.BitmapUrlUtils;
import com.here.zuki.imhere.Utils.Common;
import com.here.zuki.imhere.Utils.EventItem;

import java.util.ArrayList;

/**
 * Created by zuki on 3/26/17.
 */

public class EventAdapter extends ArrayAdapter<EventItem> implements View.OnClickListener {


    private class EventHolder {
        ImageButton icon;
        TextView tvEventName;
        RadioButton rdbSelected;
        EditText edEventName;
    }

    private Context context;
    private ArrayList<EventItem> dataSet;
    private BitmapUrlUtils buu;
    private int type;


    public EventAdapter(ArrayList<EventItem> data, Context context) {
        super(context, R.layout.event_item, data);
        this.context = context;
        this.dataSet = data;
        this.buu = BitmapUrlUtils.getInstance();
    }

    public void setType(int type)
    {
        this.type = type;
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getFullView(position, convertView, parent);
    }

    private int lastPos = -1;
    @SuppressWarnings("ResourceType")
    @Override
    public  View getView(int pos, View convertView, ViewGroup parent)
    {
        return getMinView(pos, convertView, parent);
    }

    private   View getFullView(int position, View convertView, ViewGroup parent)
    {
        EventItem event =getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        EventHolder holder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            holder = new EventHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_item, parent, false);
            holder.tvEventName  = (TextView) convertView.findViewById(R.id.tvEventItemName);
            holder.edEventName  = (EditText) convertView.findViewById(R.id.editEventInput);
            holder.icon         = (ImageButton) convertView.findViewById(R.id.eventIcon);
            holder.rdbSelected  = (RadioButton) convertView.findViewById(R.id.rdbEventSelected);
            result=convertView;
            convertView.setTag(holder);
        } else {
            holder = (EventHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(this.context,
//                (pos > lastPos) ?   R.animator.slide_from_bottom : R.animator.slide_from_top);
//        result.startAnimation(animation);
        lastPos = position;
//set icon image
//        buu.setFileName("logo.png");
//        buu.execute();
//        holder.icon.setImageBitmap(buu.getBitmap());

        holder.tvEventName.setText(event.getEventNane());


        if( this.type == Common.EVENT_ADAPTER_GET ||  event.getType() != Common.EVENT_TYPE_OTHER) {
            holder.edEventName.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            holder.edEventName.requestLayout();
        }
        return convertView;
    }


    private View getMinView(int position, View convertView,ViewGroup parent)
    {
        EventItem event =getItem(position);

        class  tempHolder
        {
            TextView tv;
        }

        tempHolder holder;
        final View result;

        if (convertView == null) {
            holder = new tempHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_dropdown_item, parent, false);

            holder.tv  = (TextView) convertView.findViewById(R.id.tv_event_dropdown_item);
            result=convertView;
            convertView.setTag(holder);
        } else {
            holder = (tempHolder) convertView.getTag();
            result=convertView;
        }
        holder.tv.setText(event.getEventNane());
        return convertView;
    }

}
