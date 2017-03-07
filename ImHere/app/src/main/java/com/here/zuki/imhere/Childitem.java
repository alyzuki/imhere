package com.here.zuki.imhere;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zuki on 3/7/17.
 */

public class Childitem {

    private int id, width, height;

    public Childitem(int id, int w, int h) {
        this.id = id;
        this.height = h;
        this.width = w;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getID() {
        return this.id;
    }
/*
    public void setLayoutParamsWidth(int width) {
        if(this.view == null)
            return;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
    }

    public void setLayoutParamsWidth() {
        setLayoutParamsWidth(this.width);
    }

    public void setLayoutParamsHeight(int height) {
        if(this.view == null)
            return;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

    public void setLayoutParamsHeight() {
        setLayoutParamsHeight(this.height);
    }

    public void setLayoutParams(int width, int height) {
        if(this.view == null)
            return;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    public void setLayoutParams() {
        setLayoutParams(this.width, this.height);
    }*/
}