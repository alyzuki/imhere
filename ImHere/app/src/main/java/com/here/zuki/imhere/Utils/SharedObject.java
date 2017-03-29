package com.here.zuki.imhere.Utils;

import android.content.Intent;

/**
 * Created by zuki on 3/29/17.
 */

public class SharedObject {

    private static SharedObject instance = null;

    private PlaceObject foundPlace;

    private EventItem foundGroupEvent;

    private EventItem foundSpecialEvent;

    private Intent curIntent;

    protected SharedObject()
    {
        this.foundGroupEvent = null;
        this.foundPlace = null;
        this.foundSpecialEvent = null;
        this.curIntent = null;
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

    public void setCurIntent(Intent intent) { this.curIntent = intent; }

    public Intent getCurIntent() { return  this.curIntent; }
}
