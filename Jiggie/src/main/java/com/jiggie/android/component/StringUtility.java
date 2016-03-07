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

    public static String getTimeFormat(long milisecond){
        String time = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milisecond),
                TimeUnit.MILLISECONDS.toMinutes(milisecond),
                TimeUnit.MILLISECONDS.toSeconds(milisecond));
        return time;
    }
}
