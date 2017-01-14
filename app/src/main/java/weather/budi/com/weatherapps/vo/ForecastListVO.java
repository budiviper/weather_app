package weather.budi.com.weatherapps.vo;

import java.util.List;

/**
 * Created by Budi on 1/14/2017.
 */

public class ForecastListVO {
    private long dt;
    private TemperatureVO main;
    private List<WeatherVO> weather;

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public TemperatureVO getMain() {
        return main;
    }

    public void setMain(TemperatureVO main) {
        this.main = main;
    }

    public List<WeatherVO> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherVO> weather) {
        this.weather = weather;
    }
}
