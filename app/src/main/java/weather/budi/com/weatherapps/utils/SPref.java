package weather.budi.com.weatherapps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPref {
    static final String app_name = "weather_app";

    public static long getPref(Context context, String key, long defValue) {
        SharedPreferences p = context.getSharedPreferences(app_name, Context.MODE_PRIVATE);
        return p.getLong(key, defValue);
    }

    public static int getPref(Context context, String key, int defValue) {
        SharedPreferences p = context.getSharedPreferences(app_name, Context.MODE_PRIVATE);
        return p.getInt(key, defValue);
    }

    public static String getPref(Context context, String key, String defValue) {
        SharedPreferences p = context.getSharedPreferences(app_name, Context.MODE_PRIVATE);
        return p.getString(key, defValue);
    }

    public static boolean getPref(Context context, String key, boolean defValue) {
        SharedPreferences p = context.getSharedPreferences(app_name, Context.MODE_PRIVATE);
        return p.getBoolean(key, defValue);
    }

    public static void setPref(Context context, String key, long value) {
        SharedPreferences p = context.getSharedPreferences(app_name, Context.MODE_PRIVATE);
        Editor e = p.edit();
        e.putLong(key, value);
        e.commit();
    }

    public static void setPref(Context context, String key, int value) {
        SharedPreferences p = context.getSharedPreferences(app_name, Context.MODE_PRIVATE);
        Editor e = p.edit();
        e.putInt(key, value);
        e.commit();
    }

    public static void setPref(Context context, String key, boolean value) {
        SharedPreferences p = context.getSharedPreferences(app_name, Context.MODE_PRIVATE);
        Editor e = p.edit();
        e.putBoolean(key, value);
        e.commit();
    }

    public static void setPref(Context context, String key, String value) {
        SharedPreferences p = context.getSharedPreferences(app_name, Context.MODE_PRIVATE);
        Editor e = p.edit();
        e.putString(key, value);
        e.commit();
    }
}
