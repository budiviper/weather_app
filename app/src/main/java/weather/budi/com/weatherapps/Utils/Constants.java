package weather.budi.com.weatherapps.utils;

import weather.budi.com.weatherapps.network.StringCookieRequest;

/**
 * Created by Budi on 1/11/2017.
 */

public class Constants {

    public final static String APP_ID="7c8651deb1b33a65f059ec6acc4daa8f";
    public final static String BASE_URL_API = "http://api.openweathermap.org/";

    public final static String URL_IMG = "img/w/";
    public final static String URL_DATA = "data/2.5/";

    public final static int CITY_ID_JAKARTA = 1642907;

    public final static String UNIT_CELCIUS = "metric";
    public final static String UNIT_FARENHEIT = "imperial";


    public static final String TAG = "weather app";
    public static boolean MODE_DEV = true;

    public final static String SP_LAST_CACHE_TIME = "lastcache";
    public static StringCookieRequest currentRequestString;

    public final static String TITLE_HOME = "Weather Apps";


    public static String COOKIE = "";
}
