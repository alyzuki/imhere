package com.here.zuki.imhere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    private OnClickListener listener;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(listener != null) listener.onClick(this);
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_UP && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if(listener != null) listener.onClick(this);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

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
        this.setClickable(true);
        this.setEnabled(true);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs)
    {
        TypedArray ta = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ZukiMenu,
                0, 0);
        int[] set = {
                android.R.attr.background, // idx 0
                android.R.attr.onClick        // idx 1
        };
        TextView text;
        try
        {
            iIndicator  = ta.getInt(R.styleable.ZukiMenu_indicatorSize, 13);
            iFont       = ta.getInt(R.styleable.ZukiMenu_strSize, 12);
            iPos        = ta.getInt(R.styleable.ZukiMenu_indicator, 1);
            cBackground = ta.getColor(R.styleable.ZukiMenu_wbackground, Color.WHITE);
            cFont       = ta.getColor(R.styleable.ZukiMenu_strColor, Color.BLACK);
            cIndicator  = ta.getColor(R.styleable.ZukiMenu_indicatorColor, Color.BLACK);
            sFont       = ta.getString(R.styleable.ZukiMenu_fontname);
            sTitle      = ta.getString(R.styleable.ZukiMenu_title);
            pad         = ta.getDimension(R.styleable.ZukiMenu_android_padding, 0);
            LayoutInflater.from(context).inflate(R.layout.setting_opt_view, this);
            text = ((TextView)(findViewById(R.id.textTilte)));
            switch (iPos)
            {
                case  0:
                    btnView = findViewById(R.id.btnMnBack);
                    findViewById(R.id.btnMnNext).setVisibility(View.INVISIBLE);
                    Log.d("=>Set text size:", String.valueOf(text.getTextSize()));
                    float size = text.getTextSize();// + (float) 0.0002;
                    text.setTextSize(TypedValue.COMPLEX_UNIT_PX, size + 10);
                    Log.d("=>Set text size:", String.valueOf(text.getTextSize()));
                    findViewById(R.id.seperate).setVisibility(View.INVISIBLE);
                    break;
                default:
                    btnView = findViewById(R.id.btnMnNext);
                    findViewById(R.id.btnMnBack).setVisibility(View.INVISIBLE);
                    break;
            }
            btnView.setVisibility(VISIBLE);

            if(sTitle != null)
            {
                text.setText(sTitle);
            }else
                text.setText("Application demo menu");

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

