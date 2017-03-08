package com.here.zuki.imhere;

/**
 * Created by zuki on 3/8/17.
 */

public class Options {

    //Search option
    public class Search{
        private  boolean    bNotification;
        private  float      fDistance;
        private  boolean    bFindById;
        private  boolean    bFindByPhone;
        private  boolean    bFindByName;
        protected  Search()
        {
            bNotification = false;
            bFindById = false;
            bFindByName = false;
            bFindByPhone = false;
            fDistance       = (float) 0.0;
        }
    }

    //SOS option
    public class SOS{
        private  boolean bShow;
        protected SOS(){
            bShow = false;
        }
    }

    //add option
    public class Add{
        private  boolean bShow;
        protected Add(){
            bShow = false;
        }
    }

    //follow option
    public class Follow{
        private  boolean followable;
    }

}
