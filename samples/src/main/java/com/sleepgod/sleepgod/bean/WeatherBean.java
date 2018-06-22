package com.sleepgod.sleepgod.bean;

import java.util.List;

/**
 * Created by cool on 2018/6/21.
 */
public class WeatherBean {
    public String message;
    public AreaInfoBean areaInfo;
    public WeatherInfoBean weatherInfo;
    public int code;

    @Override
    public String toString() {
        return "WeatherBean{" +
                "message='" + message + '\'' +
                ", areaInfo=" + areaInfo +
                ", weatherInfo=" + weatherInfo +
                ", code=" + code +
                '}';
    }

    public static class AreaInfoBean {
        public String province;
        public String city;
        public String county;

        @Override
        public String toString() {
            return "AreaInfoBean{" +
                    "province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", county='" + county + '\'' +
                    '}';
        }
    }

    public static class WeatherInfoBean {

        public RealBean real;
        public TodayBean today;
        public List<H3Bean> h3;

        @Override
        public String toString() {
            return "WeatherInfoBean{" +
                    "real=" + real +
                    ", today=" + today +
                    ", h3=" + h3 +
                    '}';
        }

        public static class RealBean {

            public String icomfort;
            public String wdd;
            public double rain;
            public double feelst;
            public double tem;
            public String aq;
            public double humidity;
            public String publish_time;
            public String weather;
            public String wdp;
            public int aqi;

            @Override
            public String toString() {
                return "RealBean{" +
                        "icomfort='" + icomfort + '\'' +
                        ", wdd='" + wdd + '\'' +
                        ", rain=" + rain +
                        ", feelst=" + feelst +
                        ", tem=" + tem +
                        ", aq='" + aq + '\'' +
                        ", humidity=" + humidity +
                        ", publish_time='" + publish_time + '\'' +
                        ", weather='" + weather + '\'' +
                        ", wdp='" + wdp + '\'' +
                        ", aqi=" + aqi +
                        '}';
            }
        }

        public static class TodayBean {
            public String wdd_day;
            public String wdp_night;
            public String weather_day;
            public double tem_day;
            public String weather_night;
            public String date;
            public String wdd_night;
            public String wdp_day;
            public double tem_night;

            @Override
            public String toString() {
                return "TodayBean{" +
                        "wdd_day='" + wdd_day + '\'' +
                        ", wdp_night='" + wdp_night + '\'' +
                        ", weather_day='" + weather_day + '\'' +
                        ", tem_day=" + tem_day +
                        ", weather_night='" + weather_night + '\'' +
                        ", date='" + date + '\'' +
                        ", wdd_night='" + wdd_night + '\'' +
                        ", wdp_day='" + wdp_day + '\'' +
                        ", tem_night=" + tem_night +
                        '}';
            }
        }

        public static class H3Bean {

            public String date;
            public String wdp;
            public String wdd;
            public float rain;
            public String weather;
            public String humidity;
            public String tem;
            public String time;
            public double wds;
            public String cloud;
            public String vis;

            @Override
            public String toString() {
                return "H3Bean{" +
                        "date='" + date + '\'' +
                        ", wdp='" + wdp + '\'' +
                        ", wdd='" + wdd + '\'' +
                        ", rain=" + rain +
                        ", weather='" + weather + '\'' +
                        ", humidity='" + humidity + '\'' +
                        ", tem='" + tem + '\'' +
                        ", time='" + time + '\'' +
                        ", wds=" + wds +
                        ", cloud='" + cloud + '\'' +
                        ", vis='" + vis + '\'' +
                        '}';
            }
        }
    }
}
