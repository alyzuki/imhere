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

}