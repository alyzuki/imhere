package com.here.zuki.imhere;

/**
 * Created by zuki on 2/16/17.
 */



public class Common {
    public enum  BtnPosition
    {
        Left,
        Right,
    }

    public static class SearchOptElement
    {
        private boolean notification;
        private float distant;
        private boolean opts_name;
        private boolean opts_phone;
        private boolean opts_id;

        public  SearchOptElement(){super();}
        public void setNotification(boolean check)
        {
            this.notification = check;
        }
        public boolean isNotification(){return  this.notification ;}

        public void setFollowName(boolean check)
        {
            this.opts_name = check;
        }
        public boolean isFollowName(){return  this.opts_name; }

        public void setFollowPhone(boolean check)
        {
            this.opts_phone = check;
        }
        public boolean isFollowPhone(){return  this.opts_phone; }

        public void setFollowSocialId(boolean check)
        {
            this.opts_id = check;
        }
        public boolean isFollowSocialId(){return  this.opts_id ;}

        public void setDistance(float dis)
        {
            this.distant = dis;
        }
        public float setDistance(){return  this.distant ;}

        public void setConfig() {}

        public void loadConfig(){}
    }


}
