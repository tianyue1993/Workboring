package com.etcomm.dcare.app.model.json;

import com.etcomm.dcare.app.model.PedometerItem;

import java.util.List;

/**
 * Created by etc on 12/7/16.
 */
public class PedometerDataJson extends BaseJson {

    /**
     * data : [{"step":0,"distance_text":"","total_time":0,"target":8000,"date":"07/05","calorie":0,"distance":"0.0","calorie_text":""},{"step":106,"distance_text":"0.1公里，是8.3个集装箱连接的长度","total_time":0,"target":8000,"date":"07/06","calorie":3,"distance":"0.1","calorie_text":""},{"step":0,"distance_text":"","total_time":0,"target":8000,"date":"07/07","calorie":0,"distance":"0.0","calorie_text":""},{"step":0,"distance_text":"","total_time":0,"target":8000,"date":"07/08","calorie":0,"distance":"0.0","calorie_text":""},{"step":0,"distance_text":"","total_time":0,"target":8000,"date":"07/09","calorie":0,"distance":"0.0","calorie_text":""},{"step":0,"distance_text":"","total_time":0,"target":8000,"date":"07/10","calorie":0,"distance":"0.0","calorie_text":""},{"step":20000,"distance_text":"5.0公里，相当于绕故宫1.5圈","total_time":0.9,"target":8000,"date":"07/11","calorie":286,"distance":"5.0","calorie_text":"≈消耗了6块鸡块的热量"},{"step":0,"distance_text":"","total_time":0,"target":0,"date":"07/12","calorie":0,"distance":0,"calorie_text":""}]
     * type : 1
     */

    public Content content;


    public class Content {
        public int type;

        public String device_mac;
        /**
         * step : 0
         * distance_text :
         * total_time : 0
         * target : 8000
         * date : 07/05
         * calorie : 0
         * distance : 0.0
         * calorie_text :
         */

        //减少改动
        public List<PedometerItem> data;

//        public List<PedometerDataItem> data;      


//        public  class PedometerDataItem {
//            public int step;
//            public String distance_text;
//            public int total_time;
//            public int target;
//            public String date;
//            public int calorie;
//            public String distance;
//            public String calorie_text;
//
//          
//        }
    }
}
