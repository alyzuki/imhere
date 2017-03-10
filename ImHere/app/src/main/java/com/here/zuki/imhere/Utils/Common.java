package com.here.zuki.imhere.Utils;

import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.plus.model.people.Person;
import com.here.zuki.imhere.Childitem;

import java.util.ArrayList;

/**
 * Created by zuki on 3/10/17.
 */

public class Common extends AppCompatActivity{



    public void setHeightOfChild(View parent, ArrayList<Childitem> childitems, int height)
    {
        if(childitems == null)
            return;
        if(parent instanceof ViewGroup)
        {
            int count = ((ViewGroup) parent).getChildCount();
            for(int i = 0; i < count; i++)
            {
                int heightSet = 0;
                View child = ((ViewGroup) parent).getChildAt(i);
                if(child  instanceof ViewGroup) {
                    setHeightOfChild(child, childitems, height);
                }

                if(child.getId() == -1)
                    continue;

                switch (height)
                {
                    case -1:
                        Childitem item = new Childitem(child.getId(), child.getWidth(), child.getHeight());
                        childitems.add(item);
                        break;
                    case  0:
                        break;
                    default:
                        for (Childitem items : childitems )
                        {
                            if(child.getId() == items.getID())
                                heightSet = items.getHeight();
                        }
                        break;
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)child.getLayoutParams();
                params.height = heightSet;
                child.setLayoutParams(params);
                child.requestLayout();
                Log.d("COMMON", child.toString() + "\n" + String.valueOf(heightSet));
            }
        }
        parent.requestLayout();
    }
}
