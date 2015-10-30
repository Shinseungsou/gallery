package com.jfsiot.hsgallery.app;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by SSS on 2015-09-22.
 */
public class AppConfig {
    public static class Option{
        public static boolean SUPER_USER;
        public static boolean MULTISELECT;
    }
    public static String currentTime(){
        Calendar c = Calendar.getInstance();
        Date date = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

        return android.text.format.DateFormat.format("yyyy/MM/dd hh:mm", date).toString();
    }
}
