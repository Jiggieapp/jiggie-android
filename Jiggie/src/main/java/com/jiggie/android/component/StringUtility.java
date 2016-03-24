package com.jiggie.android.component;

import android.text.TextUtils;

import com.jiggie.android.model.Common;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by rangg on 17/11/2015.
 */
public class StringUtility {
    public static String[] toStringArray(JSONArray json) {
        final int length = json == null ? 0 : json.length();
        final String[] result = new String[length];

        for (int i = 0; i < length; i++)
            result[i] = json.optString(i);

        return result;
    }

    public static int indexOf(String[] array, String value, boolean ignoreCase) {
        final int length = array == null ? 0 : array.length;

        for (int i = 0; i < length; i++) {
            if (isEquals(array[i], value, ignoreCase))
                return i;
        }

        return -1;
    }

    public static boolean contains(String[] array, String value, boolean ignoreCase) {
        return indexOf(array, value, ignoreCase) >= 0;
    }

    public static boolean isEquals(String str1, String str2, boolean ignoreCase) {
        if (str1 == null)
            return str2 == null;
        else if (str2 == null)
            return false;
        else if (ignoreCase)
            return str1.equalsIgnoreCase(str2);
        else
            return str1.equals(str2);
    }

    public static String formatCharacterCase(String name) {
        if (TextUtils.isEmpty(name))
            return name;
        else if (name.length() == 1)
            return name.toUpperCase();
        else
            return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public static String getAge(String dateBirth) {
        try {
            if (TextUtils.isEmpty(dateBirth))
                return null;

            final Date date = Common.ISO8601_DATE_FORMAT.parse(dateBirth);
            final Calendar cal1 = Calendar.getInstance();
            final Calendar cal2 = Calendar.getInstance();

            cal1.setTime(date);
            cal2.set(Calendar.MINUTE, 0);
            cal2.set(Calendar.SECOND, 0);
            cal2.set(Calendar.HOUR_OF_DAY, 0);

            int age = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
            if (cal2.get(Calendar.DAY_OF_YEAR) < cal1.get(Calendar.DAY_OF_YEAR))
                age--;

            return String.valueOf(age);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String getAge2(String dateBirth) {
        try {
            if (TextUtils.isEmpty(dateBirth))
                return null;

            final Date date = Common.FACEBOOK_DATE_FORMAT.parse(dateBirth);
            final Calendar cal1 = Calendar.getInstance();
            final Calendar cal2 = Calendar.getInstance();

            cal1.setTime(date);
            cal2.set(Calendar.MINUTE, 0);
            cal2.set(Calendar.SECOND, 0);
            cal2.set(Calendar.HOUR_OF_DAY, 0);

            int age = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
            if (cal2.get(Calendar.DAY_OF_YEAR) < cal1.get(Calendar.DAY_OF_YEAR))
                age--;

            return String.valueOf(age);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static long getCountdownTime(String timelimit) {
        try {
            if (TextUtils.isEmpty(timelimit))
                return 0;

            final Date date = Common.ISO8601_DATE_FORMAT.parse(timelimit);
            final Calendar cal = Calendar.getInstance();

            cal.setTime(date);

            int offset = cal.getTimeZone().getOffset(cal.getTimeInMillis());;

            long timeF = cal.getTimeInMillis() + offset;
            long timeNow = System.currentTimeMillis();
            long countdownTime = (timeF - timeNow);

            return countdownTime;
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String getTimeFormat(long hour, long minute, long second){

        /*long hour = TimeUnit.MILLISECONDS.toHours(milisecond);
        long minute = (TimeUnit.MILLISECONDS.toMinutes(milisecond)/hour);
        long second = (TimeUnit.MILLISECONDS.toSeconds(milisecond)/minute);*/

        /*long hour = ((milisecond/1000)/60)/60;
        long minute = ((milisecond/1000)/60)/3;
        long second = TimeUnit.MILLISECONDS.toSeconds(minute);*/

        String time = String.format("%02d:%02d:%02d",hour,minute,second);
        return time;
    }

    //added by Wandy 12-02-2016
    public static String getAge3(String dateBirth) {
        try {
            if (TextUtils.isEmpty(dateBirth))
                return null;

            final Date date = Common.SHORT_DATE_FORMAT.parse(dateBirth);
            final Calendar cal1 = Calendar.getInstance();
            final Calendar cal2 = Calendar.getInstance();

            cal1.setTime(date);
            cal2.set(Calendar.MINUTE, 0);
            cal2.set(Calendar.SECOND, 0);
            cal2.set(Calendar.HOUR_OF_DAY, 0);

            int age = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
            if (cal2.get(Calendar.DAY_OF_YEAR) < cal1.get(Calendar.DAY_OF_YEAR))
                age--;

            return String.valueOf(age);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    public static String getRupiahFormat(String number) {
        String displayedString = "";

        if (number.length() == 0) {
            displayedString = "Rp0";
        } else {
            if (number.length() > 3) {
                int length = number.length();

                for (int i = length; i > 0; i -= 3) {
                    if (i > 3) {
                        String myStringPrt1 = number.substring(0, i - 3);
                        String myStringPrt2 = number.substring(i - 3);

                        String combinedString;

                        combinedString = myStringPrt1 + ".";

                        combinedString += myStringPrt2;
                        number = combinedString;

                        displayedString = "Rp" + combinedString;
                    }
                }
            } else {
                displayedString = "Rp" + number;
            }
        }
        return displayedString;
    }

    public static String getCCNumberFormat(String input){
        String output = input.replaceAll("(\\d{4})(?=\\d)", "$1-");
        return output;
    }
}
