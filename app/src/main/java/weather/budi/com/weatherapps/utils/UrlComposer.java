package weather.budi.com.weatherapps.utils;

/**
 * Created by Budi on 1/11/2017.
 */

public class UrlComposer {

    public static String composeForecastByCityId(int cityId, String unit){
        return Constants.BASE_URL_API + Constants.URL_DATA + "forecast/city?id=" + cityId + "&units=" + unit + "metric&APPID=" + Constants.APP_ID;
    }

    public static String composeTodayWeather(int cityId, String unit){
        return Constants.BASE_URL_API + Constants.URL_DATA  + "weather?id=" + cityId + "&units=" + unit + "&APPID=" + Constants.APP_ID;
    }

    public static String composeWeatherIcon(String icon){
        return Constants.BASE_URL_API + Constants.URL_IMG + icon + ".png";
    }
}
