package com.here.zuki.imhere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by zuki on 3/2/17.
 */

public class Expandable extends LinearLayout {

    private  boolean showContainer = false;
    private  int defheight, height = -1;
    private ArrayList<Childitem> listElements = null;


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
        setBackground(ta.getInt(R.styleable.Expandable_containerBg, -1));
        int PaddingStart = ta.getInt(R.styleable.Expandable_PaddingStart, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw the View

        Log.d("EXPANDALE", "onDraw " + this.getHeight());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("EXPANDALE", "onFinishInflate " + this.getHeight());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("EXPANDALE", "onMeasure " + this.getHeight());
    }

    public  void toggle()
    {
        HeaderGroup header = (HeaderGroup)findViewById(R.id.expandheader);
        header.headerToggle();
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            Log.d("Expandable ", "index " + String.valueOf(i) + " value ===" + ((View) this.getChildAt(i)).toString());
            View view = this.getChildAt(i);
            if (view instanceof HeaderGroup)
                continue;
            else if (view.getId() == R.id.group_parent)
                continue;
            else {
                childSetLayoutParams(header.isShowContainer(), view.getId());
                Log.d("Child", "size: " + view.getWidth() + " " + view.getHeight());
                }
            }
        this.requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d("EXPANDALE", "onLayout " + this.getHeight());
        if(height == -1) {
            height = this.getHeight(); //height is ready
            defheight = findViewById(R.id.expandheader).getHeight() + 1;
        }
        if(listElements == null)
        {
            listElements = new ArrayList<Childitem>() ;
            int count = this.getChildCount();
            for(int i = 0; i < count; i++)
            {
                Log.d("Expandable " , "index " + String.valueOf(i) + " value ===" + ((View)this.getChildAt(i)).toString());
                View view = this.getChildAt(i);
                if(view == null)
                    continue;
                if(view instanceof  HeaderGroup)
                    continue;
                else if (view.getId() == R.id.group_parent)
                    continue;
                else {
                    Childitem child = new Childitem(view.getId(), view.getWidth(), view.getHeight());
                    listElements.add(child);
                }
            }
            toggle();
        }
    }

    private  void childSetLayoutParams(boolean show, int id)
    {
        for (Childitem child : listElements) {
            if (child != null) {
                if(child.getID() == id)
                {
                    View view  = findViewById(id);
                    if(view == null )
                        continue;
                    LinearLayout.LayoutParams childParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                    childParams.height = show ? child.getHeight() : 0;
                    view.setLayoutParams(childParams);
                    view.requestLayout();
                    //this.setAddStatesFromChildren();
                }
            }
        }
    }

    void setBackground(int color)
    {
        if(color == -1)
            return;
        this.setBackgroundColor(color);
    }
}




