package weather.budi.com.weatherapps.utils;

/**
 * Created by Budi on 1/11/2017.
 */

public class UrlComposer {

    public static String composeForecastByCityName(String cityName, String unit){
        return Constants.BASE_URL_API + Constants.URL_DATA + "forecast?q=" + cityName + "&units=" + unit + "&APPID=" + Constants.APP_ID;
    }

    public static String composeCurrentWeatherByCityName(String name, String unit){
        return Constants.BASE_URL_API + Constants.URL_DATA  + "weather?q=" + name + "&units=" + unit + "&APPID=" + Constants.APP_ID;
    }

    public static String composeForecastDaily(String cityName, int totalDay, String unit){
        return Constants.BASE_URL_API + Constants.URL_DATA  + "forecast/daily?q=" + cityName + "&cnt=" + totalDay + "&units=" + unit + "&APPID=" + Constants.APP_ID;
    }

    public static String composeCurrentWeatherByPosition(double lon, double lat,  String unit){
        return Constants.BASE_URL_API + Constants.URL_DATA  + "weather?lat=" + lat + "&lon=" + lon + "&units=" + unit + "&APPID=" + Constants.APP_ID;
    }

    public static String composeWeatherIcon(String icon){
        return Constants.BASE_URL_API + Constants.URL_IMG + icon + ".png";
    }
}
