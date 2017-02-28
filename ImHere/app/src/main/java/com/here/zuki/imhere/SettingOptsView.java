package com.here.zuki.imhere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zuki on 2/27/17.
 */

class SettingOptsView extends LinearLayout {

    private  String sFont,  sTitle;
    private  int cBackground, cFont, cIndicator;
    private  int iFont, iIndicator, iPos;
    private  float pad;
    private  View btnView;

    public SettingOptsView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.setting_opt_view, this);
    }
    public SettingOptsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }


    public SettingOptsView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs)
    {
        TypedArray ta = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ZukiMenu,
                0, 0);
        try
        {
            iIndicator  = ta.getInt(R.styleable.ZukiMenu_indicatorSize, 13);
            iFont       = ta.getInt(R.styleable.ZukiMenu_strSize, 12);
            iPos        = ta.getInt(R.styleable.ZukiMenu_indicator, 1);
            cBackground = ta.getColor(R.styleable.ZukiMenu_bg, Color.WHITE);
            cFont       = ta.getColor(R.styleable.ZukiMenu_strColor, Color.BLACK);
            cIndicator  = ta.getColor(R.styleable.ZukiMenu_indicatorColor, Color.BLACK);
            sFont       = ta.getString(R.styleable.ZukiMenu_fontname);
            sTitle      = ta.getString(R.styleable.ZukiMenu_title);
            pad         = ta.getDimension(R.styleable.ZukiMenu_android_padding, 0);
            LayoutInflater.from(context).inflate(R.layout.setting_opt_view, this);
            switch (iPos)
            {
                case  0:
                    btnView = findViewById(R.id.btnMnBack);
                    findViewById(R.id.btnMnNext).setVisibility(View.INVISIBLE);
                    break;
                default:
                    btnView = findViewById(R.id.btnMnNext);
                    findViewById(R.id.btnMnBack).setVisibility(View.INVISIBLE);
                    break;
            }
            btnView.setVisibility(VISIBLE);
            if(sTitle != null)
            {
                ((TextView)(findViewById(R.id.textTilte))).setText(sTitle);
            }else
                ((TextView)(findViewById(R.id.textTilte))).setText("Application demo menu");

            ((Button)findViewById(R.id.btnMnBack)).setTextColor(cIndicator);
            ((Button)findViewById(R.id.btnMnNext)).setTextColor(cIndicator);
            ((LinearLayout)findViewById(R.id.containerOpts)).setBackgroundColor(cBackground);
            ((TextView)(findViewById(R.id.textTilte))).setTextColor(cFont);

        }finally {
            ta.recycle();
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //draw the View
    }
    public String getFont()
    {
        return  this.sFont;
    }

    public String getTitle()
    {
        return this.sTitle;
    }

    public int getFontSize()
    {
        return  this.iFont;
    }

    public int getIndicatorSize()
    {
        return this.iIndicator;
    }

    public int getIndicatorPos()
    {
        return this.iPos;
    }

    public int getMnBackground()
    {
        return this.cBackground;

    }
    public int getFontColor()
    {
        return  this.cFont;
    }

    public int getIndicatorColor()
    {
        return this.cIndicator;
    }

    public void setOptsFont()
    {

    }
}

