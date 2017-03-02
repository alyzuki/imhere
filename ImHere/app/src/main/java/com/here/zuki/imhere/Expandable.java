package com.here.zuki.imhere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zuki on 3/2/17.
 */

public class Expandable extends LinearLayout {

    private  boolean showContainer = false;
    public Expandable(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.group_setting_elements, this);
    }
    public Expandable(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }


    public Expandable(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        initViews(context, attrs);
    }

    private void initViews(final Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Expandable,
                0, 0);
        final HeaderGroup header = (HeaderGroup)findViewById(R.id.header);
        header.setTitle(ta.getString(R.styleable.Expandable_header));
        header.setBackground(ta.getInt(R.styleable.Expandable_headerBg, -1));
        header.setFontSize(ta.getInt(R.styleable.Expandable_headerfontsize, -1));
        LayoutInflater.from(context).inflate(R.layout.expandable, this);
        header.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        header.headerToggle();
                        boolean visible = header.isShowContainer();
                        LinearLayout ll = (LinearLayout)findViewById(R.id.group_parent);
                        int count = ll.getChildCount();
                        for(int i = 0; i < count; i++)
                        {
                            View instance = ll.getChildAt(i);
                            if(instance instanceof HeaderGroup)
                                continue;
                            instance.setVisibility(visible ? VISIBLE : INVISIBLE);
                        }
                    }
                }
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw the View
    }
}
