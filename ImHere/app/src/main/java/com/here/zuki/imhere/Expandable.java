package com.here.zuki.imhere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
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
        super.onFinishInflate();
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
        LayoutInflater.from(context).inflate(R.layout.expandable, this);
        final HeaderGroup header = (HeaderGroup)findViewById(R.id.expandheader);
        header.setTitle(ta.getString(R.styleable.Expandable_header));
        header.setBackground(ta.getInt(R.styleable.Expandable_headerBg, -1));
        header.setFontSize(ta.getFloat(R.styleable.Expandable_headerfontsize, -1));
        int PaddingStart = ta.getInt(R.styleable.Expandable_PaddingStart, 0);
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
        LinearLayout ll = (LinearLayout)findViewById(R.id.group_parent);
        int count = ll.getChildCount();
        for(int i = 0; i < count; i++)
        {
            View instance = ll.getChildAt(i);
            if(instance instanceof HeaderGroup)
                continue;
            else if(instance instanceof LinearLayout)
            {
                int count2 = ((LinearLayout)instance).getChildCount();
                for (int j = 0; i < count2; j++)
                {
                    View _instance = ((LinearLayout)instance).getChildAt(j);
                    Log.d("EXPAND CHILD", String.valueOf(instance.getId()));
                }
            }
            instance.setPaddingRelative(PaddingStart,0,0,0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw the View
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

}
