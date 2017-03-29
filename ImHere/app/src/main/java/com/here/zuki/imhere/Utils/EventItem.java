package com.here.zuki.imhere.Utils;

import java.util.ArrayList;

/**
 * Created by zuki on 3/26/17.
 */

public class EventItem {
    private int type;
    private String eventNane;

    public  EventItem(){}

    public EventItem(String eventNane, int  type)
    {
        this.eventNane = eventNane;
        this.type = type;
    }
    public String getEventNane()
    {
        return  this.eventNane;
    }

    public void setEventNane(String name)
    {
        this.eventNane = name;
    }

    public int getType() { return  this.type;}


    public final ArrayList<EventItem> getListTest()
    {
        ArrayList<EventItem> list = new ArrayList<EventItem>();
        for(int i = 0; i < 10; i++)
        {
            EventItem item = new EventItem("Event " + String.valueOf(i), i % Common.EVENT_TYPE_COUNT);
            list.add(item);
        }
        return  list;
    }


    public final void changeList(ArrayList<EventItem> list)
    {
        list.clear();
        for(int i = 0; i < 10; i++)
        {
            EventItem item = new EventItem("Event change " + String.valueOf(i), i % Common.EVENT_TYPE_COUNT);
            list.add(item);
        }
    }
}
