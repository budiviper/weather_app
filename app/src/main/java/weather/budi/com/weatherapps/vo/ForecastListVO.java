package weather.budi.com.weatherapps.vo;

import java.util.List;

/**
 * Created by Budi on 1/14/2017.
 */

public class ForecastListVO {
    private long dt;
    private TempDailyVO temp;
    private List<WeatherVO> weather;

    public TempDailyVO getTemp() {
        return temp;
    }

    public void setTemp(TempDailyVO temp) {
        this.temp = temp;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public List<WeatherVO> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherVO> weather) {
        this.weather = weather;
    }
}
