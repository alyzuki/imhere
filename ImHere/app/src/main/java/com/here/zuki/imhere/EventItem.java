package com.here.zuki.imhere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zuki on 3/17/17.
 */

public class EventItem  extends LinearLayout{


    public EventItem(Context context)
    {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.event_item, this);
    }

    public EventItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context, attrs);
    }

    public EventItem(Context context, AttributeSet attrs, int defs)
    {
        this(context,attrs);
        initView(context, attrs);
    }

    public void  initView(Context context, AttributeSet attrs)
    {
        TypedArray ta = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ListItem,
                0, 0
        );
        LayoutInflater.from(context).inflate(R.layout.event_item, this);

        this.setBackground(ta.getInt(R.styleable.ListItem_itemBg, -1));

        this.setTitle(ta.getString(R.styleable.ListItem_titleItem));

        this.setIconSrc(ta.getString(R.styleable.ListItem_iconSrc));

        this.setInputVisible(ta.getBoolean(R.styleable.ListItem_inputVisible, false));

        this.setType(ta.getInteger(R.styleable.ListItem_type, 0));

        int count = this.getChildCount();
        for(int i = 0; i < count; i++)
        {
            View child = this.getChildAt(i);
            switch (child.getId())
            {
                //define all child that will pass click listener
                case 0:
                case -1:
                    break;
                default:
                    child.setOnClickListener(null);

            }
        }
    }

    public void setTitle(String title)
    {
        if(title == null)
            title = "Item Name";
        ((TextView)findViewById(R.id.tvName)).setText(title);
    }

    public void setBackground(int color)
    {
        if(color == -1)
            return;
        setBackgroundColor(color);
    }

    public void setType(int type)
    {
        switch (type)
        {
            case 0:
                break;
            case 1:
                break;
        }
    }

    public void setIconSrc(String src)
    {
        if(src == null)
            src = "@drawable/common_google_signin_btn_icon_dark";
        ((ImageButton)findViewById(R.id.iconShow)).setBackground(Drawable.createFromPath(src));
    }

    public void setInputVisible(boolean isVisible)
    {
        findViewById(R.id.itemInput).setVisibility( isVisible ? VISIBLE : INVISIBLE);
    }

}
