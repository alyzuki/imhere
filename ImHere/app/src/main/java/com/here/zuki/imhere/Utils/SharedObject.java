package com.here.zuki.imhere.Utils;

import android.content.Intent;

import com.here.zuki.imhere.CatalogueActivity;

import java.util.ArrayList;

/**
 * Created by zuki on 3/29/17.
 */

public class SharedObject {

    private static SharedObject instance = null;

    private PlaceObject foundPlace;

    private EventItem foundGroupEvent;

    private EventItem foundSpecialEvent;

    private ArrayList<Intent> curIntentList;

    private EventItem catalogueItem;

    private int catalogueType;

    protected SharedObject()
    {
        this.foundGroupEvent = null;
        this.foundPlace = null;
        this.foundSpecialEvent = null;
        this.curIntentList = new ArrayList<Intent>();
        this.catalogueType = Common.CATALOGUE_TIME;
        this.catalogueItem = null;
    }

    public static synchronized SharedObject getInstance(){
        if(instance == null)
            instance = new SharedObject();
        return instance;
    }

    public void setFoundPlace(PlaceObject place) { this.foundPlace = place; }

    public PlaceObject getFoundPlace() { return this.foundPlace; }

    public void setFoundGroupEvent(EventItem eventItem){ this.foundGroupEvent = eventItem; }

    public EventItem getFoundGroupEvent(){ return  this.foundGroupEvent; }

    public void setFoundSpecialEvent(EventItem eventItem) { this.foundGroupEvent = eventItem; }

    public EventItem getFoundSpecialEvent() { return this.foundSpecialEvent; }

    public void setCurIntent(Intent intent) {
        this.curIntentList.add(0, intent);
    }

    public Intent getCurIntent() {
        if(this.curIntentList.isEmpty())
            return null;
        return  this.curIntentList.remove(0);
    }

    public void setCatalogueType(int catalogueType) { this.catalogueType = catalogueType; }

    public int getCatalogueType() { return this.catalogueType; }

    public void setCatalogueItem(EventItem item) { this.catalogueItem = item;}

    public EventItem getCatalogueItem() { return this.catalogueItem; }
}
