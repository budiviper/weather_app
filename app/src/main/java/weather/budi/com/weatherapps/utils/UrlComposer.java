package weather.budi.com.weatherapps.utils;

/**
 * Created by Budi on 1/11/2017.
 */

public class UrlComposer {

    public static String composeCurrentWeatherByCityName(String cityName, String unit){
        return Constants.BASE_URL_API + Constants.URL_DATA  + "weather?q=" + StringUtils.urlEncode(cityName) + "&units=" + unit + "&APPID=" + Constants.APP_ID;
    }

    public static String composeWeatherByGroupCityId(String groupCityId, String unit){
        return Constants.BASE_URL_API + Constants.URL_DATA  + "group?id=" + StringUtils.urlEncode(groupCityId) + "&units=" + unit + "&APPID=" + Constants.APP_ID;
    }

    public static String composeForecastDaily(String cityName, int totalDay, String unit){
        return Constants.BASE_URL_API + Constants.URL_DATA  + "forecast/daily?q=" + StringUtils.urlEncode(cityName) + "&cnt=" + totalDay + "&units=" + unit + "&APPID=" + Constants.APP_ID;
    }

    public static String composeWeatherIcon(String icon){
        return Constants.BASE_URL_API + Constants.URL_IMG + icon + ".png";
    }

}
