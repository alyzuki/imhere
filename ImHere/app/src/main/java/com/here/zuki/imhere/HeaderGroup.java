package com.here.zuki.imhere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zuki on 3/2/17.
 */

public class HeaderGroup extends LinearLayout {

    private  boolean showContainer = false;
    public HeaderGroup(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.group_setting_elements, this);
    }
    public HeaderGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }


    public HeaderGroup(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HeaderGroup,
                0, 0);
        LayoutInflater.from(context).inflate(R.layout.group_setting_elements, this);
        Button btn = (Button)findViewById(R.id.btnExpand);
        btn.setOnClickListener(
            new OnClickListener() {
                @Override
                public void onClick(View v) {
                    headerToggle();
                }
            }
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw the View
    }

    public void setTitle(String title)
    {
        TextView tv = (TextView)findViewById(R.id.header_title);
        if(TextUtils.isEmpty(title))
            tv.setText("New Menu");
        else
            tv.setText(title);
    }

    public void setBackground(int color)
    {
        if(color < 0)
            return;
        LinearLayout ll = (LinearLayout)findViewById(R.id.settings_header);
        ll.setBackgroundColor(color);
    }

    public void setFontSize(float size)
    {
        if(size < 0)
            return;
        TextView tv = (TextView)findViewById(R.id.header_title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void headerToggle()
    {
        Button btn = (Button)findViewById(R.id.btnExpand);
        showContainer = !showContainer;
        btn.setBackground(ResourcesCompat.getDrawable(getResources(), showContainer ? R.drawable.ic_expand :R.drawable.ic_collapse ,null));
    }

    public boolean isShowContainer()
    {
        return showContainer;
    }
}
